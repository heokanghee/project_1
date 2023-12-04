package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vision.haksa.entitys.Workplace;
import com.vision.haksa.services.WorkplaceService;

@Controller
@RequestMapping("/workplaces")
public class WorkplaceController {
	private final WorkplaceService workplaceService;

	@Autowired
	public WorkplaceController(WorkplaceService workplaceService) {
		this.workplaceService = workplaceService;
	}

//	@GetMapping("")
//	public String listWorkplaces(Model model) {
//		model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
//		return "/workplace/list";
//	}

	@GetMapping("")
	public String listWorkplaces(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<Workplace> workplaces = workplaceService.getAllWorkplaces();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("workplaces", workplaces);
			return "workplace/list";
		}

		Comparator<Workplace> comparator = null;

		switch (sortBy) {
		case "regno":
			comparator = Comparator.comparing(workplace -> workplace.getBusinessregistrationnumber(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;
		
		case "cname":
			comparator = Comparator.comparing(workplace -> workplace.getCompanyname(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;
		}

		if (comparator != null) {
			workplaces = workplaces.stream()
					.sorted(comparator)
					.collect(Collectors.toList());
		}

		model.addAttribute("workplaces", workplaces);
		return "workplace/list";
	}

	@GetMapping("/{businessregistrationnumber}")
	public String viewWorkplace(@PathVariable String businessregistrationnumber, Model model) {
		Workplace workplace = workplaceService.getWorkplaceById(businessregistrationnumber);
		if (workplace == null) {
			return "error";
		}
		model.addAttribute("workplace", workplace);
		return "/workplace/detail";
	}

	@GetMapping("/add")
	public String createWorkplaceForm(Model model) {
		return "/workplace/form";
	}

//	@PostMapping("/create")
//	public String createWorkplace(@ModelAttribute Workplace workplace) {
//		workplaceService.saveWorkplace(workplace);
//		return "redirect:/workplaces";
//	}

	@PostMapping("/create")
	public String createWorkplace(@ModelAttribute Workplace workpalce, Model model) {
		workplaceService.saveWorkplace(workpalce);
		model.addAttribute("successMessage", "근무처가 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

	@GetMapping("/edit/{businessregistrationnumber}")
	public String editWorkplaceForm(@PathVariable String businessregistrationnumber, Model model) {
		Workplace workplace = workplaceService.getWorkplaceById(businessregistrationnumber);
		if (workplace == null) {
			return "error";
		}
		model.addAttribute("workplace", workplace);
		return "/workplace/edit";
	}

	@PostMapping("/update/{businessregistrationnumber}")
	public String updateWorkplace(@PathVariable String businessregistrationnumber, @ModelAttribute Workplace workplace,
			Model model) {
		try {
			workplace.setBusinessregistrationnumber(businessregistrationnumber);
			workplaceService.saveWorkplace(workplace);
			model.addAttribute("successMessage", "근무처를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "근무처를 수정하지 못했습니다.");
		}
		return "success";
	}

	@PostMapping("/delete/{businessregistrationnumber}")
	public String deleteWorkplace(@PathVariable String businessregistrationnumber, Model model) {
		try {
			workplaceService.deleteWorkplace(businessregistrationnumber);
			model.addAttribute("successMessage", "근무처를 삭제했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "근무처를 삭제하지 못했습니다.");
		}
		return "success";
	}

	@GetMapping("/form")
	public String getForm(Model model) {
		Workplace workplace = new Workplace();
		model.addAttribute("workplace", workplace);
		return "workplace/form";
	}

}
