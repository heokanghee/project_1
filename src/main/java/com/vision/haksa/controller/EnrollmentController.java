package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Enrollment;
import com.vision.haksa.services.CourseService;
import com.vision.haksa.services.EnrollmentService;
import com.vision.haksa.services.StudentService;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

	private final EnrollmentService enrollmentService;
	private final CourseService courseService; // 추가
	private final StudentService studentService; // 추가

	@Autowired
	public EnrollmentController(EnrollmentService enrollmentService, CourseService courseService,
			StudentService studentService) {
		this.enrollmentService = enrollmentService;
		this.courseService = courseService; // 추가
		this.studentService = studentService; // 추가
	}

	@GetMapping("")
	public String listEnrollment(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("enrollments", enrollments);
			return "/enrollment/list";
		}

		Comparator<Enrollment> comparator = null;

		switch (sortBy) {

		case "ccode":
			comparator = Comparator.comparing(enrollment -> enrollment.getCoursecode(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "ssn":
			comparator = Comparator.comparing(enrollment -> enrollment.getSsn(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "eva":
			comparator = Comparator.comparing(enrollment -> enrollment.getEvaluation(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "tfee":
			comparator = Comparator.comparing(enrollment -> enrollment.getTuitionfee(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "att1":
			comparator = Comparator.comparing(enrollment -> enrollment.getAttendance1(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		case "att2":
			comparator = Comparator.comparing(enrollment -> enrollment.getAttendance2(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		case "att3":
			comparator = Comparator.comparing(enrollment -> enrollment.getAttendance3(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		case "att4":
			comparator = Comparator.comparing(enrollment -> enrollment.getAttendance4(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		case "att5":
			comparator = Comparator.comparing(enrollment -> enrollment.getAttendance5(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;
		}

		if (comparator != null) {
			enrollments = enrollments.stream().sorted(comparator).collect(Collectors.toList());
		}

		model.addAttribute("enrollments", enrollments);
		return "enrollment/list";
	}

	@GetMapping("/{coursecode}/{ssn}")
	public String viewEnrollment(@PathVariable String coursecode, @PathVariable String ssn, Model model) {
		Enrollment enrollment = enrollmentService.getEnrollmentById(coursecode, ssn);
		if (enrollment == null) {
			return "error";
		}
		model.addAttribute("enrollment", enrollment);
		return "enrollment/detail";
	}

	// 추가
	@GetMapping("/new")
	public String createEnrollmentForm(Model model) {
		model.addAttribute("enrollment", new Enrollment());
		model.addAttribute("courses", courseService.getAllCourses());
		model.addAttribute("students", studentService.getAllStudents());
		return "enrollment/form";
	}

	@PostMapping("/save")
	public String saveEnrollment(@ModelAttribute Enrollment enrollment, Model model) {
		enrollmentService.saveEnrollment(enrollment);
		model.addAttribute("successMessage", "수강강좌가 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

	@GetMapping("/edit/{coursecode}/{ssn}")
	public String editEnrollmentForm(@PathVariable String coursecode, @PathVariable String ssn, Model model) {
		Enrollment enrollment = enrollmentService.getEnrollmentById(coursecode, ssn);
		if (enrollment == null) {
			return "error";
		}
		model.addAttribute("enrollment", enrollment);
		return "enrollment/edit";
	}

	@PostMapping("/update/{coursecode}/{ssn}")
	public String updateEnrollment(@PathVariable String coursecode, @PathVariable String ssn,
			@ModelAttribute Enrollment enrollment, Model model) {
		try {
			enrollment.setCoursecode(coursecode);
			enrollment.setSsn(ssn);
			enrollmentService.saveEnrollment(enrollment);
			model.addAttribute("successMessage", "수강강좌를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "수강강좌를 수정하지 못 했습니다.");
		}
		return "success";
	}

	@PostMapping("/delete/{coursecode}/{ssn}")
	public String deleteLectureroom(@PathVariable String coursecode, @PathVariable String ssn, Model model) {
		try {
			enrollmentService.deleteEnrollment(coursecode, ssn);
			model.addAttribute("successMessage", "수강강좌를 삭제했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "수강강좌를 삭제하지 못 했습니다.");
		}
		return "success";
	}

}