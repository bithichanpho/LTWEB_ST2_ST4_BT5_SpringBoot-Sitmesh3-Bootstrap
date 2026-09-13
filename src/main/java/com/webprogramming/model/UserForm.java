package com.webprogramming.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {

	private int userId;

	@NotBlank(message = "Ho ten khong duoc de trong")
	@Size(max = 255, message = "Ho ten toi da 255 ky tu")
	private String fullname;

	@NotBlank(message = "Email khong duoc de trong")
	@Email(message = "Email khong dung dinh dang")
	private String email;

	@Size(min = 0, max = 255)
	private String password;

	private String phone;

	@NotBlank(message = "Vui long chon vai tro")
	@Pattern(regexp = "admin|user", message = "Vai tro khong hop le")
	private String role;

	@NotNull(message = "Vui long chon trang thai")
	private Integer status;

	private Boolean isEdit;
}