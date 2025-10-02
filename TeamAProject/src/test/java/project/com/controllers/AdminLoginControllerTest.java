package project.com.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.servlet.http.HttpSession;
import jdk.jshell.Snippet.Status;
import project.com.model.entity.Admin;
import project.com.services.AdminService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AdminService adminService;

	// 1. ログインページを正しく取得するテスト
	@Test
	public void testGetLoginPage() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/admin/login");
		mockMvc.perform(request).andExpect(view().name("admin_login.html"));
	}

	// 2. 正しいemail & password → ログイン成功 + Session保存 + リダイレクト
	@Test
	public void testAdminLogin_Success() throws Exception {
		Admin mockAdmin = new Admin("wzh", "wzh@123", "123");
		when(adminService.loginCheck("wzh@123", "123")).thenReturn(mockAdmin);

		mockMvc.perform(post("/admin/login/process")
				.param("adminEmail", "wzh@123")
				.param("adminPassword", "123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/lesson/all"))
				.andExpect(request().sessionAttribute("loginAdminInfo", mockAdmin));

	}
	
	// 3. 誤ったemail → ログイン失敗 → admin_login.html + Session未設定
	@Test
	public void testLoginFilure_WrongEamil() throws Exception{
		
		when(adminService.loginCheck("test@123","123"))
		.thenReturn(null);
		
		mockMvc.perform(post("/admin/login/process")
				.param("adminEmail", "test123")
				.param("adminPassword","123"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_login.html"))
				.andExpect(request().sessionAttributeDoesNotExist("loginAdminInfo"));
	}
	
	  // 4. 誤ったpassword → ログイン失敗 → admin_login.html + Session未設定
	@Test
	public void testloginFilure_WrongPassword() throws Exception{
		
		when(adminService.loginCheck("wzh@123", "12345"))
		.thenReturn(null);
		
		mockMvc.perform(post("/admin/login/process")
				.param("adminEmail","wzh@123")
				.param("adminPassword", "12345"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_login.html"))
				.andExpect(request().sessionAttributeDoesNotExist("loginAdminInfo"));
			
	}
	
	
	// 5. emailとpassword間違い → 失敗
	@Test
	public void testloginFilure_WrongPasswordAndEmail() throws Exception{
		
		when(adminService.loginCheck("test@123", "12345"))
		.thenReturn(null);
		
		mockMvc.perform(post("/admin/login/process")
				.param("adminEmail","test@123")
				.param("adminPassword", "12345"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_login.html"))
				.andExpect(request().sessionAttributeDoesNotExist("loginAdminInfo"));
			
	}
	
	// 6. 初期画面表示（入力値が空）
    @Test
    void testLoginWithEmptyInput() throws Exception{
    	when(adminService.loginCheck("", "")).thenReturn(null);
    	
    	mockMvc.perform(post("/admin/login/process")
    			.param("adminEmail","")
				.param("adminPassword", ""))
    	 		.andExpect(status().isOk())
    	 		.andExpect(view().name("admin_login.html"))
    	 		.andExpect(request().sessionAttributeDoesNotExist("loginAdminInfo"));
    }
	
	
	
	
	
	
}
