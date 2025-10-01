package project.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import project.com.model.entity.Admin;
import project.com.model.entity.Lesson;
import project.com.services.AdminLessonService;

@Controller
@RequestMapping("/admin/lesson")
@RequiredArgsConstructor
public class AdminLessonRegisterController {

	private final AdminLessonService adminLessonService;
	private final HttpSession session;

	// 登録リストの表示
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("lesson", new Lesson());
		// sessionから管理者の情報をもらう
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		// 管理者の情報をｈｔｍｌで表示する用
        model.addAttribute("loginAdmin", admin);
		
		return "admin_register_lesson.html";
	}

	//
	@PostMapping("/register/create")
	public String createLesson(@ModelAttribute Lesson lesson,
			@RequestParam("imageFile") MultipartFile file)
			throws IOException {

		// sessionから管理者の情報をもらう
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		lesson.setAdminId(admin.getAdminId());

		// fileの保存
		if (!file.isEmpty() && file != null) {
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ file.getOriginalFilename();
			try {
				Files.copy(file.getInputStream(), 
						Path.of("src/main/resources/static/lesson-image/" + fileName));
			}catch (IOException e) {
				e.printStackTrace();
			}
		
		//file名前の保存
		lesson.setImageName(fileName);
			
		}else {
			return "admin_register_lesson.html";
		}
		// 時間の保存
		lesson.setRegisterDate(LocalDateTime.now());
		// DBに保存する
		adminLessonService.saveLesson(lesson);
		//成功したら、前のページに戻る
		return "admin_fix_register.html"; // 保存成功后跳转课程列表

	}

}
