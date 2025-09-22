package project.com.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.com.model.dao.UsersDao;
import project.com.model.entity.Users;

@Service
public class UsersService {
	@Autowired
	private UsersDao usersDao;

	// ログイン処理
	public Users loginCheck(String userEmail, String userPassword) {
		// もし、users_emailとusers_passwordがfindByUserEmailAndUserPassword
		// を使用して存在しなかった場合＝＝nullの場合
		Users users = usersDao.findByUserEmailAndUserPassword(userEmail, userPassword);
		if (users == null) {
			// その場合は、存在しないnullであることをコントローラクラスに知らせる
			return null;
		} else {
			// そうでない場合ログインしている人に情報をコントローラクラスに知らせる
			return users;
		}
	}
	
	//パスワードを変換したいとき、既存メールアドレスであるかを確認する
	public Users userEmailCheck(String userEmail) {
		// もし、users_emailがfindByUserEmail
		//を使用して存在しなかった場合＝＝nullの場合
		Users users = usersDao.findByUserEmail(userEmail);
		if (users == null) {
			// その場合は、存在しないnullであることをコントローラクラスに知らせる
			return null;
		} else {
			// そうでない場合ログインしている人に情報をコントローラクラスに知らせる
			return users;
		}
		
	}
	
	//パスワードの変更処理
	public boolean updateUserPassword(String userEmail,String userPassword,LocalDateTime registerDate) {
		if(userEmail == null) {
			//もし、入力したメールアドレスが存在しない場合,false	
			return false;
		}else {
			//もし、入力したメールアドレスが存在する場合,true	
			Users users = usersDao.findByUserEmail(userEmail);
			//新しいパスワード、変更時間を保存する
			users.setUserPassword(userPassword);
			users.setRegisterDate(registerDate);
			usersDao.save(users);
			return true;
		}
	}

}
