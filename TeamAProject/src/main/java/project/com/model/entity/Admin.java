package project.com.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admin", schema = "public")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "admin_password")
    private String adminPassword;

    @Column(name = "delete_flg")
    private Integer deleteFlg;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    public Admin() {
        // 可以初始化默认值
        this.deleteFlg = 0; 
        this.registerDate = LocalDateTime.now(); 
    }
    
	public Admin(String adminName, String adminEmail, String adminPassword) {
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
	}
    
    
}
