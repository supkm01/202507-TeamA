package project.com.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import project.com.model.entity.Users;
import project.com.services.UsersService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserLoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @Test
    @DisplayName("ログイン成功時、/lesson/menu にリダイレクトされる")
    void testUserLoginSuccess() throws Exception {
        Users mockUser = new Users();
        mockUser.setUserId(1L);
        mockUser.setUserEmail("test@test.com");
        mockUser.setUserName("テストユーザー");

        when(usersService.loginCheck(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(post("/user/login/process")
                .param("userEmail", "test@test.com")
                .param("userPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lesson/menu"));
    }

    @Test
    @DisplayName("ログイン失敗時、user_login.html を返す")
    void testUserLoginFail() throws Exception {
        when(usersService.loginCheck(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/user/login/process")
                .param("userEmail", "wrong@example.com")
                .param("userPassword", "badpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_login.html"));
    }

    @Test
    @DisplayName("ログアウト時、セッション無効化し /user/login にリダイレクトされる")
    void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUsersInfo", new Users());

        mockMvc.perform(get("/user/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }
}