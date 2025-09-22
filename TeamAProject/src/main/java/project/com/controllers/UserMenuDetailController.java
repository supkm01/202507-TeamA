package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import project.com.model.entity.Lesson;
import project.com.model.entity.Users;
import project.com.services.UserLessonService;

@Controller
public class UserMenuDetailController {
@Autowired
UserLessonService lessonService;

@Autowired
private HttpSession session;

@ModelAttribute
public void setHeaderFlags(Model model) {
	
	//loginしているかしていないかを判断する
	//sessoinからloginしているデータをもらう
    Object loginUser = session.getAttribute("loginUsersInfo");
    boolean loginFlg = (loginUser != null);
    model.addAttribute("loginFlg", loginFlg);
    //loginFlgがnullであるかを判断する
    if (loginFlg) {
    	//もし、nullではない場合、登録している人の情報をmodeに渡す
        Users u = (Users) loginUser;
        model.addAttribute("userName", u.getUserName());
    } else {
    	//nullである場合、nullを渡す
        model.addAttribute("userName", null);
    }
}
	
//商品詳細画面の獲得
 @GetMapping("/lesson/detail/{lessonId}")
	public String getLessonMenuDetailPage(@PathVariable Long lessonId, Model model
//			, HttpSession session
			) {
//	 //loginしているかしていないかを判断する
//	    boolean loginFlg = session.getAttribute("loginUsersInfo") != null; 
//	    model.addAttribute("loginFlg", loginFlg);	 
	    
	//lessonのテーブルから、lessonIdで探して、該当するlessonの情報を張り出す
	Lesson lesson = lessonService.selectByLessonId(lessonId);
	
	//lessonの情報をmodelにセットし
	model.addAttribute("lesson", lesson);
	
	//ueser_lesson_detail.htmlから参照可能にする。
	return "user_lesson_detail.html";	
 }

}
