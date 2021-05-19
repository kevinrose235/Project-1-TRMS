package com.revature.controllers;


import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import com.revature.beans.Course;
import com.revature.beans.Employee;
import com.revature.beans.Reimbursement;
import com.revature.factory.BeanFactory;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.ReimbService;
import com.revature.services.ReimbServiceImpl;
import io.javalin.http.Context;

public class ReimbController {
	private static ReimbService rs = (ReimbService) BeanFactory.getFactory().get(ReimbService.class, ReimbServiceImpl.class);
	private static EmployeeService es = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, EmployeeServiceImpl.class);
	public static void requestCourse(Context ctx){
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		Course c = ctx.bodyValidator(Course.class).get();
		if (c==null) {
			ctx.status(400);
			return;
		}
		LocalDate today= LocalDate.now();
		if (ChronoUnit.DAYS.between(today, c.getStartDate())<7) {
			ctx.status(405);
			return;
		}
		
		Reimbursement r= new Reimbursement(c,u);
		if (ChronoUnit.DAYS.between(c.getStartDate(), today)<14) {
			r.setUrgent(true);
		}
		r.setDateSub(today);
		r.setProcess(rs.setProcess(r));
		
		if (rs.addReimb(r)==true) {
			u=es.getEmployee(""+u.getId());
			u.setTuitionPending(u.getTuitionPending()+r.getAmount());
			es.updateEmployee(u);
			ctx.status(201);
			ctx.json(r);
			return;
		}
		ctx.status(400);
		
	}
	public static void viewReimbs(Context ctx) {
		
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		List<Reimbursement> list = rs.viewReimbs(u);
		ctx.json(list);
		return;
	}
	public static void updateReimb(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		Reimbursement r = ctx.bodyValidator(Reimbursement.class).get();
		if (r==null) {
			ctx.status(400);
			return;
		}
		Reimbursement r2=rs.getReimb(r.getId());
		if (rs.curPro(r2)==null || rs.curPro(r2).equals(u.getId())==false) {
			ctx.status(403);
			return;
		}
		r=rs.nextPro(r, r2);
		r.setDateSub(LocalDate.now());
		if(rs.updateReimb(r)==true) {
			ctx.status(200);
			return;
		}//we need to switch who it is
		ctx.status(400);
		return;
	}//should have a set to employee on change of thing in service layer
	public static void rejectReimb(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		Reimbursement r = ctx.bodyValidator(Reimbursement.class).get();
		if (r==null) {
			ctx.status(400);
			return;
		}
		Reimbursement r2=rs.getReimb(r.getId());
		if (rs.curPro(r2)==null || rs.curPro(r2).equals(u.getId())==false) {
			ctx.status(403);
			return;
		}
		r.setActive(false);
		ctx.json(r);
		ctx.status(200);
		return;
		
	}
	
	public static void uploadFile(Context ctx) {
		//check that you should be able to upload files and that that reimb exists
		//set reimb to have attachment
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		String reqId=ctx.pathParam("reimbID");
		Integer reqIdInt;
		try {
			reqIdInt= Integer.parseInt(reqId);
		} catch (NumberFormatException e1) {

			//e1.printStackTrace();
			ctx.status(400);
			return;
		}
		Reimbursement r= rs.getReimb(reqIdInt);
		if (r==null) {
			ctx.status(404);
			return;
		}
		if (r.getReqID()!=u.getId()) {
			ctx.status(403);
			return;
		}
		try {
			rs.uploadFile(reqIdInt, ctx.pathParam("fileName"),ctx.bodyAsBytes());
		} catch (Exception e ) {
			ctx.status(500);
			return;
		}
		//update the process
		Set<String> attachments= r.getAttachments();
		attachments.add(ctx.pathParam("fileName"));
		r.setAttachments(attachments);
		
		if(rs.curPro(r)==u.getDirsupID()&& es.getEmployee(u.getDirsupID()+"").getPosition().posInt==2){//if the current person is direct supervisor and that direct supervisor is a manager
			//System.out.println("this part blajal;jflk");
			r=rs.nextPro(r, r);
			
			
		}
		rs.updateReimb(r);
			
		
		
		ctx.status(200);
		
	}
	public static void yourReimbs(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		String id = ctx.pathParam("empID");
		Integer idInt;
		try {
			idInt= Integer.parseInt(id);
		} catch (NumberFormatException e) {
			ctx.status(404);
			
			e.printStackTrace();
			return;
		}
		if(idInt!=u.getId()) {
			ctx.status(403);
			return;
		}
		ctx.json(rs.yourReimbs(idInt));
		ctx.status(200);
		return;
		
	}
	public static void downloadFile(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		String reqId=ctx.pathParam("reimbID");
		Integer reqIdInt;
		try {
			reqIdInt= Integer.parseInt(reqId);
		} catch (NumberFormatException e1) {

			//e1.printStackTrace();
			ctx.status(400);
			return;
		}
		Reimbursement r= rs.getReimb(reqIdInt);
		if (r==null) {
			ctx.status(404);
			return;
		}
		if (rs.curPro(r)!=u.getId()) {
			ctx.status(403);
			return;
		}
		if(r.getAttachments().contains(ctx.pathParam("fileName"))==false) {
			ctx.status(400);
			return;
		}
		byte[] bytes;
		try {
			bytes = rs.downloadFile(reqIdInt, ctx.pathParam("fileName"));
		} catch (IOException e) {

			//e.printStackTrace();
			ctx.status(500);
			return;
		}
		ctx.result(bytes);
		ctx.status(200);
		
	}
	public static void viewReimbId(Context ctx){
		ctx.header("Cache-Control", "no-store");
		Employee u =ctx.sessionAttribute("Employee");
		if(u==null) {
			ctx.status(401);
			return;
		}
		String idSring =ctx.pathParam("reimbID");
		Integer id;
		try {
			id=Integer.parseInt(idSring);
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
			ctx.status(400);
			return;
		}
		Reimbursement r = rs.getReimb(id);
		if (r==null) {
			ctx.status(404);
			return;
		}
		

		ctx.json(r);
		ctx.status(200);
		return;
	}
	public static void updateServer() {
		rs.updateServer();
	}
}

