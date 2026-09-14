package com.webprogramming.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Dat ten JPQL entity la "OrderEntity" (khac "Order") de tranh xung dot voi tu khoa
// "ORDER BY" khi Hibernate parse cac cau truy van JPQL.
@Entity(name = "OrderEntity")
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

	// Cac trang thai don hang hop le - dung chung cho ca khach hang va nhan vien
	public static final String STATUS_PENDING = "PENDING"; // cho xac nhan
	public static final String STATUS_CONFIRMED = "CONFIRMED"; // da xac nhan
	public static final String STATUS_SHIPPING = "SHIPPING"; // dang giao
	public static final String STATUS_COMPLETED = "COMPLETED"; // hoan thanh
	public static final String STATUS_CANCELLED = "CANCELLED"; // da huy

	public static final String PAYMENT_COD = "COD";
	public static final String PAYMENT_BANKING = "BANKING";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int orderId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	// Tong tien don hang = tong (gia * so luong) cua tat ca OrderDetail.
	// Duoc tinh toan va gan boi OrderService, khong cho nguoi dung nhap tay.
	@Column(name = "total_amount")
	private double totalAmount;

	@Column(name = "status", length = 20)
	private String status = STATUS_PENDING;

	@Column(name = "payment_method", length = 20)
	private String paymentMethod = PAYMENT_COD;

	@Column(name = "paid")
	private boolean paid = false;

	@Column(name = "recipient_name", columnDefinition = "NVARCHAR(255) NULL")
	private String recipientName;

	@Column(name = "phone", columnDefinition = "NVARCHAR(20) NULL")
	private String phone;

	@Column(name = "address", columnDefinition = "NVARCHAR(500) NULL")
	private String address;

	@Column(name = "note", columnDefinition = "NVARCHAR(500) NULL")
	private String note;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderDetail> orderDetails = new ArrayList<>();

	// LocalDateTime khong duoc JSTL fmt:formatDate ho tro truc tiep nen cung cap san chuoi da format cho JSP
	public String getOrderDateFormatted() {
		return orderDate == null ? "" : orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}
}
