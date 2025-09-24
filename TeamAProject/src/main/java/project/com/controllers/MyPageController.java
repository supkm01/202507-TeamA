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

	// マイページ表示処理
	@GetMapping("/lesson/mypage")
	public String completePage(Model model) {
		// セッションからログインユーザを取得
		Users user = (Users) session.getAttribute("loginUsersInfo");
		// 購入済みアイテム一覧を取得
		var list = myPageService.getPurchasedItems(user.getUserId());
		// Viewへデータを渡す
		model.addAttribute("listSub", list);
		return "mypage";
	}

	// 購入履歴削除処理
	@PostMapping("/lesson/history/delete")
	@Transactional
	public String deleteHistory(@RequestParam("transactionId") Long transactionId, RedirectAttributes ra) {
		// セッションからログインユーザを取得
		Users user = (Users) session.getAttribute("loginUsersInfo");
		if (user == null) {
			// 未ログインの場合、ログイン画面へリダイレクト
			ra.addFlashAttribute("error", "ログインが必要です。");
			return "redirect:/user/login";
		}

		try {
			// 該当ユーザの取引履歴ヘッダを削除
			transactionHistoryService.deleteHeaderForUser(transactionId, user.getUserId());
			ra.addFlashAttribute("message", "削除しました。");
		} catch (Exception ex) {
			// エラー発生時はメッセージを表示
			ra.addFlashAttribute("error", "削除に失敗しました。もう一度お試しください。");
		}
		// マイページへリダイレクト
		return "redirect:/lesson/mypage";
	}
}
