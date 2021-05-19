package com.revature.Daos;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.Position;

public interface EmployeeDao {

	void updateEmployee(Employee e);
	public List<Employee> getRole(Position p);
	Employee getEmployeeById(int idInt);

	
}
