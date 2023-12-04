package com.vision.haksa.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vision.haksa.entitys.LectureRoom;
import com.vision.haksa.services.LectureRoomService;

@Controller
@RequestMapping("/lecturerooms")
public class LectureroomController {

	private final LectureRoomService lectureRoomService;

	@Autowired
	public LectureroomController(LectureRoomService lectureRoomService) {
		this.lectureRoomService = lectureRoomService;
	}

	@GetMapping("")
	public String listLecturerooms(Model model, @RequestParam(name = "sortBy", required = false) String sortBy) {
		List<LectureRoom> lecturerooms = lectureRoomService.getAllLectureRooms();

		if (sortBy == null || sortBy.isEmpty()) {
			model.addAttribute("lecturerooms", lecturerooms);
			return "lectureroom/list";
		}

		Comparator<LectureRoom> comparator = null;

		switch (sortBy) {
		case "rnumber":
			comparator = Comparator.comparing(lectureroom -> lectureroom.getRoomnumber(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "rname":
			comparator = Comparator.comparing(lectureroom -> lectureroom.getRoomname(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		case "scount":
			comparator = Comparator.comparing(lectureroom -> lectureroom.getSeatcount(),
					Comparator.nullsLast(Comparator.naturalOrder()));
			break;

		default:
			break;
		}

		if (comparator != null) {
			lecturerooms = lecturerooms.stream().sorted(comparator).collect(Collectors.toList());
		}

		model.addAttribute("lecturerooms", lecturerooms);
		return "lectureroom/list";
	}

	@GetMapping("/{roomnumber}")
	public String viewLectureroom(@PathVariable String roomnumber, Model model) {
		LectureRoom lectureroom = lectureRoomService.getLectureRoomById(roomnumber);
		if (lectureroom == null) {
			return "error";
		}
		model.addAttribute("lectureroom", lectureroom);
		return "/lectureroom/detail";
	}

	@GetMapping("/add")
	public String createLectureroomForm(Model model) {
		return "lectureroom/form";
	}

	@PostMapping("/create")
	public String createLectureroom(@ModelAttribute LectureRoom lectureroom, Model model) {
		lectureRoomService.saveLectureRoom(lectureroom);
		model.addAttribute("successMessage", "강의실이 성공적으로 저장되었습니다.");
		model.addAttribute(model);
		return "success";
	}

//	@PostMapping("/create")
//	public String createLectureroom(@ModelAttribute LectureRoom lectureroom) {
//		lectureRoomService.saveLectureRoom(lectureroom);
//		return "redirect:/lecturerooms";
//	}
//	
	@PostMapping("/delete/{roomnumber}")
	public String deleteLectureroom(@PathVariable String roomnumber, Model model) {
		try {
			lectureRoomService.deleteLectureRoom(roomnumber);
			model.addAttribute("successMessage", "강의실 번호를 삭제했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "강의실 번호를 삭제하지 못했습니다.");
		}
		return "success";
	}

	@GetMapping("/edit/{roomnumber}")
	public String editLectureroomForm(@PathVariable String roomnumber, Model model) {
		LectureRoom lectureroom = lectureRoomService.getLectureRoomById(roomnumber);
		if (lectureroom == null) {
			return "error";
		}
		model.addAttribute("lectureroom", lectureroom);
		return "/lectureroom/edit";
	}

	@PostMapping("/update/{roomnumber}")
	public String updateLectureroom(@PathVariable String roomnumber, @ModelAttribute LectureRoom lectureroom,
			Model model) {
		try {
			lectureroom.setRoomnumber(roomnumber);
			lectureRoomService.saveLectureRoom(lectureroom);
			model.addAttribute("successMessage", "강의실 번호를 수정했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("successMessage", "강의실 번호를 수정하지 못 했습니다.");
		}
		return "success";
	}

}