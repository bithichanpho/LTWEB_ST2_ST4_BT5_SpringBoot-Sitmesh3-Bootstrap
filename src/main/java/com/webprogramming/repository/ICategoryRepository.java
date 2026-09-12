package com.webprogramming.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webprogramming.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByCategoryName(String categoryName);

}
