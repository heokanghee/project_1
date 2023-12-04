package com.vision.haksa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.vision.haksa.compkeys.CourseInstructorId;

@Entity
@IdClass(CourseInstructorId.class) // 복합 기본 키 클래스
@Table(name = "Courseinstructors")
public class CourseInstructor {

	@Id // 엔터티의 기본 키
	@Column(name = "coursecode")
	private String coursecode;

	@Id
	@Column(name = "teacherid")
	private String teacherid;

	@Id // 추가
	@Column(name = "lecturedate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lecturedate;

	@Column(name = "lecturetime")
	private String lecturetime;

	@Column(name = "lectureevaluation")
	private int lectureevaluation;

	@ManyToOne // 다대일(Many-to-One) 관계
	// @JoinColumn: 외래 키(Foreign Key)
	@JoinColumn(name = "coursecode", referencedColumnName = "coursecode", insertable = false, updatable = false)
	public Course course;

	@ManyToOne
	@JoinColumn(name = "teacherid", referencedColumnName = "teacherid", insertable = false, updatable = false)
	private Teacher teacher;

	public CourseInstructor() {
		// 기본 생성자
	}

	public CourseInstructor(String coursecode, String teacherid, Date lecturedate) {
		this.coursecode = coursecode;
		this.teacherid = teacherid;
		this.lecturedate = lecturedate;
	}

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}

	public String getTeacherid() {
		return teacherid;
	}

	public void setTeacherid(String teacherid) {
		this.teacherid = teacherid;
	}

	public Date getLecturedate() {
		return lecturedate;
	}

	// lecturedate가 null일 경우에만 현재날짜로 초기화하고, 시간 정보를 제거한 새로운 Date 객체를 생성하고 저장
	public void setLecturedate(Date lecturedate) {
		if (lecturedate == null) {
			lecturedate = new Date();
		}
		Date dateOnly = new Date(lecturedate.getTime());

		this.lecturedate = dateOnly;
	}
	
// VS lecturedate - 어떠한 추가적인 조건 검사나 변환을 수행하지 않고, 그냥 입력된 lecturedate를 그대로 저장
//	public void setLecturedate(Date lecturedate) {
//	    this.lecturedate = lecturedate;
//	}
	
	public String getLecturetime() {
		return lecturetime;
	}

	public void setLecturetime(String lecturetime) {
		this.lecturetime = lecturetime;
	}

	public int getLectureevaluation() {
		return lectureevaluation;
	}

	public void setLectureevaluation(int lectureevaluation) {
		this.lectureevaluation = lectureevaluation;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}