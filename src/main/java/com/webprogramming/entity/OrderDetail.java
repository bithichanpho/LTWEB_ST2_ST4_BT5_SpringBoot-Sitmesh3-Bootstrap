package com.webprogramming.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_detail_id")
	private int orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	// Luu lai ten san pham tai thoi diem dat hang (san pham co the bi doi ten/xoa sau nay)
	@Column(name = "product_name", columnDefinition = "NVARCHAR(255) NULL")
	private String productName;

	// Luu lai don gia tai thoi diem dat hang (gia san pham co the thay doi sau nay)
	@Column(name = "price")
	private double price;

	@Column(name = "quantity")
	private int quantity;

	// Thanh tien cua dong chi tiet nay = don gia * so luong
	public double getSubtotal() {
		return price * quantity;
	}
}
