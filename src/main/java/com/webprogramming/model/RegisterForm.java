package com.webprogramming.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterForm {
 
	@NotBlank(message = "Ho ten khong duoc de trong")
	@Size(min = 2, max = 255, message = "Ho ten phai co tu 2 den 255 ky tu")
	private String fullname;
 
	@NotBlank(message = "Email khong duoc de trong")
	@Email(message = "Email khong dung dinh dang")
	private String email;
 
	@NotBlank(message = "Mat khau khong duoc de trong")
	@Size(min = 6, max = 255, message = "Mat khau phai co it nhat 6 ky tu")
	private String password;
 
	@NotBlank(message = "Vui long nhap lai mat khau")
	private String confirmPassword;
}