//package project.com.controllers;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import jakarta.servlet.http.HttpSession;
//import project.com.model.entity.Users;
//import project.com.services.UserLessonService;
//import project.com.services.UsersService;
//import project.com.model.entity.Lesson;
//
//@Controller
//public class UserCartControllerRan {
//	@Autowired
//	UserLessonService lessonService;
//
//	@Autowired
//	private UsersService usersService;
//
//	@Autowired
//	private HttpSession session;
//
//	@ModelAttribute
//	public void setHeaderFlags(Model model) {
//		// loginしているかしていないかを判断する
//		// sessoinからloginしているデータをもらう
//		Object loginUser = session.getAttribute("loginUsersInfo");
//		boolean loginFlg = (loginUser != null);
//		model.addAttribute("loginFlg", loginFlg);
//		// loginFlgがnullであるかを判断する
//		if (loginFlg) {
//			// もし、nullではない場合、登録している人の情報をmodeに渡す
//			Users u = (Users) loginUser;
//			model.addAttribute("userName", u.getUserName());
//		} else {
//			// nullである場合、nullを渡す
//			model.addAttribute("userName", null);
//		}
//	}
//
//	/* ====userカートに入れた商品を表示======= */
//	// sessionにカートに入れった商品を記録できるために
//	// 偽のカートマップを用意する
//	private Map<Long, Integer> cart() {
//		// sessionにlesson（商品）を記録する準備
//		final String KEY = "userCart";
//		Object obj = session.getAttribute(KEY);
//		// sessionからloginしているUser情報をもらう
//		Users user = (Users) session.getAttribute("loginUsersInfo");
//
//		Map<Users, Map<Long, Integer>> userCartMap;
//
//		if (obj == null) {
//			// もし、カートがない場合、カートを作る
//			userCartMap = new LinkedHashMap<>();
//			// sessionにカートを保存
//			session.setAttribute(KEY, userCartMap);
//
//		} else {
//			// もし、カートがすでに存在している。このカートを利用する
//			userCartMap = (Map<Users, Map<Long, Integer>>) obj;
//		}
//
//		return userCartMap.computeIfAbsent(user, k -> new LinkedHashMap<>());
//	}
//
//	@PostMapping("/lesson/cart/all")
//	public String userAddlesson(@RequestParam Long lessonId, HttpSession session) {
//		Lesson lesson = lessonService.selectByLessonId(lessonId);
//		if (lesson == null) {
//			// もし、このlessonが存在しなかった
//			// lesson一覧画面にリセット
//			return "redirect:/lesson/menu";
//		} else {
//			// もし、このlessonが存在する
//			// カートに一つのlessonを追加する(しかし、HTMLに個数を数えていないから、個数を表示しない)
//			cart().merge(lessonId, 1, Integer::sum);
//			// カート画面にアクセス
//			return "redirect:/lesson/show/cart";
//		}
//	}
//
//	// usercart画面を表示
//	@GetMapping("/lesson/show/cart")
//	public String getUserCart(Model model) {
//
//		Map<Long, Integer> cart = cart();
//		List<Lesson> lessons = cart.keySet().stream().map(lessonService::selectByLessonId).collect(Collectors.toList());
//
//		// お金の合計の計算（購入機能が完成したら確認する！！！！１）
////		    int total = lessons.stream()
////		                       .mapToInt(l -> l.getLessonFee() * cart.get(l.getLessonId()))
////		                       .sum();
//
//		// カート画面でsessionに存在するlessonが表示できるように
//		model.addAttribute("list", lessons);
//
//		// お金の合計の表示（購入機能が完成したら確認する！！！！１）
////		    model.addAttribute("counts", cart);    
////		    model.addAttribute("total", total);   
//		return "user_planned_application.html";
//	}
//
//	/* ====カートに入れた商品を削除======= */
//
//	@GetMapping("/lesson/cart/delete/{lessonId}")
//	public String deleteUserCartLesson(@PathVariable Long lessonId, HttpSession session, RedirectAttributes ra) {
//		Users user = (Users) session.getAttribute("loginUsersInfo");
//		if (user == null) {
//			// もし、Userがログインしていない場合
//			// ログインしてやってみることを勧誘する
//			//HTMLに、このエラーメッセージを表示する場所がないから、表示しない
//			//以下のコードをHTMLに追加すると表示できるようになる
//			//<div th:if="${errorMsg}" class="alert alert-danger" th:text="${errorMsg}"></div>
//			ra.addFlashAttribute("errorMsg", "ログインして、もう一度やってみます。");
//			// この画面にとどまる
//			 return "redirect:/lesson/show/cart";
//		}
//	
//		Map<Users, Map<Long, Integer>> userCartMap = (Map<Users, Map<Long, Integer>>) session.getAttribute("userCart");
//
//		if (userCartMap == null) {
//			// もし、カートが存在しない場合
//			// この画面にとどまる
//			return "redirect:/lesson/show/cart";
//		} else {
//			//もし、カートが存在する場合
//			//ログインしたユーザーのカートを呼び出す
//			Map<Long, Integer> myCart = userCartMap.get(user);
//			if (myCart != null) {
//				// lesson商品を削除
//				myCart.remove(lessonId);
//			}
//		}
//		// 削除が終わったら、カート画面にリダイレクトする
//		return "redirect:/lesson/show/cart";
//
//	}
//
//}
