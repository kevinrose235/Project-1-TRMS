package com.revature.services;

import com.revature.beans.Employee;

public interface EmployeeService {

	Employee getEmployee(String id);
	boolean addEmployee(Employee e);
	boolean updateEmployee(Employee e);

}
