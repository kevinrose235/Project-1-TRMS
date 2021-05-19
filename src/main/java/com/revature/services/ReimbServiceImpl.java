package com.revature.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.revature.Daos.EmployeeDao;
import com.revature.Daos.EmployeeDaoImpl;
import com.revature.Daos.ReimbDao;
import com.revature.Daos.ReimbDaoImpl;
import com.revature.beans.Employee;
import com.revature.beans.Position;
import com.revature.beans.Reimbursement;
import com.revature.factory.Log;
import com.revature.utils.S3Util;

import software.amazon.awssdk.utils.IoUtils;
@Log
public class ReimbServiceImpl implements ReimbService {
	static ReimbDao rd = new ReimbDaoImpl();
	static EmployeeDao ed = new EmployeeDaoImpl();
	

	@Override
	public boolean addReimb(Reimbursement r) {
		if (rd.getReimb(r.getId())==null) {
			r.setDateSub(LocalDate.now());
			rd.upsertReimb(r);
			return true;
		}
		return false;
	}

	@Override
	public Map<Integer, Boolean> setProcess(Reimbursement r) {
		Employee emp= ed.getEmployeeById(r.getReqID());
		Position p= emp.getPosition();
		Map<Integer,Boolean>process =r.getProcess();
		Random rand = new Random();
		List<Employee> bencos= ed.getRole(Position.BEN_CO);
		process.put(emp.getId(), true);
		if (p.posInt()==4) {
			int temp =bencos.stream()
					.filter(e->e.getId()!=emp.getId())
					.skip(rand.nextInt(bencos.size()-1))
					.findAny().get().getId();
					
			process.put(temp, false);
			return process;
		}
		if(p.posInt()==1) {
			process.put(emp.getDirsupID(), false);
		}
		if(p.posInt()<=2) {
			int temp = ed.getRole(Position.DEP_HEAD).stream().filter(e->e.getDepartment().toString().equals(emp.getDepartment().toString())).findAny().get().getId();
			process.put(temp, false);
		}
		if(p.posInt()<=3) {
			int temp = bencos.stream().filter(e->e.getDepartment().toString().equals(emp.getDepartment().toString())).findAny().get().getId();
			process.put(temp, false);
		}
		return process;
		
		
		
	}

	@Override
	public List<Reimbursement> viewReimbs(Employee u) {
		return rd.getAllActive().stream().filter(r-> this.curPro(r)==u.getId()).collect(Collectors.toList());
	}


	@Override
	public Integer curPro(Reimbursement r) {
		Map <Integer, Boolean >process=r.getProcess();
		for (Integer key : process.keySet()) {
			if (process.get(key)==false)
				return key;
		}
		return null;
	}

	@Override
	public boolean updateReimb(Reimbursement r) {
		if (rd.getReimb(r.getId())!=null) {
			rd.upsertReimb(r);
			return true;
		}
		return false;
	}

	@Override
	public Reimbursement nextPro(Reimbursement delta, Reimbursement cur) {
		Map <Integer, Boolean> process= cur.getProcess();
		for (int key :process.keySet()) {
			if(process.get(key)==false) {
				process.put(key, true);
				break;
			}
				
			
		}
		
		
		if (cur.getAmount()!=delta.getAmount()) {
			process.put(cur.getReqID(), false);
		}
		delta.setProcess(process);
		return delta;
		
	}

	@Override
	public void updateServer() {
		LocalDate today = LocalDate.now();
		rd.getAllActive().stream().forEach(r->{
			System.out.println(r);
			if(ChronoUnit.DAYS.between(r.getCourse().getStartDate(), today)<7) {
				r.setUrgent(true);
			}
			if(ChronoUnit.DAYS.between(r.getDateSub(), today)>3&&curPro(r)!=r.getReqID()) {
				r.setDateSub(today);
				r=nextPro(r, r);
			}
			System.out.println("boop");
			rd.upsertReimb(r);
		});
		
	}

	@Override
	public List<Reimbursement> yourReimbs(Integer idInt) {
		return rd.getAllActive().stream().filter(r-> r.getReqID()==idInt).collect(Collectors.toList());
	}

	@Override
	public Reimbursement getReimb(int id) {
		return rd.getReimb(id);
	}

	@Override
	public void uploadFile(Integer reqIdInt, String pathParam, byte[] bytes) {
		S3Util.getInstance().uploadToBucket(reqIdInt+"_"+pathParam, bytes);
		
		
		
		
	}

	@Override
	public byte[] downloadFile(Integer reqIdInt, String pathParam) throws IOException {
		return IoUtils.toByteArray(S3Util.getInstance().getObject(reqIdInt+"_"+pathParam));
		
	}


}
