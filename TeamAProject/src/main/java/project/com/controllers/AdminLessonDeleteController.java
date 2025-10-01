package project.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import project.com.services.AdminLessonService;

@Controller
@RequestMapping("/admin/lesson")
@RequiredArgsConstructor
public class AdminLessonDeleteController {
	private final AdminLessonService adminLessonService;
	@Autowired
	private HttpSession session;

	// 講座削除フォーム表示
	@GetMapping("/delete")
	public String showDeleteForm( Model model) {
		// sessionから管理者の情報をもらう
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			List<Lesson> lessonList = adminLessonService.findAll(); 
		    model.addAttribute("lessonList", lessonList);
		 // 管理者の情報をｈｔｍｌで表示する用
	        model.addAttribute("loginAdmin", admin);
		    return "admin_delete_lesson";

		}

	}
	
	// 講座削除
	@PostMapping("/delete/remove")
	public String lessonDelete(Long lessonId, Model model) {
		// セッションからログインしている人の情報をaccountという変数に格納
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			if(adminLessonService.deleteLesson(lessonId)) {
				//後の操作　Listが必要
				List<Lesson> lessonList = adminLessonService.findAll(); 
				model.addAttribute("lessonList", lessonList);
				//trueだったら、講座変更完了画面を表示する
				return "admin_fix_delete.html";
			}else {
				//falseだったら、講座削除フォームをも一度表示する
				return "admin_delete_lesson";
			}
		}
			
	}
	
	
}
