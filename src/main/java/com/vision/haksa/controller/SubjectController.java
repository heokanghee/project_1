package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.Subject;
import com.vision.haksa.services.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

	private final SubjectService subjectService;

	@Autowired
	public SubjectController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	@GetMapping("")
	public String listSubjects(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<Subject> subjects = subjectService.getAllSubjects();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("subjects", subjects);
			return "subject/list";
		}

		Comparator<Subject> comparator = null;

		switch (sortBy) {
		case "scode":
			comparator = Comparator.comparing(subject -> subject.getSubjectcode(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "sname":
			comparator = Comparator.comparing(subject -> subject.getSubjectname(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "tbname":
			comparator = Comparator.comparing(subject -> subject.getTextbookname(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;
		}

		if (comparator != null) {
			subjects = subjects.stream().sorted(comparator).collect(Collectors.toList());
		}

		model.addAttribute("subjects", subjects);
		return "subject/list";
	}

	@GetMapping("/{subjectcode}")
	public String viewSubject(@PathVariable String subjectcode, Model model) {
		Subject subject = subjectService.getSubjectByCode(subjectcode);
		if (subject == null) {
			return "error";
		}
		model.addAttribute("subject", subject);
		return "/subject/detail";
	}

	@GetMapping("/add")
	public String createSubjectForm(Model model) {
		return "/subject/form";
	}

	@PostMapping("/create")
	public String createSubject(@ModelAttribute Subject subject, Model model) {
		subjectService.saveSubject(subject);
		model.addAttribute("successMessage", "과목이 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";

	}

//    @PostMapping("/create")
//    public String createSubject(@ModelAttribute Subject subject) {
//        subjectService.saveSubject(subject);
//        return "redirect:/subjects";
//    }

	@GetMapping("/edit/{subjectcode}")
	public String editSubjectForm(@PathVariable String subjectcode, Model model) {
		Subject subject = subjectService.getSubjectByCode(subjectcode);
		if (subject == null) {
			return "error";
		}
		model.addAttribute("subject", subject);
		return "/subject/edit";
	}

	@PostMapping("/update/{subjectcode}")
	public String updateSubject(@PathVariable String subjectcode, @ModelAttribute Subject subject, Model model) {
		try {
			subject.setSubjectcode(subjectcode);
			subjectService.saveSubject(subject);
			model.addAttribute("successMessage", "과목을 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "과목을 수정하지 못했습니다.");
		}
		return "success";
	}

	@PostMapping("/delete/{subjectcode}")
	public String deleteSubject(@PathVariable String subjectcode, Model model) {
		try {
			subjectService.deleteSubject(subjectcode);
			model.addAttribute("successMessage", "과목을 삭제했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "과목을 삭제 하지 못했습니다.");
		}
		return "success";
	}
}