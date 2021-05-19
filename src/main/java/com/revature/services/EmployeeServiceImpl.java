package com.revature.services;

import com.revature.Daos.EmployeeDao;
import com.revature.Daos.EmployeeDaoImpl;
import com.revature.beans.Employee;
import com.revature.factory.Log;
@Log
public class EmployeeServiceImpl implements EmployeeService {
	
	EmployeeDao ed= new EmployeeDaoImpl();
	@Override
	public Employee getEmployee(String id) {
		int idInt = 0;
		try {
			idInt=Integer.parseInt(id);
		} catch (NumberFormatException e) {
			System.out.println("ruhro");
			e.printStackTrace();
			return null;
			
		}
		
		return ed.getEmployeeById(idInt);
	}

	@Override
	public boolean addEmployee(Employee e) {
		if(ed.getEmployeeById(e.getId())==null) {
			ed.updateEmployee(e);//upsert... should probably change the name
			return true;
		}
		return false;
	}

	@Override
	public boolean updateEmployee(Employee e) {
		if(ed.getEmployeeById(e.getId())!=null) {
			ed.updateEmployee(e);//upsert... should probably change the name
			return true;
		}
		return false;
	}



}
