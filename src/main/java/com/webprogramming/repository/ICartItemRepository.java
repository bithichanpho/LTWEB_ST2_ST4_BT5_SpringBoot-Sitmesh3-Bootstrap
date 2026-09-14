package com.webprogramming.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.CartItem;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Integer> {
	List<CartItem> findByCart_CartId(int cartId);

	Optional<CartItem> findByCart_CartIdAndProduct_ProductId(int cartId, String productId);

	void deleteByCart_CartId(int cartId);
}
