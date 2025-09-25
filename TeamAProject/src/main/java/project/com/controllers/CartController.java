//package project.com.controllers;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import project.com.model.entity.Lesson;
//import project.com.model.entity.Users;
//import project.com.services.LessonMenuService;
//
//import java.util.*;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/lesson")
//public class CartController {
//
//	@Autowired
//	private HttpSession session;
//
//	@Autowired
//	private LessonMenuService lessonService;
//
//	
//	@ModelAttribute
//    public void setHeaderFlags(Model model) {
//        Object loginUser = session.getAttribute("loginUsersInfo");
//        boolean loginFlg = (loginUser != null);
//
//        model.addAttribute("loginFlg", loginFlg);
//
//        if (loginFlg) {
//            Users u = (Users) loginUser;
//            model.addAttribute("userName", u.getUserName());
//
//        } else {
//            model.addAttribute("userName", null);
//        }
//    }
//	
//
//	@SuppressWarnings("unchecked")
//	private Map<Long, Integer> getCartItems() {
//		Map<Long, Integer> items = (Map<Long, Integer>) session.getAttribute("CART_ITEMS");
//		if (items == null) {
//			items = new LinkedHashMap<>();
//			session.setAttribute("CART_ITEMS", items);
//		}
//		return items;
//	}
//
//	// cart一覧画面
//	@GetMapping("/show/cart")
//	public String showCart(Model model) {
//		Map<Long, Integer> items = getCartItems();
//		List<Lesson> lessons = lessonService.findAllById(items.keySet());
//
//		long total = lessons.stream().mapToLong(l -> (long) l.getLessonFee() * items.getOrDefault(l.getLessonId(), 0))
//				.sum();
//
//		model.addAttribute("list", lessons);
//		model.addAttribute("counts", items);
//		model.addAttribute("total", total);
//		return "user_planned_application";
//	}
//
//	// cartに追加
//	@PostMapping("/cart/all")
//    public String addToCart(@RequestParam("lessonId") Long lessonId,
//                            @RequestParam(value = "qty", defaultValue = "1") int qty,
//                            RedirectAttributes ra) {
//        //登録判断
//        Object login = session.getAttribute("loginUsersInfo");
//        if (login == null) return "redirect:/user/login";
//
//        Map<Long, Integer> items = getCartItems();
//        items.merge(lessonId, Math.max(1, qty), Integer::sum);
//
////        ra.addFlashAttribute("message", "カートに追加しました。");
//
//        return "redirect:/lesson/menu";
//
//    }
//
//
//	// 削除
//	@GetMapping("/cart/delete/{lessonId}")
//	public String delete(@PathVariable Long lessonId, RedirectAttributes ra) {
//		Map<Long, Integer> items = getCartItems();
//		items.remove(lessonId);
////		ra.addFlashAttribute("message", "削除しました。");
//		return "redirect:/lesson/show/cart";
//	}
//}
