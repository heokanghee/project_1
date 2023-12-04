package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Teacher;
import com.vision.haksa.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private final TeacherService teacherService;

	@Autowired
	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping("")
	public String listTeachers(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<Teacher> teachers = teacherService.getAllTeachers();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("teachers", teachers);
			return "teacher/list";
		}

		Comparator<Teacher> comparator = null;

		switch (sortBy) {
		case "tid":
			comparator = Comparator.comparing(teacher -> teacher.getTeacherid(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "tname":
			comparator = Comparator.comparing(teacher -> teacher.getTname(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "pname":
			comparator = Comparator.comparing(teacher -> teacher.getPhonenumber(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "address":
			comparator = Comparator.comparing(teacher -> teacher.getAddress(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;

		}

		if (comparator != null) {
			teachers = teachers.stream().sorted(comparator).collect(Collectors.toList());
		}
		model.addAttribute("teachers", teachers);
		return "/teacher/list"; // teacher/list.html에 대한 뷰로 이동
	}

	@GetMapping("/{teacherid}")
	public String viewTeacher(@PathVariable String teacherid, Model model) {
		Teacher teacher = teacherService.getTeacherById(teacherid);
		if (teacher == null) {
			// 선생님을 찾을 수 없는 경우 처리
			return "error"; // 에러 페이지로 리다이렉트 또는 처리
		}
		model.addAttribute("teacher", teacher);
		return "/teacher/detail"; // teacher/detail.html에 대한 뷰로 이동
	}

	@GetMapping("/add")
	public String createTeacherForm(Model model) {
		// model.addAttribute("teacher", new Teacher());
		return "/teacher/form"; // teacher/form.html에 대한 뷰로 이동
	}

	@PostMapping("/create")
	public String createTeacher(@ModelAttribute Teacher teacher, Model model) {
		teacherService.saveTeacher(teacher);
		model.addAttribute("successMessage", "선생님이 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

//    @PostMapping("/create")
//    public String createTeacher(@ModelAttribute Teacher teacher) {
//        teacherService.saveTeacher(teacher);
//        return "redirect:/teachers"; // 선생님 목록 페이지로 리다이렉트
//    }

	@GetMapping("/edit/{teacherid}")
	public String editTeacherForm(@PathVariable String teacherid, Model model) {
		Teacher teacher = teacherService.getTeacherById(teacherid);
		if (teacher == null) {
			// 선생님을 찾을 수 없는 경우 처리
			return "error"; // 에러 페이지로 리다이렉트 또는 처리
		}
		model.addAttribute("teacher", teacher);
		return "teacher/edit";
	}

	@PostMapping("/update/{teacherid}")
	public String updateTeacher(@PathVariable String teacherid, @ModelAttribute Teacher teacher, Model model) {
		try {
			teacher.setTeacherid(teacherid); // 기존 선생님 정보를 업데이트
			teacherService.saveTeacher(teacher);
			model.addAttribute("successMessage", "선생님ID를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "선생님ID를 수정하지 못 했습니다.");
		}
		return "success";
	}

	@PostMapping("/delete/{teacherid}")
	public String deleteTeacher(@PathVariable String teacherid, Model model) {
		try {
			teacherService.deleteTeacher(teacherid);
			model.addAttribute("successMessage", "선생님ID를 삭제 했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "선생님ID를 삭제하지 못 했습니다.");
		}
		return "success";
	}

}