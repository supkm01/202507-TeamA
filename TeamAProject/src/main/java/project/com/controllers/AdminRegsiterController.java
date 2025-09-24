package project.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.com.dto.AdminRegisterForm;
import project.com.services.AdminService;

@Controller
@RequiredArgsConstructor
public class AdminRegsiterController {
	
	@Autowired
	private AdminService adminService;
	
	// form
    @ModelAttribute("form")
    public AdminRegisterForm setupForm() {
        return new AdminRegisterForm();
    }
	
	// 登録画面の表示
	@GetMapping("/admin/register")
	public String getAdminRegisterPage() {
		return "admin_register.html";
	}
	
	// 確認画面へ(DB保存しない)
    @PostMapping("/admin/register/process")
    public String toConfirm(@Valid @ModelAttribute("form") AdminRegisterForm form,
                            BindingResult br,
                            Model model) {

//         确认密码校验
        if (!form.getAdminPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "Mismatch", "パスワードが一致しません。");
        }

        if (br.hasErrors()) {
            return "admin_register.html"; // 
        }

        return "admin_confirm_register.html"; // 
    }
    
    @PostMapping("/admin/confirm")
    public String registerProcess(@Valid @ModelAttribute("form") AdminRegisterForm form,
                                  BindingResult result,
                                  RedirectAttributes ra) {

        // 保存到DB
    	adminService.register(form);

        return "redirect:/admin/login"; 
    }


}
