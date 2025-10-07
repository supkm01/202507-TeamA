package project.com.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import project.com.dto.UserRegisterForm;
import project.com.services.UserRegisterService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegisterController.class)
class UserRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRegisterService userRegisterService;

    @Test
    void showRegisterForm() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_register"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void toConfirm_passwordMismatch() throws Exception {
        mockMvc.perform(post("/user/register/process")
                        .param("userName", "test")
                        .param("userEmail", "test@test.com")
                        .param("password", "abc12345")
                        .param("userPassword", "DIFF-abc12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_confirm_register"))

                .andExpect(model().attributeHasFieldErrors("form", "userPassword"));
    }

    @Test
    void toConfirm_passwordMatch() throws Exception {
        mockMvc.perform(post("/user/register/process")
                        .param("userName", "test")
                        .param("userEmail", "test@test.com")
                        .param("password", "abc12345")
                        .param("userPassword", "abc12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_confirm_register"))
                .andExpect(model().attributeHasNoErrors("form"));
    }

    @Test
    void registerProcess_callsServiceAndReturnLoginView() throws Exception {
        ArgumentCaptor<UserRegisterForm> captor = ArgumentCaptor.forClass(UserRegisterForm.class);
        doAnswer(invocation -> null).when(userRegisterService).register(any(UserRegisterForm.class));

        mockMvc.perform(post("/user/confirm")
                .param("userName", "test")
                .param("userEmail", "test@test.com")
                .param("password", "abc12345")
                .param("userPassword", "abc12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_login"));

        verify(userRegisterService, times(1)).register(captor.capture());
        UserRegisterForm saved = captor.getValue();
        assertThat(saved.getUserName()).isEqualTo("test");
        assertThat(saved.getUserEmail()).isEqualTo("test@test.com");
        assertThat(saved.getPassword()).isEqualTo("abc12345");
        assertThat(saved.getUserPassword()).isEqualTo("abc12345");
    }
}
