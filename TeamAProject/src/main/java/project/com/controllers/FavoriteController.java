package project.com.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.com.model.entity.Users;
import project.com.services.FavoriteService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

	private final FavoriteService favoriteService;
	private final HttpSession session;

	// トグルAPI（AJAX）
	@PostMapping("/toggle")
	@ResponseBody
	public ResponseEntity<?> toggle(@RequestParam Long lessonId) {
		Users user = (Users) session.getAttribute("loginUsersInfo");
		if (user == null) {
			Map<String, Object> res = new HashMap<>();
			res.put("error", "LOGIN_REQUIRED");
			return ResponseEntity.status(401).body(res);
		}
		boolean on = favoriteService.toggle(user.getUserId(), lessonId);
		long count = favoriteService.countByLesson(lessonId);
		Map<String, Object> res = new HashMap<>();
		res.put("favorited", on);
		res.put("count", count);
		return ResponseEntity.ok(res);
	}

	// お気に入り一覧
	@GetMapping
	public String list(Model model) {
		Users user = (Users) session.getAttribute("loginUsersInfo");
		if (user == null)
			return "redirect:/user/login";
		model.addAttribute("favLessons", favoriteService.getFavoriteLessons(user.getUserId()));
		return "favorite_list";
	}
}