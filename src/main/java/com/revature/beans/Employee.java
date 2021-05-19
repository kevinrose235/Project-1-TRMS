package com.revature.beans;

public class Employee {

	private String name;
	private int id;
	private float tuitionReimbursed=0f;
	private float tuitionPending=0f; //this for that formula
	private Integer dirsupID=null;
	private Department department;
	private Position position;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getTuitionReimbursed() {
		return tuitionReimbursed;
	}
	public void setTuitionReimbursed(float tuitionReimbursed) {
		this.tuitionReimbursed = tuitionReimbursed;
	}
	public float getTuitionPending() {
		return tuitionPending;
	}
	public void setTuitionPending(float tuitionRequested) {
		this.tuitionPending = tuitionRequested;
	}
	public Integer getDirsupID() {
		return dirsupID;
	}
	public void setDirsupID(int dirsupID) {
		this.dirsupID = dirsupID;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((dirsupID == null) ? 0 : dirsupID.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + Float.floatToIntBits(tuitionPending);
		result = prime * result + Float.floatToIntBits(tuitionReimbursed);
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
		Employee other = (Employee) obj;
		if (department != other.department)
			return false;
		if (dirsupID == null) {
			if (other.dirsupID != null)
				return false;
		} else if (!dirsupID.equals(other.dirsupID))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position != other.position)
			return false;
		if (Float.floatToIntBits(tuitionPending) != Float.floatToIntBits(other.tuitionPending))
			return false;
		if (Float.floatToIntBits(tuitionReimbursed) != Float.floatToIntBits(other.tuitionReimbursed))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", id=" + id + ", tuitionReimbursed=" + tuitionReimbursed
				+ ", tuitionPending=" + tuitionPending + ", dirsupID=" + dirsupID + ", department=" + department
				+ ", position=" + position + "]";
	}
	
	
	
	
	
	
}
