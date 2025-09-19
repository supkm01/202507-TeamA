package project.com.services;

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

}
