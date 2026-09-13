package com.webprogramming.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

	@Id
	@Column(name = "product_id", length = 10)
	private String productId;

	@Column(name = "product_name", columnDefinition = "NVARCHAR(255) NULL")
	private String productName;

	@Column(name = "price")
	private double price;

	@Column(name = "description", columnDefinition = "NVARCHAR(255) NULL")
	private String description;

	@Column(name = "images", columnDefinition = "NVARCHAR(255) NULL")
	private String images;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "sold", columnDefinition = "INT NOT NULL DEFAULT 0")
	private int sold;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	public double getRevenue() {
		return sold * price;
	}

}