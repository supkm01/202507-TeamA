package project.com.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import project.com.services.AdminService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AdminService adminService;

	// 1 表示テスト
	@Test
	public void testGetAdminRegisterPage() throws Exception {
		mockMvc.perform(get("/admin/register")).andExpect(status().isOk())
				.andExpect(view().name("admin_register.html"));
	}

	// 2 POST /admin/register/process → 確認画面へ
	@Test
	void testRegisterProcessSuccess() throws Exception {
		when(adminService.existsByEmail("bob@123.com")).thenReturn(false);

		mockMvc.perform(post("/admin/register/process")
				.param("adminName", "bob")
				.param("adminEmail", "bob@123.com")
				.param("adminPassword", "1234")
				.param("confirmPassword", "1234"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_confirm_register.html"));
	}

	// 3 POST /admin/register/process → パスワードが一致しない
	@Test
	void testRegisterProcessPasswordMismatch() throws Exception {
		mockMvc.perform(post("/admin/register/process")
				.param("adminEmail", "bob@123")
				.param("adminPassword", "1234")
				.param("confirmPassword", "5678"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin_register.html"))
				.andExpect(model().attributeHasFieldErrors("form", "confirmPassword"));
	}
	
	 // 4 POST /admin/register/process → 
	@Test
	void testRegisterProcessDuplicateEmail() throws Exception {
		  when(adminService.existsByEmail("wzh@123")).thenReturn(true);
		  
		  mockMvc.perform(post("/admin/register/process")
	                .param("adminEmail", "wzh@123")
	                .param("adminPassword", "1234")
	                .param("confirmPassword", "1234"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("admin_register.html"))
	                .andExpect(model().attributeHasFieldErrors("form", "adminEmail"));
		  
	}
	
	 // 5. POST /admin/confirm → DB保存 + リダイレクト
    @Test
    void testConfirmRegister() throws Exception {
        mockMvc.perform(post("/admin/confirm")
                .param("adminEmail", "test@example.com")
                .param("adminPassword", "1234")
                .param("confirmPassword", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));

        // 确认 service.register() 被调用
        verify(adminService).register(any());
    }
	
	
}
