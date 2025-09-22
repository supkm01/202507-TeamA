package project.com.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Users;
import project.com.services.UsersService;

@Controller
public class UserPasswordChangeController {
	@Autowired
	private UsersService usersService;

	@Autowired
	private HttpSession session;

	// userパスワードリセット画面を表示
	@GetMapping("/user/password/rest")
	public String getUsersPasswordRestPage() {
		return "user_password_reset.html";
	}

	
/*===== userパスワード変更処理（安全性が低くて、メールアドレスに送信しない処理） =====*/
	//パスワードリセット処理：
	//パスワードを変換したいメールアドレスを記録
	@PostMapping("/user/password/reset/mail")
	public String userPassworChangeProcess(@RequestParam String userEmail,Model model) {
		Users users = usersService.userEmailCheck(userEmail);
		if (users == null) {
			// もし、users==nullパスワードリセット画面にとどまります
			return "user_password_reset.html";
		} else {
			// そうではない場合は、sessionにログイン情報に保存
			// パスワード変更画面にリダイレクトする 
			session.setAttribute("passwordChangeUsersInfo", users);
			model.addAttribute("userEmail", users);
			return "user_password_change";
		}
		
	}
	


	//userパスワード変更画面を表示
//	@GetMapping("/user/password/change/{userId}")
//	public String getUsersPasswordChangePage(@PathVariable String userEmail,Model model) {
//		//sessonからパスワードを変換したいメールアドレスを取得
//		
//				
//	}
	
}
