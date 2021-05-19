package com.revature;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.beans.Position;
import com.revature.controllers.EmployeeController;
import com.revature.controllers.ReimbController;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Driver {

	private static final Logger log = LogManager.getLogger(Driver.class);
	public static void main(String[] args) {
		
		
		log.trace("Begin the TRMS application");
		final ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
		s.scheduleAtFixedRate(ReimbController::updateServer, 60, 24*60*60, TimeUnit.SECONDS);
		
		Javalin app = Javalin.create().start(8080);
		
		
		app.routes(() -> {
		    path("employees", () -> {
		    	put(EmployeeController::register);
		    	post(EmployeeController::login);
		    	delete(EmployeeController::logout);
				
				path(":employee", () -> {
		        	get(EmployeeController::viewEmployee);
		        	
		        });

		    });
		    
		   
		    
		    
		    path("reimbursements", () -> {
		    	put(ReimbController::requestCourse);
		    	get(ReimbController::viewReimbs);//ones that you need to view at the moment
		    	post(ReimbController::updateReimb);//if they want to do something besides pass it up they have to give a reason//i guess allow post files to reimb
		    	delete(ReimbController::rejectReimb);//this can be used by both benco and employee
		    	path("byemployee", () ->{
		    		path(":empID",()->{
		    			get(ReimbController::yourReimbs);
		    		});
		    		
		    	});
		    	path("byID", ()->{
		    		path(":reimbID",()->{
		    			get(ReimbController::viewReimbId);
		    			path(":fileName",()->{//gosh i'm really starting to love the way javalin let's you organize things
		    				put(ReimbController::uploadFile);
			    			get(ReimbController::downloadFile);
		    			});
		    			
		    		});
		    		
		    	});
				

		    });
		    
		});
		

		

		

		
	}
	static Position getUserRole(Context ctx) {
		   if(ctx.sessionAttribute("employee")==null)
			   return null;
			Position clearance = ((Employee) ctx.sessionAttribute("employee")).getPosition();
		    return clearance;
		}
	
}
