package com.revature.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.revature.beans.Employee;
import com.revature.beans.Reimbursement;

public interface ReimbService {


	public boolean addReimb(Reimbursement r);

	public Map<Integer, Boolean> setProcess(Reimbursement r);

	public List<Reimbursement> viewReimbs(Employee u);



	public Integer curPro(Reimbursement r);

	public boolean updateReimb(Reimbursement r);

	public Reimbursement nextPro(Reimbursement delta, Reimbursement cur);
	
	void updateServer();

	public List<Reimbursement> yourReimbs(Integer idInt);
	public Reimbursement getReimb(int id);


	public void uploadFile(Integer reqIdInt, String pathParam, byte[] bodyAsBytes);

	public byte[] downloadFile(Integer reqIdInt, String pathParam)throws IOException ;
	
	
}
