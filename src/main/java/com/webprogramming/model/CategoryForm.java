package com.webprogramming.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryForm {

	private int categoryId;

	@NotBlank(message = "Ten danh muc khong duoc de trong")
	@Size(max = 255, message = "Ten danh muc toi da 255 ky tu")
	private String categoryName;

	@NotNull(message = "Vui long chon trang thai")
	private Integer status;

	private Boolean isEdit;
}