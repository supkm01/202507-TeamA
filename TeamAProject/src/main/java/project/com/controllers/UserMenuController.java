package project.com.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Lesson;
import project.com.model.entity.Users;
import project.com.services.FavoriteService; // ★ 追加
import project.com.services.LessonMenuService;
import project.com.services.UserRegisterService;
import project.com.services.impl.LessonMenuServiceImpl;

@Controller
public class UserMenuController {

	@Autowired
	private UserRegisterService userRegisterService;

	@Autowired
	private LessonMenuService lessonService;

	@Autowired
	private LessonMenuServiceImpl lessonMenuServiceimpl;

	@Autowired
	private FavoriteService favoriteService; // ★ 追加

	@Autowired
	private HttpSession session;

	// 共通的にModelへログイン情報をセットする
	@ModelAttribute
	public void setHeaderFlags(Model model) {
		Object loginUser = session.getAttribute("loginUsersInfo");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);

		if (loginFlg) {
			Users u = (Users) loginUser;
			model.addAttribute("userName", u.getUserName());
		} else {
			model.addAttribute("userName", null);
		}
	}

	// レッスンメニュー画面表示処理（開催予定一覧）
	@GetMapping("/lesson/menu")
	public String showMenu(Model model) {
		List<Lesson> lessonList = lessonService.listUpcoming();
		model.addAttribute("lessonList", lessonList);

		Users user = (Users) session.getAttribute("loginUsersInfo");
		enrichFavorites(model, lessonList, user);

		return "user_menu";
	}

	// キーワード検索付き一覧
	@GetMapping("/lesson")
	public String list(@RequestParam(value = "q", required = false) String q, Model model) {
		List<Lesson> lessonList = (q == null || q.isBlank()) ? lessonService.listUpcoming()
				: lessonMenuServiceimpl.searchByKeyword(q);

		model.addAttribute("lessonList", lessonList);

		Users user = (Users) session.getAttribute("loginUsersInfo");
		enrichFavorites(model, lessonList, user);

		model.addAttribute("q", q);
		return "user_menu";
	}

	private void enrichFavorites(Model model, List<Lesson> lessons, Users user) {
		Map<Long, Boolean> favoriteMap = new HashMap<>();
		Map<Long, Long> favoriteCounts = new HashMap<>();

		if (lessons == null || lessons.isEmpty()) {
			model.addAttribute("favoriteMap", favoriteMap);
			model.addAttribute("favoriteCounts", favoriteCounts);
			return;
		}

		for (Lesson l : lessons) {
			if (l == null || l.getLessonId() == null)
				continue;
			Long lessonId = l.getLessonId();

			// 1) 各講座のお気に入り数
			favoriteCounts.put(lessonId, favoriteService.countByLesson(lessonId));

			// 2) ログイン中のみ「自分が付けているか」
			if (user != null && user.getUserId() != null) {
				boolean favored = favoriteService.isFavorited(user.getUserId(), lessonId);
				favoriteMap.put(lessonId, favored);
			}
		}

		model.addAttribute("favoriteMap", favoriteMap);
		model.addAttribute("favoriteCounts", favoriteCounts);
	}
}