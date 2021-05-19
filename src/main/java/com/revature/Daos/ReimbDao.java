package com.revature.Daos;

import java.util.List;

import com.revature.beans.Course;
import com.revature.beans.Employee;
import com.revature.beans.Reimbursement;

public interface ReimbDao {

	public Reimbursement getReimb(int id);
	public void upsertReimb(Reimbursement r);
	public List<Reimbursement> getAllActive();
	
	
}
