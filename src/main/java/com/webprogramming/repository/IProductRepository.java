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

	// Ma san pham co dinh dang "SPxx" -> sinh ma tiep theo dua tren toan bo
	// danh sach (parse trong ProductService, an toan hon ORDER BY chuoi).
 
	@Query("SELECT COALESCE(SUM(p.sold * p.price), 0) FROM Product p")
	double sumRevenue();
 
	@Query("SELECT COALESCE(SUM(p.sold), 0) FROM Product p")
	long sumSold();
 
	@Query("SELECT COALESCE(SUM(p.sold * p.price), 0) FROM Product p WHERE p.category.categoryId = :categoryId")
	double sumRevenueByCategory(@Param("categoryId") int categoryId);
 
	@Query("SELECT COALESCE(SUM(p.sold), 0) FROM Product p WHERE p.category.categoryId = :categoryId")
	long sumSoldByCategory(@Param("categoryId") int categoryId);
}
