package project.com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
	public class AdminRegisterForm {
	    @NotBlank
	    private String adminName;

	    @NotBlank
	    private String adminEmail;

	    @NotBlank
	    private String adminPassword;

	    
	    @NotBlank
	    private String confirmPassword;
	    
	    
	}

