package project.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import project.com.services.AdminLessonService;

@Controller
public class AdminHPController {;


	@Autowired
	private HttpSession session;

	@Autowired
	private AdminLessonService adminLessonService;

	// ブログHP画面に表示
	@GetMapping("/admin/lesson/all")
	public String getLessonHP(Model model) {
		// セッションからログインしている人の情報を取得
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		// もし、admin==null ログイン画面にリダイレクト
		// そうでない場合、ログインしている一人の名前の情報を画面に渡す
		// LessonHP画面のhtmlを表示.
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			// 全部Lesssionの情報を取得
			List<Lesson> LessonList =  adminLessonService.selectAllLesson(admin.getAdminId());
			model.addAttribute("lessonList", LessonList);
		}
		return "admin_lesson_lineup.html";
	}

}
