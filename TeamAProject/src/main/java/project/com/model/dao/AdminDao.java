package project.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.com.model.entity.Admin;

@Repository// 
public interface AdminDao extends JpaRepository<Admin, Long> {
	
	// SELECT * FROM account WHERE admin_email = ?
	// 用途：管理者の登録処理をするときに、同じメールアドレスがあったら 登録処理させないようにする
	// 1行だけしかレコードは所得できない
	Admin findByAdminEmail(String adminEmail);
	
	
	// SELECT * FROM account WHERE admin_email = ? AND admin_password = ?
	// 用途：ログイン処理に使用。入力したメールアドレスとパスワードが一致してるときだけ データを取る
	Admin findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);
	
	//用途：email確認用
	boolean existsByAdminEmail(String adminEmail);
}
