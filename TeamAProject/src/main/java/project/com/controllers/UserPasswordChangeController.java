package project.com.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	@GetMapping("/user/password/reset")
	public String getUsersPasswordRestPage() {
		return "user_password_reset.html";
	}

	/* ===== userパスワード変更処理（安全性が低くて、メールアドレスに送信しない処理） ===== */
	// パスワードリセット処理：
	// パスワードを変換したいメールアドレスを記録
	@PostMapping("/user/password/reset/mail")
	public String userPassworChangeProcess(@RequestParam String userEmail, Model model) {
		Users users = usersService.userEmailCheck(userEmail);
		if (users == null) {
			// もし、users==nullパスワードリセット画面にとどまります
			return "user_password_reset.html";
		} else {
			// そうではない場合は、sessionにログイン情報に保存
			// パスワード変更画面にリダイレクトする
			session.setAttribute("passwordChangeUsersInfo", users);
			model.addAttribute("userEmail", users);
			return "user_password_change.html";
		}

	}

	@PostMapping("/user/change/password/complete")
	public String changePassword(@RequestParam("password") String password,
			@RequestParam("passwordConfirm") String passwordConfirm, @RequestParam("userEmail") String userEmail,
			Model model) {
		// 入力したパスワードが一致していることを判断する
		if (!password.equals(passwordConfirm)) {
			// もし 一致しない場合
			// 再びパスワードを更新したいuserEmailを更新画面に渡す
			Users user = new Users();
			user.setUserEmail(userEmail);
			model.addAttribute("userEmail", user);
			return "user_password_change.html";
		} else {
			// 一致する場合
			// パスワードを更新する
			boolean success = usersService.updateUserPassword(userEmail, password, LocalDateTime.now());

			if (!success) {
				// パスワード更新が成功しない場合
				// 再びパスワードを更新したいuserEmailを更新画面に渡す
				Users user = new Users();
				user.setUserEmail(userEmail);
				model.addAttribute("userEmail", user);
				return "user_password_change.html";
			} else {
				return "user_change_password_complete.html";
			}
		}
	}

}
