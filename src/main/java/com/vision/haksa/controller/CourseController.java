package com.vision.haksa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Course;
import com.vision.haksa.entitys.LectureRoom;
import com.vision.haksa.entitys.Subject;

import com.vision.haksa.services.CourseService;
import com.vision.haksa.services.LectureRoomService;
import com.vision.haksa.services.SubjectService;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private final CourseService courseService;
	private final SubjectService SubjectService; // 추가
	private final LectureRoomService lectureRoomService; // 추가

	@Autowired
	public CourseController(CourseService courseService, SubjectService SubjectService,
			LectureRoomService lectureRoomService) {
		this.courseService = courseService;
		this.SubjectService = SubjectService; // 추가
		this.lectureRoomService = lectureRoomService; // 추가
	}

	@GetMapping("")
	public String listCourses(Model model) {
		model.addAttribute("courses", courseService.getAllCourses());
		return "/course/list";
	}

	@GetMapping("/{coursecode}")
	public String viewCourse(@PathVariable String coursecode, Model model) {
		Course course = courseService.getCourseById(coursecode);
		if (course == null) {
			return "error";
		}
		model.addAttribute("course", course);
		return "/course/detail";
	}

	// 추가 html.form select박스 추가할 때 필요
	@GetMapping("/add")
	public String createCourseForm(Model model) {
		List<Subject> subjects = SubjectService.getAllSubjects();
		List<LectureRoom> lecturerooms = lectureRoomService.getAllLectureRooms();
		model.addAttribute("course", new Course());
		model.addAttribute("subjects", subjects);
		model.addAttribute("lecturerooms", lecturerooms);
		return "/course/form";
	}

//	@PostMapping("/create")
//	public String createCourse(@ModelAttribute Course course) {
//		courseService.saveCourse(course);
//		return "redirect:/courses";
//	}

	@PostMapping("/create")
	public String createCourse(@ModelAttribute Course course, Model model) {
		courseService.saveCourse(course);
		model.addAttribute("successMessage", "코스등록이 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

//	// 강좌 삭제 액션
//	@RequestMapping(value = "/{coursecode}", method = RequestMethod.DELETE)
//	public String deleteCourse(@PathVariable String courseCode) {
//		courseService.deleteCourse(courseCode);
//		return "redirect:/courses";
//	}

	@PostMapping("/delete/{coursecode}")
	public String deleteCourse(@PathVariable String coursecode, Model model) {
		try {
			courseService.deleteCourse(coursecode);
			model.addAttribute("successMessage", "강좌가 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "강좌가 삭제되지 못했습니다.");
		}
		return "success";
	}

	@GetMapping("/edit/{coursecode}")
	public String editCourseForm(@PathVariable String coursecode, Model model) {
		Course course = courseService.getCourseById(coursecode);
		if (course == null) {
			return "error";
		}
		model.addAttribute("course", course);
		return "/course/edit";
	}

	@PostMapping("/update/{coursecode}")
	public String updateCourse(@PathVariable String coursecode, @ModelAttribute Course course, Model model) {
		try {
			course.setCoursecode(coursecode);
			courseService.saveCourse(course);
			model.addAttribute("successMessage", "정보를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "정보를 수정하지 못했습니다.");
		}
		return "success";
	}

}