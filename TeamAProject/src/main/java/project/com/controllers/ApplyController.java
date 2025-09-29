package project.com.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.com.model.entity.Lesson;
import project.com.model.entity.Users;
import project.com.services.LessonMenuService;
import project.com.services.TransactionHistoryService;
import project.com.services.impl.TransactionItemServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApplyController {

	@Autowired
	private HttpSession session;

	@Autowired
	private LessonMenuService lessonService;

	@Autowired
	private TransactionHistoryService transactionHistoryService;

	@Autowired
	private TransactionItemServiceImpl transactionItemService;

	@SuppressWarnings("unchecked")
	private Map<Long, Integer> getCartItems() {
		Map<Long, Integer> items = (Map<Long, Integer>) session.getAttribute("CART_ITEMS");
		if (items == null) {
			items = new LinkedHashMap<>();
			session.setAttribute("CART_ITEMS", items);
		}
		return items;
	}

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

	// 支払い方法選択
	@GetMapping("/lesson/request")
	public String selectPayment(Model model, @RequestParam(value = "msg", required = false) String msg) {
		Map<Long, Integer> items = getCartItems();
		if (items.isEmpty()) {
			model.addAttribute("list", List.of());
			model.addAttribute("error", "カートに商品がありません。");
			return "user_apply_select_payment";
		}
		List<Lesson> lessons = lessonService.findAllById(items.keySet());
		model.addAttribute("list", lessons);
		if (msg != null)
			model.addAttribute("message", msg);
		return "user_apply_select_payment";
	}

	// 確認画面
	@PostMapping("/lesson/confirm")
	public String postConfirm(@RequestParam(value = "payment", required = false) Integer payment,
	                          Model model,
	                          RedirectAttributes ra) {
	    // ログインチェック
	    Users user = (Users) session.getAttribute("loginUsersInfo");
	    if (user == null) {
	        ra.addFlashAttribute("error", "ログインが必要です。");
	        return "redirect:/user/login";
	    }

	    // カートチェック
	    Map<Long, Integer> items = getCartItems();
	    if (items == null || items.isEmpty()) {
	        ra.addFlashAttribute("error", "カートに商品がありません。");
	        return "redirect:/lesson/request";
	    }

	    // 支払い方法チェック
	    if (payment == null) {
	        ra.addFlashAttribute("error", "お支払い方法を選択してください。");
	        return "redirect:/lesson/request";
	    }

	    // 支払い方法をセッションに格納
	    session.setAttribute("APPLY_PAYMENT", payment);

	    // 表示用データ
	    List<Lesson> lessons = lessonService.findAllById(items.keySet());
	    long total = lessons.stream()
	            .mapToLong(l -> (long) l.getLessonFee() * Math.max(1, items.getOrDefault(l.getLessonId(), 1)))
	            .sum();

	    String payMethod = switch (payment) {
	        case 0 -> "当日現金支払い";
	        case 1 -> "事前銀行振込";
	        case 2 -> "クレジットカード決済";
	        default -> "未選択";
	    };
	    boolean payFlg = (payment == 2); // true=カード用UI, false=非カード用UI

	    // 非クレカ入庫
//	    boolean persisted = false;
//	    if (!payFlg) {
//	        try {
//	            for (Map.Entry<Long, Integer> e : items.entrySet()) {
//	                Long lessonId = e.getKey();
//	                int qty = Math.max(1, e.getValue());
//	                for (int i = 0; i < qty; i++) {
//	                    Long txId = transactionHistoryService.record(user, lessonId); // amount=lesson_fee
//	                    transactionItemService.insertItem(txId, lessonId);            // 明細1行
//	                }
//	            }
//	            persisted = true;
//	            // 入庫済みなのでカート等はクリア
//	            session.removeAttribute("CART_ITEMS");
//	            session.removeAttribute("APPLY_PAYMENT");
//	        } catch (Exception ex) {
//	            ra.addFlashAttribute("error", "処理に失敗しました。もう一度お試しください。");
//	            return "redirect:/lesson/request";
//	        }
//	    }

	    // 画面用属性
	    model.addAttribute("list", lessons);
	    model.addAttribute("total", total);        // 表示用（DB合計ではない）
	    model.addAttribute("payment", payment);
	    model.addAttribute("payMethod", payMethod);
	    model.addAttribute("payFlg", payFlg);      // true: クレカ用画面, false: 非クレカ用画面
//	    model.addAttribute("persisted", persisted);// 非クレカで既に入庫済みなら true	  
	    model.addAttribute("persisted", false); //上の一行目を変換	    
	    model.addAttribute("amount", total);

	    // 確認ページを返す
	    return "user_confirm_apply_detail";
	}

//	@PostMapping("/lesson/pay")
//	public String pay(RedirectAttributes ra) {
//		Users user = (Users) session.getAttribute("loginUsersInfo");
//		if (user == null) {
//			ra.addFlashAttribute("error", "ログインが必要です。");
//			return "redirect:/user/login";
//		}
//
//		Map<Long, Integer> items = getCartItems(); 
//		if (items == null || items.isEmpty()) {
//			ra.addFlashAttribute("error", "カートに商品がありません。");
//			return "redirect:/lesson/request";
//		}
//
//		Integer payment = (Integer) session.getAttribute("APPLY_PAYMENT");
//		if (payment == null || payment != 2) {
//			ra.addFlashAttribute("error", "お支払い方法が不正です。");
//			return "redirect:/lesson/request";
//		}
//
//		// lesson別に分けて、データを入力
//		for (Map.Entry<Long, Integer> e : items.entrySet()) {
//			Long lessonId = e.getKey();
//			int qty = Math.max(1, e.getValue());
//			for (int i = 0; i < qty; i++) {
//				Long txId = transactionHistoryService.record(user, lessonId);
//				transactionItemService.insertItem(txId, lessonId);
//			}
//		}
//
//		// 入庫後、カート系セッションを掃除
//		session.removeAttribute("CART_ITEMS");
//		session.removeAttribute("APPLY_PAYMENT");
//
//		ra.addFlashAttribute("message", "お支払いが完了しました。");
//		return "redirect:/lesson/complete";
//	}
	
	//新たな支払い画面
	@PostMapping("/lesson/pay")
	public String pay(RedirectAttributes ra) {
	    Users user = (Users) session.getAttribute("loginUsersInfo");
	    if (user == null) {
	        ra.addFlashAttribute("error", "ログインが必要です。");
	        return "redirect:/user/login";
	    }

	    Map<Long, Integer> items = getCartItems();
	    if (items == null || items.isEmpty()) {
	        ra.addFlashAttribute("error", "カートに商品がありません。");
	        return "redirect:/lesson/request";
	    }

	    Integer payment = (Integer) session.getAttribute("APPLY_PAYMENT");
	    if (payment == null) {
	        ra.addFlashAttribute("error", "お支払い方法が不正です。");
	        return "redirect:/lesson/request";
	    }

	    /* 全支払い方法共通で落库 */
	    for (Map.Entry<Long, Integer> e : items.entrySet()) {
	        Long lessonId = e.getKey();
	        int qty = Math.max(1, e.getValue());
	        for (int i = 0; i < qty; i++) {
	            Long txId = transactionHistoryService.record(user, lessonId);
	            transactionItemService.insertItem(txId, lessonId);
	        }
	    }

	    /* セッションの掃除 */
	    session.removeAttribute("CART_ITEMS");
	    session.removeAttribute("APPLY_PAYMENT");

	    ra.addFlashAttribute("message", "お申込みが完了しました。");
	    return "redirect:/lesson/complete";
	}
	
	
	

	@GetMapping("/lesson/complete")
	public String completePage(Model model) {

		return "user_apply_complete";
	}
}
