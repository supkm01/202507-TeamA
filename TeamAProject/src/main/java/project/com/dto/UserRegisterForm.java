package project.com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterForm {

	@NotBlank(message = "氏名は必須です。")
	private String userName;

	@NotBlank(message = "メールアドレスは必須です。")
	@Email(message = "正しいメールアドレスを入力してください。")
	private String userEmail;

	// <input name="password">
	@NotBlank(message = "パスワードは必須です。")
	@Size(min = 8, message = "パスワードは8文字以上です。")
	private String password;

	// <input name="userPassword">
	@NotBlank(message = "確認用パスワードは必須です。")
	private String userPassword;

}
