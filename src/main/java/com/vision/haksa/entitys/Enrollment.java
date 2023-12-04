package com.vision.haksa.entitys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.vision.haksa.compkeys.EnrollmentId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@IdClass(EnrollmentId.class) // 복합 키 클래스를 지정
@Table(name = "Enrollments")
public class Enrollment implements Serializable{
	
	@Id
	@JoinColumn(name = "coursecode")
	private String coursecode;

	@Id // 엔터티의 기본 키임
	@JoinColumn(name = "ssn") // 추가
	private String ssn;

	@Column(name = "evaluation")
	private int evaluation;

	@Column(name = "tuitionfee")
	private BigDecimal tuitionfee;

	@Column(name = "attendance1")
	private int attendance1;

	@Column(name = "attendance2")
	private int attendance2;

	@Column(name = "attendance3")
	private int attendance3;

	@Column(name = "attendance4")
	private int attendance4;

	@Column(name = "attendance5")
	private int attendance5;

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	public BigDecimal getTuitionfee() {
		return tuitionfee;
	}

	public void setTuitionfee(BigDecimal tuitionfee) {
		this.tuitionfee = tuitionfee;
	}

	public int getAttendance1() {
		return attendance1;
	}

	public void setAttendance1(int attendance1) {
		this.attendance1 = attendance1;
	}

	public int getAttendance2() {
		return attendance2;
	}

	public void setAttendance2(int attendance2) {
		this.attendance2 = attendance2;
	}

	public int getAttendance3() {
		return attendance3;
	}

	public void setAttendance3(int attendance3) {
		this.attendance3 = attendance3;
	}

	public int getAttendance4() {
		return attendance4;
	}

	public void setAttendance4(int attendance4) {
		this.attendance4 = attendance4;
	}

	public int getAttendance5() {
		return attendance5;
	}

	public void setAttendance5(int attendance5) {
		this.attendance5 = attendance5;
	}

	

}