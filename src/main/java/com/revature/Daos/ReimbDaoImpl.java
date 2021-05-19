package com.revature.Daos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Course;
import com.revature.beans.CourseType;
import com.revature.beans.Reimbursement;
import com.revature.factory.Log;
import com.revature.utils.CassUtil;
@Log
public class ReimbDaoImpl implements ReimbDao {

	private CqlSession session = CassUtil.getInstance().getSession();
	EmployeeDao ed = new EmployeeDaoImpl();
	
	@Override
	public Reimbursement getReimb(int id) {
		Reimbursement r=null;
		BoundStatement bound= session.prepare("select isActive, Amount, cost, CourseType, description, justification, special,date,time, attachments,process, processOrder, reqId from Reimbursement where id=? ").bind(id);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data!=null) {
			r= new Reimbursement();
			r.setActive(data.getBoolean("isActive"));
			r.setAmount(data.getFloat("Amount"));
			Course c = new Course();
			c.setCost(data.getFloat("cost"));
			c.setCourseType(CourseType.valueOf(data.getString("CourseType")));
			c.setDescription(data.getString("description"));
			c.setJustification(data.getString("justification"));
			c.setSpecial(data.getString("special"));
			c.setStartDate(LocalDate.parse(data.getString("date")));
			c.setTime(data.getString("time"));

			r.setCourse(c);
			r.setAttachments(data.getSet("Attachments", String.class));
			r.setId(id);
			List<Integer> order= data.getList( "processOrder",Integer.class);
			Map<Integer,Boolean> unordered=data.getMap("process", Integer.class, Boolean.class);
			Map<Integer,Boolean> process= r.getProcess();
			for(int key:order) {
				process.put(key, unordered.get(key));
			}
			r.setProcess(process);
			r.setReqID(data.getInt("reqID"));
			r.setUrgent(true);
		}
		return r;
	}

	@Override
	public void upsertReimb(Reimbursement r) {
		SimpleStatement s = new SimpleStatementBuilder("insert into reimbursement ( isActive , Amount, cost, CourseType , description , justification, special, date, time, attachments, process, reqID, urgent, id, processOrder) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		Course c= r.getCourse();

		BoundStatement bound = session.prepare(s).bind(r.isActive(),r.getAmount(),c.getCost(),c.getCourseType().toString(),c.getDescription(),c.getJustification(),c.getSpecial(),c.getStartDate().toString(),c.getTime(),r.getAttachments(),r.getProcess(),r.getReqID(),r.isUrgent(),r.getId(),new ArrayList<Integer>(r.getProcess().keySet()));
		session.execute(bound);
		
	}



	@Override
	public List<Reimbursement> getAllActive() {//ooooh if i use this for all the multiples and just 
		BoundStatement bound= session.prepare("select id, Amount, cost, CourseType, description, justification, special,date,time, attachments,process, reqId, processOrder from Reimbursement where isActive=? ALLOW FILTERING").bind(true);
		ResultSet rs = session.execute(bound);
		List<Reimbursement> reimbs = new ArrayList<Reimbursement>();
		rs.forEach(row->{
			Reimbursement r = new Reimbursement();
			r.setActive(true);
			r.setAmount(row.getFloat("Amount"));
			Course c = new Course();
			c.setCost(row.getFloat("cost"));
			c.setCourseType(CourseType.valueOf(row.getString("CourseType")));
			c.setDescription(row.getString("description"));
			c.setJustification(row.getString("justification"));
			c.setSpecial(row.getString("special"));
			c.setStartDate(LocalDate.parse(row.getString("date")));
			c.setTime(row.getString("time"));

			r.setCourse(c);
			r.setAttachments(row.getSet("Attachments", String.class));

			r.setId(row.getInt("id"));
			List<Integer> order= row.getList( "processOrder",Integer.class);
			Map<Integer,Boolean> unordered=row.getMap("process", Integer.class, Boolean.class);
			Map<Integer,Boolean> process= r.getProcess();
			for(int key:order) {
				process.put(key, unordered.get(key));
			}
			r.setProcess(process);
			r.setReqID(row.getInt("reqID"));
			r.setUrgent(true);
			reimbs.add(r);
		});
		return reimbs;
	}

}
