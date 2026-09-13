package com.webprogramming.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.webprogramming.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	Page<User> findByFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
			String fullnameKeyword, String emailKeyword, Pageable pageable);

}