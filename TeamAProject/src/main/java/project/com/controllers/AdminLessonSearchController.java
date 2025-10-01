package project.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import project.com.services.AdminLessonService;

@Controller
public class AdminLessonSearchController {

	@Autowired
	private HttpSession session;

	@Autowired
	private AdminLessonService adminLessonService;

	@GetMapping("/Admin/search")
	public String searchLesson(@RequestParam("keyword") String keyword, Model model) {
		// セッションからログインしている人の情報をaccountという変数に格納
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		// ログインしていない場合はログインページにリダイレクト
		if (admin == null) {
			return "redirect:/admin/login";
		}

		// 全部Lesssionの情報を取得
		List<Lesson> LessonList = adminLessonService.searchLessonByKeyword(admin.getAdminId(), keyword);
		
		
		
		if (LessonList == null || LessonList.isEmpty()) {
			// 該当するlessonは見つからなかった場合、メッセージを表示し、リダイレクトする。
			return "redirect:/admin/lesson/all?message=notfound";
		} else {
			model.addAttribute("lessonList", LessonList);
            model.addAttribute("keyword", keyword);
            return "admin_lesson_lineup.html";
		}

	}
}
