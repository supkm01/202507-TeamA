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

    @GetMapping("/lesson/menu")
    public String showMenu(Model model) {
    	model.addAttribute("lessonList", lessonService.listAll());
        return "user_menu";
    }
}
