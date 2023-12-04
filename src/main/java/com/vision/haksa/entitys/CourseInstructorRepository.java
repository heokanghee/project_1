package com.vision.haksa.entitys;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vision.haksa.compkeys.CourseInstructorId;

public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, CourseInstructorId> {
	// 복합 키를 사용하여 엔티티 조회
	// findByCoursecodeAndTeacherTeacherid 의미를 명확하게 하기 위해 길게 적음
	// CourseInstructor findByCoursecodeAndTeacherTeacherid(String coursecode,
	// String teacherid);
	@Query("SELECT ci FROM CourseInstructor ci WHERE ci.coursecode = :coursecode AND ci.teacherid = :teacherid AND ci.lecturedate = :lecturedate")
	CourseInstructor findById(@Param("coursecode") String coursecode, @Param("teacherid") String teacherid,
			@Param("lecturedate") Date lecturedate);

	// 복합 키를 사용하여 엔티티 삭제
	@Query("DELETE FROM CourseInstructor ci WHERE ci.coursecode = :coursecode AND ci.teacherid = :teacherid AND ci.lecturedate = :lecturedate")
	void deleteById(@Param("coursecode") String coursecode, @Param("teacherid") String teacherid,
			@Param("lecturedate") Date lecturedate);

	void deleteByCoursecodeAndTeacheridAndLecturedate(String coursecode, String teacherid, Date lecturedate);

}