package com.revature.beans;



public enum CourseType {
	UNIVERSITY(0.8f), SEMINAR(0.6f), CERT_PREP_CLASS(0.75f), CERTIFICATION(1f), TECH_TRAIN(0.9f), OTHER(0.3f);
	public final float percent;
	private CourseType(float f) {
		this.percent=f;
	}
	
}



