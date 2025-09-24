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
//	//偽のカートマップを用意する
//	private Map<Long, Integer> cart() {
//	    final String KEY = "cart";
//	    Object obj = session.getAttribute(KEY);
//	    if (obj == null) {
//	    	//もし、カートがない場合、カートを作る
//	        Map<Long, Integer> newCart = new LinkedHashMap<>();
//	        //sessionにカートを保存
//	        session.setAttribute(KEY, newCart);
//	        return newCart;
//	    }else {
//	    	//カートがすでに存在している場合、このカートを利用する
//	    	 return (Map<Long, Integer>) obj;
//	    } 
//	}
//	
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
//			//カートに一つのlessonを追加する(しかし、HTMLに個数を数えていないから、個数を表示しない)
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
//		 Map<Long, Integer> cart = cart();
//		 List<Lesson> lessons = cart.keySet()
//                 .stream()
//                 .map(lessonService::selectByLessonId)
//                 .collect(Collectors.toList());
//		 
//			//お金の合計の計算（購入機能が完成したら確認する！！！！１）
////		    int total = lessons.stream()
////		                       .mapToInt(l -> l.getLessonFee() * cart.get(l.getLessonId()))
////		                       .sum();
//
//		    //カート画面でsessionに存在するlessonが表示できるように
//		    model.addAttribute("list", lessons);
//		    
//		    //お金の合計の表示（購入機能が完成したら確認する！！！！１）
////		    model.addAttribute("counts", cart);    
////		    model.addAttribute("total", total);   
//		    return "user_planned_application.html"; 
//		}
//
//}
