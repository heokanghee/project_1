package com.vision.haksa.controller;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Course;
import com.vision.haksa.entitys.CourseInstructor;
import com.vision.haksa.entitys.Teacher;
import com.vision.haksa.services.CourseInstructorService;
import com.vision.haksa.services.CourseService;
import com.vision.haksa.services.TeacherService;

@Controller
@RequestMapping("/courseinstructors")
public class CourseInstructorController {

	private final CourseInstructorService courseInstructorService;
	private final CourseService courseService;
	private final TeacherService teacherService;

	@Autowired
	public CourseInstructorController(CourseInstructorService courseInstructorService, CourseService courseService,
			TeacherService teacherService) {
		this.courseInstructorService = courseInstructorService;
		this.courseService = courseService;
		this.teacherService = teacherService;
	}

	@GetMapping("")
	public String listCourseInstructor(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<CourseInstructor> courseInstructors = courseInstructorService.getAllCourseInstructors();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("courseInstructors", courseInstructors);
			return "courseinstructor/list";
		}

		Comparator<CourseInstructor> comparator = null;

		switch (sortBy) {
		case "ccode":
			comparator = Comparator.comparing(courseInstructor -> courseInstructor.getCoursecode(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "tid":
			comparator = Comparator.comparing(courseInstructor -> courseInstructor.getTeacherid(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "ldate":
			comparator = Comparator.comparing(courseInstructor -> courseInstructor.getLecturedate(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "ltime":
			comparator = Comparator.comparing(courseInstructor -> courseInstructor.getLecturetime(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "leva":
			comparator = Comparator.comparing(courseInstructor -> courseInstructor.getLectureevaluation(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;
		}

		if (comparator != null) {
			courseInstructors = courseInstructors.stream().sorted(comparator).collect(Collectors.toList());
		}

		model.addAttribute("courseInstructors", courseInstructors);
		return "courseInstructor/list";

	}

	// 추가
	@GetMapping("/new")
	public String newCourseInstructorForm(Model model) {
		CourseInstructor courseInstructor = new CourseInstructor();
		model.addAttribute("courseInstructor", new CourseInstructor());

		List<Course> courses = courseService.getAllCourses();
		List<Teacher> teachers = teacherService.getAllTeachers();

		model.addAttribute("courses", courses);
		model.addAttribute("teachers", teachers);

		return "courseinstructor/form";
	}

	@PostMapping("/new")
	public String saveCourseInstructor(@ModelAttribute CourseInstructor courseInstructor, Model model) {
		courseInstructorService.saveCourseInstructor(courseInstructor);
		model.addAttribute("successMessage", "과정강사가 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

	@GetMapping("/{coursecode}/{teacherid}/{lecturedate}")
	public String viewCourseInstructor(@PathVariable String coursecode, @PathVariable String teacherid,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date lecturedate, Model model) {
		CourseInstructor courseInstructor = courseInstructorService.findCourseInstructor(coursecode, teacherid,
				lecturedate);
		if (courseInstructor == null) {
			return "error";
		}
		model.addAttribute("courseInstructor", courseInstructor);
		return "courseinstructor/detail";
	}

	@GetMapping("/edit/{coursecode}/{teacherid}/{lecturedate}")
	public String editCourseInstructorForm(@PathVariable String coursecode, @PathVariable String teacherid,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date lecturedate, Model model) {
		CourseInstructor courseInstructor = courseInstructorService.findCourseInstructor(coursecode, teacherid,
				lecturedate);
		if (courseInstructor == null) {
			return "error";
		}
		model.addAttribute("courseInstructor", courseInstructor);
		return "courseinstructor/edit";
	}

	@PostMapping("/edit/{coursecode}/{teacherid}")
	public String updateCourseInstructor(@PathVariable String coursecode, @PathVariable String teacherid,
			@ModelAttribute CourseInstructor courseInstructor, Model model) {
		try {
			courseInstructor.setCoursecode(coursecode);
			courseInstructor.setTeacherid(teacherid);
			courseInstructorService.saveCourseInstructor(courseInstructor);
			model.addAttribute("successMessage", "과정강사 목록을 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "과정강사 목록을 수정하지 못했습니다.");
		}
		return "success";
	}

//	@GetMapping("/delete/{coursecode}/{teacherid}/{lecturedate}")
//	public String deleteCourseInstructor(@PathVariable String coursecode, @PathVariable String teacherid,
//			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date lecturedate, Model model) {
//		try {
//			courseInstructorService.deleteCourseInstructor(coursecode, teacherid, lecturedate);
//			model.addAttribute("successMessage", "과정강사를 삭제했습니다.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("successMessage", "과정강사를 삭제하지 못 했습니다.");
//		}
//		return "success";
//	}

	@PostMapping("/delete/{coursecode}/{teacherid}/{lecturedate}")
	public String deleteCourseInstructor(@PathVariable String coursecode, @PathVariable String teacherid,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date lecturedate, Model model) {
		try {
			courseInstructorService.deleteCourseInstructor(coursecode, teacherid, lecturedate);
			model.addAttribute("successMessage", "과정강사를 삭제했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "과정강사를 삭제하지 못 했습니다.");
		}
		return "success";
	}

}