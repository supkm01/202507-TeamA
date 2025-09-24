package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Admin;
import project.com.services.AdminService;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private HttpSession session;

	// ログイン画面へ
	@GetMapping("admin/login")
	public String getAdminLoginPage() {
		return "admin_login.html";
	}

	// ログイン処理
	@PostMapping("/admin/login/process")
	public String adminLoginPage(@RequestParam("adminEmail") String adminEmail,
			@RequestParam("adminPassword") String adminPassword) {
		// loginCheckメソッドを呼び出してその結果をaccountという変数に格納
		Admin admin = adminService.loginCheck(adminEmail, adminPassword);
		// もし、admin==nullログイン画面にとどまります。
		// そうでない場合、sessionにログイン情報に保存
		// ブログHP画面にリダイレクトする/blog/hp
		if(admin == null) {
			return "admin_login.html";
		}else {
			session.setAttribute("loginAdminInfo", admin);
			// 後でsession.getAttribute("loginAdminInfo");
			return "redirect:/admin/lesson/all";
//			return "admim_lesson_lineup.html";
		}

	}

}
