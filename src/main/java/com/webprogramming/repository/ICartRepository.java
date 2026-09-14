package com.webprogramming.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {
	Optional<Cart> findByUser_UserId(int userId);
}
