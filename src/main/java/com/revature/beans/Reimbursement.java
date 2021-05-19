package com.revature.beans;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Reimbursement {
	int id;
	private LocalDate dateSub;
	private Course course;
	private float amount;
	private Integer reqID;
	private boolean isUrgent;
	private boolean isActive;
	static final float TOTAL=1000;
	private Map<Integer, Boolean> process=new LinkedHashMap<Integer,Boolean>();
	private Set<String> attachments= new HashSet<String>();
	
	public Reimbursement(Course c, Employee r) {
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		this.course=c;
		this.reqID=r.getId();
		float available= TOTAL-r.getTuitionReimbursed()-r.getTuitionPending();
		
		this.amount = (available < 0) ? 0 : available < (c.cost* c.courseType.percent) ? available : Math.round(c.cost* c.courseType.percent*100.0)/100.0f;
		this.isActive=true;
		this.isUrgent=false;
		this.id= r.getId()*3;//would usually have a different more randomized version but this is for testing
	}
	public Reimbursement() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Integer getReqID() {
		return reqID;
	}
	public void setReqID(Integer reqID) {
		this.reqID = reqID;
	}
	public boolean isUrgent() {
		return isUrgent;
	}
	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Map<Integer, Boolean> getProcess() {
		return process;
	}
	public void setProcess(Map<Integer, Boolean> process) {
		this.process = process;
	}

	public static float getTotal() {
		return TOTAL;
	}

	public LocalDate getDateSub() {
		return dateSub;
	}
	public void setDateSub(LocalDate dateSub) {
		this.dateSub = dateSub;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(amount);
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((dateSub == null) ? 0 : dateSub.hashCode());
		result = prime * result + id;
		result = prime * result + (isActive ? 1231 : 1237);
		result = prime * result + (isUrgent ? 1231 : 1237);
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + ((reqID == null) ? 0 : reqID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (dateSub == null) {
			if (other.dateSub != null)
				return false;
		} else if (!dateSub.equals(other.dateSub))
			return false;
		if (id != other.id)
			return false;
		if (isActive != other.isActive)
			return false;
		if (isUrgent != other.isUrgent)
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (reqID == null) {
			if (other.reqID != null)
				return false;
		} else if (!reqID.equals(other.reqID))
			return false;
		return true;
	}
	public Set<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<String> attachments) {
		this.attachments = attachments;
	}
	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", dateSub=" + dateSub + ", course=" + course + ", amount=" + amount
				+ ", reqID=" + reqID + ", isUrgent=" + isUrgent + ", isActive=" + isActive + ", process=" + process
				+ ", attachments=" + attachments + "]";
	}
	
	
}


	