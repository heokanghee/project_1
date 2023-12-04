package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Student;
import com.vision.haksa.entitys.Workplace;
import com.vision.haksa.services.StudentService;
import com.vision.haksa.services.WorkplaceService;

@Controller
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final WorkplaceService workplaceService; // 추가

	@Autowired
	public StudentController(StudentService studentService, WorkplaceService workplaceService) {
		this.studentService = studentService;
		this.workplaceService = workplaceService; // 추가
	}

//	@GetMapping("")
//	public String listStudents(Model model) {
//		model.addAttribute("students", studentService.getAllStudents());
//		return "/student/list"; // student/list.html에 대한 뷰로 이동
//	}

	// 새로추가
	@GetMapping("")
	// Model model 데이터와 뷰를 연결해는 모델 객체, 학생 목록을 추가하여 뷰로 전달
	// @RequestParam(name = "sortBy", required = false)
	// sortBy값을 받아오는 역할
	// required=false로 설정되어 있기 때문에 sortBy 매개변수가 요청에 포함되지 않아도 됨
	public String listStudents(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		// studentService 사용하여 모든 학생을 검색하여 리스트로 가져옴
		List<Student> students = studentService.getAllStudents();

		// 만약 sortBy가 null이거나 비어있으면, 정렬 옵션이 전달되지 않았다는 것을 의미
		// 학생 목록을 그대로 뷰로 전달하고, 정렬되지 않은 상태로 표시
		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("students", students);
			return "student/list";
		}

		// Comparator 객체를 초기화, 처음에는 null로 설정
		Comparator<Student> comparator = null;

		switch (sortBy) { // sortBy 변수의 값에 따라 다른 정렬 기준을 설정
		// 만약 sortBy 값이 "name"이라면, 학생 객체를 이름(student.getFullname())으로 비교하며, null 값은 가장
		// 나중에 정렬
		case "name":
			comparator = Comparator.comparing(student -> student.getFullname(),
					Comparator.nullsLast(Comparator.naturalOrder())); // naturalOrder: 오름차순 descendingorder: 내림차순
			break;
		case "ssn":
			comparator = Comparator.comparing(student -> student.getSsn(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		case "businessRegNum":
			comparator = Comparator.comparing(student -> student.getBusinessregistrationnumber(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		default: // 위의 어떤 경우에도 해당하지 않으면 기본적으로 정렬하지 않습니다.
			break;
		}

		// comparator 변수가 null이 아닌 경우, 즉 어떤 정렬 기준이 설정되어 있다면, 학생 목록을 해당 기준으로 정렬
		if (comparator != null) {
			// students 리스트를 스트림으로 변환한 다음, sorted(comparator) 메서드를 사용하여 (comparator) 에 따라 정렬
			// 정렬된 학생 객체들은 스트림에서 다시 리스트로 수집되어 students 변수에 할당
			students = students.stream()
					.sorted(comparator)
					.collect(Collectors.toList());
		}
		// 정렬된 학생 목록을 뷰로 전달하기 위해 Spring의 Model 객체에 "students" 속성으로 추가
		model.addAttribute("students", students);
		// 정렬된 학생 목록을 보여줄 뷰 템플릿의 이름을 반환
		return "student/list";
	}

	@GetMapping("/{ssn}")
	public String viewStudent(@PathVariable String ssn, Model model) {
		Student student = studentService.getStudentById(ssn);
		if (student == null) {
			// 학생이 존재하지 않는 경우 처리
			return "error"; // 에러 페이지로 리다이렉트 또는 처리
		}
		model.addAttribute("student", student);
		return "/student/detail"; // student/detail.html에 대한 뷰로 이동
	}

	@GetMapping("/add")
	public String createStudentForm(Model model) {
		List<Workplace> workplaces = workplaceService.getAllWorkplaces();
		model.addAttribute("student", new Student()); // 추가
		model.addAttribute("workplaces", workplaces); // 추가
		return "/student/form"; // student/form.html에 대한 뷰로 이동
	}

	@PostMapping("/create")
	public String createStudent(@ModelAttribute Student student, Model model) {
		studentService.saveStudent(student);
		// 경고 메시지를 설정, +추가
		model.addAttribute("successMessage", "학생이 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

	@GetMapping("/edit/{ssn}")
	public String editStudentForm(@PathVariable String ssn, Model model) {
		Student student = studentService.getStudentById(ssn);
		if (student == null) {
			// 학생이 존재하지 않는 경우 처리
			return "error"; // 에러 페이지로 리다이렉트 또는 처리
		}
		model.addAttribute("student", student);
		return "/student/edit"; // student/edit.html에 대한 뷰로 이동
	}

//	@PostMapping("/update/{ssn}")
//	public String updateStudent(@PathVariable String ssn, @ModelAttribute Student student) {
//		student.setSsn(ssn); // 기존 학생 정보를 업데이트
//		studentService.saveStudent(student);
//		return "redirect:/students"; // 학생 목록 페이지로 리다이렉트
//	}

	@PostMapping("/update/{ssn}")
	public String updateStudent(@PathVariable String ssn, @ModelAttribute Student student, Model model) {
		try {
			student.setSsn(ssn);
			studentService.saveStudent(student);
			model.addAttribute("successMessage", "정보를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "정보가 수정하지 못했습니다..");
		}
		return "success";
	}

	@PostMapping("/delete/{ssn}")
	public String deleteStudent(@PathVariable String ssn, Model model) {
		try {
			studentService.deleteStudent(ssn);
			model.addAttribute("successMessage", "선택한 학생을 제거했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "선택한 학생을 제거하지 못했습니다.");
		}
		return "success"; // 학생 목록 페이지로 리다이렉트
	}
}