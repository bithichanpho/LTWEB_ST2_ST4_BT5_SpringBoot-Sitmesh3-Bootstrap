package com.webprogramming.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "fullname", columnDefinition = "NVARCHAR(255) NULL")
	private String fullname;

	@Column(name = "email", unique = true, nullable = false, columnDefinition = "NVARCHAR(255)")
	private String email;

	@Column(name = "password", nullable = false, columnDefinition = "NVARCHAR(255)")
	private String password;

	// 0 = chua kich hoat, 1 = da kich hoat
	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "role", nullable = false)
	private String role = "user";

	@Column(name = "phone", columnDefinition = "NVARCHAR(20) NULL")
	private String phone;

	@Column(name = "avatar", columnDefinition = "NVARCHAR(255) NULL")
	private String avatar;
}