package com.webprogramming.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, String>{
	Page<Product> findAllByOrderByProductIdDesc(Pageable pageable);

	List<Product> findTop10ByOrderByCreatedAtDesc();

	long countByCategory_CategoryId(int categoryId);

	List<Product> findByCategory_CategoryIdOrderByProductIdDesc(int categoryId);

	Optional<Product> findByProductName(String productName);

	boolean existsByProductNameAndCategory_CategoryId(String productName, int categoryId);

	Page<Product> findByProductNameContainingIgnoreCaseOrderByProductIdDesc(String keyword, Pageable pageable);

	@Query("SELECT COALESCE(SUM(d.quantity * d.price), 0) FROM OrderDetail d WHERE d.order.status = 'COMPLETED'")
	double sumRevenue();

	@Query("SELECT COALESCE(SUM(d.quantity), 0) FROM OrderDetail d WHERE d.order.status = 'COMPLETED'")
	long sumSold();

	@Query("SELECT COALESCE(SUM(d.quantity * d.price), 0) FROM OrderDetail d WHERE d.order.status = 'COMPLETED' AND d.product.category.categoryId = :categoryId")
	double sumRevenueByCategory(@Param("categoryId") int categoryId);

	@Query("SELECT COALESCE(SUM(d.quantity), 0) FROM OrderDetail d WHERE d.order.status = 'COMPLETED' AND d.product.category.categoryId = :categoryId")
	long sumSoldByCategory(@Param("categoryId") int categoryId);
}
