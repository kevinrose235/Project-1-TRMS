package com.revature.beans;

public enum Position {

	NONE(0),
	EMPLOYEE(1), 
	MANAGER(2),
	DEP_HEAD(3),
	BEN_CO(4);
	public final int posInt;
	public int posInt() {
		return this.posInt;
	}
	private Position(int i) {
		this.posInt=i;
	}
	
}
