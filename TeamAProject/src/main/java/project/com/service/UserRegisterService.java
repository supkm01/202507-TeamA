package project.com.service;

import lombok.RequiredArgsConstructor;
import project.com.dto.UserRegisterForm;
import project.com.model.dao.UsersRepository;
import project.com.model.entity.Users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

	private final UsersRepository usersRepository;
	// 安全性を考えBCryptPasswordEncoderを使います
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public boolean emailExists(String email) {
		return usersRepository.existsByUserEmail(email);
	}

	@Transactional
	public Users register(UserRegisterForm form) {
		Users u = new Users();
		u.setUserName(form.getUserName());
		u.setUserEmail(form.getUserEmail());
		u.setUserPassword(passwordEncoder.encode(form.getPassword()));
		u.setDeleteFlg(0);
		u.setRegisterDate(LocalDateTime.now());
		return usersRepository.save(u);
	}
}
