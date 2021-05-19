package com.revature;




import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Course;
import com.revature.beans.CourseType;
import com.revature.beans.Department;
import com.revature.beans.Employee;
import com.revature.beans.Position;
import com.revature.beans.Reimbursement;
import com.revature.utils.CassUtil;
import com.revature.utils.S3Util;

public class Setup {

	public static void main(String[] args) {
		 //dbtest(); // created my keyspace
				// playerTable(); // created a table
				// addPlayersToTable(); // inserted into the table
				// readPlayers();
		createEmployee();
		//testBucket();
				// createQuestionTable();
				// createQuizTable();
				
				// createMaterializedView();
		createReimbursement();
	}
	
	private static void testBucket() {
		String name = "amoosing.jpg";
		byte[] bytes=null;
		try {
			bytes = Files.readAllBytes(Paths.get("C:\\Users\\Kevin\\Pictures\\amoosing.jpg"));
			System.out.println("successfully got the object");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("nope");
		}
		
		try {
			S3Util.getInstance().uploadToBucket(name, bytes);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	
	public static void createEmployee() {
		StringBuilder sb; //= new StringBuilder("Drop TABLE IF EXISTS Employee;");
		//CassandraUtil.getInstance().getSession().execute(sb.toString());
		// ran into a race condition where the create wasn't finishing before we tried to insert
		
		sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Employee (")
				.append("id int, name text, department text, dirsupid int, tuitionreimbursed float, tuitionpending float, position text")
				.append(", primary key(position,id));"); //i think we could switch position to be partition
		CassUtil.getInstance().getSession().execute(sb.toString());
		
		/*Employee newEmployee = new Employee();
		
		newEmployee.setId(1);
		
		newEmployee.setDepartment(Department.IT);
		newEmployee.setName("ITHead");
		newEmployee.setPosition(Position.DEP_HEAD);
		
		
		
		ObjectMapper o = new ObjectMapper();
		String json = null;
		try {
			json = o.writeValueAsString(newEmployee);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(json);
		sb = new StringBuilder("INSERT INTO EMPLOYEE JSON '")
				.append(json)
				.append("';");
		SimpleStatement s = new SimpleStatementBuilder(sb.toString())
			.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		CassUtil.getInstance().getSession().execute(s);
		
		sb = new StringBuilder("Select * from employee;");
		ResultSet rs = CassUtil.getInstance().getSession().execute(sb.toString());
		rs.forEach(row -> {
			Employee e = new Employee();
			e.setId(row.getInt("id"));
			e.setName(row.getString("name"));
			e.setTuitionPending(row.getFloat("tuitionPending"));
			e.setTuitionReimbursed(row.getFloat("tuitionReimbursed"));
			e.setDirsupID(row.getInt("dirsupID"));
			e.setDepartment(Department.valueOf(row.getString("department")));
			e.setPosition(Position.valueOf(row.getString("position")));
			System.out.println(e);
		});
		CassUtil.getInstance().getSession().close();*/
	}

	public static void createReimbursement() {
		StringBuilder sb;// = new StringBuilder("Drop TABLE IF EXISTS Reimbursement;");
		//CassandraUtil.getInstance().getSession().execute(sb.toString());
		// ran into a race condition where the create wasn't finishing before we tried to insert
		
		sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Reimbursement (")
				.append("isActive boolean, Amount float, cost float,CourseType text,description text,justification text, special text, date text, time text, attachments set<text>,process map<int,boolean>, reqId int, id int, urgent boolean, processOrder List<int>")
				.append(", primary key((id), isActive));");
		CassUtil.getInstance().getSession().execute(sb.toString());
		
		/*Reimbursement newReimb = new Reimbursement();
		
		newReimb.setId(1);
		
		newReimb.setActive(true);;
		newReimb.setReqID(555);
		Course co= new Course();
		co.setStartDate(LocalDate.now());
		newReimb.setCourse(co);
		
		
		
		ObjectMapper o = new ObjectMapper();
		String json = null;
		try {
			json = o.writeValueAsString(newReimb);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(json);
		sb = new StringBuilder("INSERT INTO REIMBURSEMENT JSON '")
				.append(json)
				.append("';");
		SimpleStatement s = new SimpleStatementBuilder(sb.toString())
			.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		CassUtil.getInstance().getSession().execute(s);
		
		sb = new StringBuilder("Select * from Reimbursement;");
		ResultSet rs = CassUtil.getInstance().getSession().execute(sb.toString());
		List<Reimbursement> reimbs = new ArrayList<>();
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
			r.setProcess(row.getMap("process", Integer.class, Boolean.class));
			r.setReqID(row.getInt("reqID"));
			r.setUrgent(row.getBoolean("urgent"));
			reimbs.add(r);
		});*/
		CassUtil.getInstance().getSession().close();
	}
/*	public static void readPlayers() {
		StringBuilder sb = new StringBuilder("Select * from player;");
		ResultSet rs = CassandraUtil.getInstance().getSession().execute(sb.toString());
		rs.forEach(p -> {
			Player player = new Player();
			player.setName(p.getString("name"));
			player.setRole(Player.Role.valueOf(p.getString("role")));
			player.setScore(p.getInt("score"));
			System.out.println(player);
		});
	}

	public static void addPlayersToTable() {
		PlayerDao textDao = new PlayerDaoFile();
		List<Player> players = textDao.getPlayers();

		for (Player p : players) {
			StringBuilder sb = new StringBuilder("INSERT INTO PLAYER (name, role, score) VALUES ('")
					.append(p.getName() + "','").append(p.getRole() + "',").append(p.getScore()).append(");");
			SimpleStatement s = new SimpleStatementBuilder(sb.toString())
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			CassandraUtil.getInstance().getSession().execute(s);
		}
	}

	public static void playerTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Player (")
				.append("name text PRIMARY KEY,").append("role text,").append("score int);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}*/

	public static void dbtest() {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append("krosetrms with replication = {")
				.append("'class':'SimpleStrategy','replication_factor':1};");
		CassUtil.getInstance().getSession().execute(sb.toString());
	}

	
	
	
}
