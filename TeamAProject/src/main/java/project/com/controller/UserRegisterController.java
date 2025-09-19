package project.com.controller;

import lombok.RequiredArgsConstructor;
import project.com.dto.UserRegisterForm;
import project.com.service.UserRegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserRegisterController {

	@Autowired
	private UserRegisterService userRegisterService;

	// form
	@ModelAttribute("form")
	public UserRegisterForm setupForm() {
		return new UserRegisterForm();
	}

	// (16) ユーザー登録画面 GET /user/register
	@GetMapping("/user/register")
	public String showRegisterForm(@ModelAttribute("form") UserRegisterForm form) {
		return "user_register";
	}

	// (17) ユーザー登録確認機能 POST /user/register/process
	@PostMapping("/user/register/process")
	public String toConfirm(@Valid @ModelAttribute("form") UserRegisterForm form, BindingResult br, Model model) {

		if (!form.getPassword().equals(form.getUserPassword())) {
			br.rejectValue("userPassword", "Mismatch", "パスワードが一致しません。");
		}

		return "user_confirm_register";
	}

	// (18) ユーザー登録機能 POST /user/confirm
	@PostMapping("/user/confirm")
	public String registerProcess(@Valid @ModelAttribute("form") UserRegisterForm form, BindingResult result,
			RedirectAttributes ra) {

		userRegisterService.register(form);

		return "user_login";
	}

}
