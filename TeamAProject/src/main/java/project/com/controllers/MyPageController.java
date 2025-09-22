package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import project.com.model.entity.Users;
import project.com.services.TransactionHistoryService;
import project.com.services.impl.MyPageServiceImpl;

@Controller

public class MyPageController {
	@Autowired
	private HttpSession session;

	@Autowired
	private MyPageServiceImpl myPageService;

	@Autowired
	private TransactionHistoryService transactionHistoryService;

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

	@GetMapping("/lesson/mypage")
	public String completePage(Model model) {
		Users user = (Users) session.getAttribute("loginUsersInfo");
		var list = myPageService.getPurchasedItems(user.getUserId());
		model.addAttribute("listSub", list);
		return "mypage";
	}

	@PostMapping("/lesson/history/delete")
	@Transactional
	public String deleteHistory(@RequestParam("transactionId") Long transactionId, RedirectAttributes ra) {
		Users user = (Users) session.getAttribute("loginUsersInfo");
		if (user == null) {
			ra.addFlashAttribute("error", "ログインが必要です。");
			return "redirect:/user/login";
		}

		try {

			transactionHistoryService.deleteHeaderForUser(transactionId, user.getUserId());

			ra.addFlashAttribute("message", "削除しました。");
		} catch (Exception ex) {
			ra.addFlashAttribute("error", "削除に失敗しました。もう一度お試しください。");
		}

		return "redirect:/lesson/mypage";
	}
}
