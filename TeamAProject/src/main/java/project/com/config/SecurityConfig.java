package project.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				// STATIC資源にaccess可能
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/error").permitAll()
				// User&AdminのRegister＆login access可能
				.requestMatchers("/user/register", "/user/register/process", "/user/confirm", "/user/register/complete",
						"/user/login", "/user/login/process", "/admin/register", "/admin/confirm",
						"/admin/register/process", "/admin/login", "/admin/login/process")
				.permitAll()
				// 他にはaccess不可能
				.anyRequest().authenticated())
				// login画面に戻る
				.formLogin(login -> login.loginPage("/user/login").loginProcessingUrl("/user/login/process")
						.usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/home", true)
						.failureUrl("/user/login?error"))
				.csrf(csrf -> csrf.ignoringRequestMatchers("/user/register/process", "/user/confirm"));
		return http.build();
	}
}