package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Users;
import project.com.services.LessonMenuService;
import project.com.services.UserRegisterService;

@Controller
public class UserMenuController {

	@Autowired
	private UserRegisterService userRegisterService;
	
	@Autowired
	private LessonMenuService lessonService;

	@Autowired
	private HttpSession session;
	
	// 共通的にModelへログイン情報をセットする
		@ModelAttribute
		public void setHeaderFlags(Model model) {
			// セッションからログインユーザを取得
			Object loginUser = session.getAttribute("loginUsersInfo");
			// ログイン状態フラグ（ユーザがnullでなければtrue）
			boolean loginFlg = (loginUser != null);

			// View側で参照できるようにModelへ格納
			model.addAttribute("loginFlg", loginFlg);

			if (loginFlg) {
				// ログイン済みの場合、ユーザ名も渡す
				Users u = (Users) loginUser;
				model.addAttribute("userName", u.getUserName());

			} else {
				// 未ログインの場合、ユーザ名はnull
				model.addAttribute("userName", null);
			}
		}
	
	// レッスンメニュー画面表示処理
    @GetMapping("/lesson/menu")
    public String showMenu(Model model) {
    	// 全レッスン一覧をサービスから取得し、Modelに格納
    	model.addAttribute("lessonList", lessonService.listAll());
		// user_menu.html を表示
        return "user_menu";
    }
}
