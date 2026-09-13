package com.webprogramming.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductForm {

	// "SP01", "SP02",... duoc ProductAdminController tu sinh, khong nhap tay
	private String productId;

	@NotBlank(message = "Ten san pham khong duoc de trong")
	@Size(max = 255, message = "Ten san pham toi da 255 ky tu")
	private String productName;

	@NotNull(message = "Gia san pham khong duoc de trong")
	@DecimalMin(value = "0", inclusive = true, message = "Gia san pham phai >= 0")
	private Double price;

	@NotNull(message = "So luong khong duoc de trong")
	@Min(value = 0, message = "So luong phai >= 0")
	private Integer quantity;

	@Size(max = 4000, message = "Mo ta toi da 4000 ky tu")
	private String description;

	@NotNull(message = "Vui long chon danh muc")
	@Min(value = 1, message = "Vui long chon danh muc")
	private Integer categoryId;

	private Boolean isEdit;
}