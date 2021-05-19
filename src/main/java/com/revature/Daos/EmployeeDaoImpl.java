package com.revature.Daos;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Department;
import com.revature.beans.Employee;
import com.revature.beans.Position;
import com.revature.factory.Log;
import com.revature.utils.CassUtil;
@Log
public class EmployeeDaoImpl implements EmployeeDao {

	private CqlSession session = CassUtil.getInstance().getSession();
	@Override
	public Employee getEmployeeById(int id) {
		Employee e=null;
		BoundStatement bound= session.prepare("Select name, tuitionReimbursed, tuitionPending, dirsupID, position, department from employee where id = ?  ALLOW FILTERING;").bind(id);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data!=null) {
			e= new Employee();
			e.setDepartment(Department.valueOf(data.getString("department")));
			e.setDirsupID(data.getInt("dirsupID"));
			e.setId(id);
			e.setName(data.getString("name"));
			e.setPosition(Position.valueOf(data.getString("position")));
			e.setTuitionReimbursed(data.getFloat("tuitionReimbursed"));
			e.setTuitionPending(data.getFloat("tuitionPending"));
		}
		return e;

	}



	@Override
	public void updateEmployee(Employee e) {
		SimpleStatement s = new SimpleStatementBuilder("insert into employee (department , dirsupID , name , tuitionReimbursed , tuitionPending , id,position) values (?,?,?,?,?,?,?)")
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(e.getDepartment().toString(),e.getDirsupID(),e.getName(),e.getTuitionReimbursed(),e.getTuitionPending(),e.getId(),e.getPosition().toString());
		session.execute(bound);

	}



	@Override
	public List<Employee> getRole(Position p) {
		
		
		BoundStatement bound= session.prepare("Select name, tuitionReimbursed, tuitionPending, dirsupID, id , department from employee where position = ?;").bind(p.toString());
		ResultSet rs = session.execute(bound);
		
		List<Employee> emps = new ArrayList<>();
		rs.forEach(row->{
			Employee e= new Employee();
			//e.setDepartment(Department.valueOf(row.getString("Department")));
			e.setDepartment(Department.valueOf(row.getString("department")));
			e.setDirsupID(row.getInt("dirsupID"));
			e.setId(row.getInt("id"));
			e.setName(row.getString("name"));
			e.setPosition(p);
			e.setTuitionReimbursed(row.getFloat("tuitionReimbursed"));
			e.setTuitionPending(row.getFloat("tuitionPending"));
			emps.add(e);
		});

		
		return emps;
	}


	

}
