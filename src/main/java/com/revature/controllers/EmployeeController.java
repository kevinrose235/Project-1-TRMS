package com.revature.controllers;



import com.revature.beans.Employee;
import com.revature.factory.BeanFactory;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;

import io.javalin.http.Context;

public class EmployeeController {
	private static EmployeeService es = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, EmployeeServiceImpl.class);

	public static void register(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		Employee trye= ctx.bodyValidator(Employee.class).get();
		if (trye==null) {
			ctx.status(500);
			return;
		}
		
		boolean added = es.addEmployee(trye);
		if (added) {
			ctx.json(trye);
		} else {
			ctx.status(409);
		}
	}

	public static void login(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		// We can get session information from the Context to use it elsewhere.
		if(ctx.sessionAttribute("Employee") != null) {
			ctx.status(204);
			return;
		}
		String id = ctx.body();
		Employee u = es.getEmployee(id);
		if (u == null){
			ctx.status(401);
		} else {
			ctx.sessionAttribute("Employee", u);
			ctx.json(u);
		}
	}
	
	public static void logout(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		ctx.req.getSession().invalidate();
	}
	public static void viewEmployee(Context ctx) {
		ctx.header("Cache-Control", "no-store");
		if(ctx.sessionAttribute("Employee")==null) {
			ctx.status(401);
			return;
		}
		Employee u= es.getEmployee(ctx.pathParam("employee"));
		if(u==null) {
			ctx.status(404);
			return;
		}
		ctx.status(200);
		ctx.json(u);
		return;
	}
}
