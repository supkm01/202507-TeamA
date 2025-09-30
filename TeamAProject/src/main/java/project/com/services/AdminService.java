package project.com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.com.dto.AdminRegisterForm;
import project.com.model.dao.AdminDao;
import project.com.model.entity.Admin;

@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;

	
    public void register(AdminRegisterForm form) {
        // 先检查 email 是否已经存在
        if (adminDao.findByAdminEmail(form.getAdminEmail()) != null) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています。");
        }

        Admin admin = new Admin(
                form.getAdminName(),
                form.getAdminEmail(),
                form.getAdminPassword()
        );

        adminDao.save(admin); // INSERT
    }

	// ログインチェック用のメソッド メソッド名「loginCheck」
	// もし、「emailとpasswordの組み合わせ:findByAccountEmailAndPassword」が存在していない場合、
	// その場合、存在しないnullであることをコントローラクラスに知らせる
	public Admin loginCheck(String adminEmail, String adminPassword) {
		Admin account = adminDao.findByAdminEmailAndAdminPassword(adminEmail, adminPassword);
		if (account == null) {
			return null;
		} else {
			return account;
		}
	}
	
	//email確認
	public boolean existsByEmail(String email) {
	    return adminDao.existsByAdminEmail(email);
	}

}