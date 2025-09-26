//package project.com.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import jakarta.servlet.http.HttpSession;
//import project.com.model.entity.TransactionItem;
//import project.com.model.entity.Users;
//import project.com.services.TransactionHistoryService;
//import project.com.services.impl.MyPageServiceImpl;
//
//@Controller
//public class MypageControllerRan {
//	@Autowired
//	private HttpSession session;
//
//	@Autowired
//	private MyPageServiceImpl myPageService;
//
//	@Autowired
//	private TransactionHistoryService transactionHistoryService;
//
//	// 共通的にModelへログイン情報をセットする
//	@ModelAttribute
//	public void setHeaderFlags(Model model) {
//		// セッションからログインユーザを取得
//		Object loginUser = session.getAttribute("loginUsersInfo");
//		// ログイン状態フラグ（ユーザがnullでなければtrue）
//		boolean loginFlg = (loginUser != null);
//
//		// View側で参照できるようにModelへ格納
//		model.addAttribute("loginFlg", loginFlg);
//
//		if (loginFlg) {
//			// ログイン済みの場合、ユーザ名も渡す
//			Users u = (Users) loginUser;
//			model.addAttribute("userName", u.getUserName());
//
//		} else {
//			// 未ログインの場合、ユーザ名はnull
//			model.addAttribute("userName", null);
//		}
//	}
//
//	// マイページ表示処理
//	@GetMapping("/lesson/mypage")
//	public String completePage(Model model) {
//		// セッションからログインユーザを取得
//		Users user = (Users) session.getAttribute("loginUsersInfo");
//		// userIdを用いて購入済みアイテムリストのリストを作成
//		List<TransactionItem> list = myPageService.getPurchasedItems(user.getUserId());
//		// modelにデータを入れる
//		model.addAttribute("listSub", list);
//		// マイページ画面で表示する
//		return "mypage.html";
//	}
//
//	// 購入履歴削除処理
//	@PostMapping("/lesson/history/delete")
//	@Transactional
//	public String deleteHistory(@RequestParam("transactionId") Long transactionId) {
//		// セッションからログインユーザを取得
//		Users user = (Users) session.getAttribute("loginUsersInfo");
//		if (user == null) {
//			// もし、ログインしていない場合
//			// ログイン画面にリダイレクトする
//			return "redirect:/user/login";
//		}
//		try {
//			// 購入履歴を削除する
//			transactionHistoryService.deleteHeaderForUser(transactionId, user.getUserId());
//		} catch (Exception ex) {
//		}
//		// マイページにリダイレクトする
//		return "redirect:/lesson/mypage";
//	}
//
//}
