package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Users;
import project.com.services.UsersService;

@Controller
public class UserLoginController {
	@Autowired
	private UsersService usersService;
	// Sessionが使えるように
	private HttpSession session;

	// userログイン画面を表示
	@GetMapping("/user/login")
	public String getUsersLoginPage() {
		return "user_login.html";
	}

	// userログイン処理
	@PostMapping("/user/login/process")
	public String userLonginProcess(@RequestParam String userEmail, @RequestParam String userPassword) {
		// loginCheckメソッドを呼び出してその結果をusersという変数に格納
		Users users = usersService.loginCheck(userEmail, userPassword);
		if (users == null) {
			// もし、users==nullログイン画面にとどまります
			return "user_login.html";
		} else {
			// そうではない場合は、sessionにログイン情報に保存
			// 講座一覧画面にリダイレクトする /lesson/menu user_menu.html
			session.setAttribute("loginUsersInfo", users);
			return "user_menu.html";
		}
	}

	// ログアウト処理 (一覧画面機能が完成したら検証する)
	@GetMapping("/user/logout")
	public String usersLoginout() {
		// セッションの無効か
		session.invalidate();
		return "redirect:/user/login";
	}

}
