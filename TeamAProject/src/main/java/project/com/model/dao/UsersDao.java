package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.com.model.entity.Users;



@Repository
public interface UsersDao extends JpaRepository<Users, Long> {
	//保存処理と更新処理
	Users save(Users users);
	
	//登録済みメールアドレスの存在を確認するため
	//SELECT * FROM Users WHERE users_email = ?
	Users findByUserEmail(String userEmail);
	
	//userログインする時の検証（メールアドレスとパスワード）
	//SELECT * FROM Users WHERE users_email = ? AND users_password
	Users findByUserEmailAndUserPassword(String userEmail, String userPassword);

}
