package com.webprogramming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webprogramming.entity.Category;
import com.webprogramming.repository.ICategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private ICategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Optional<Category> findById(int id) {
		return categoryRepository.findById(id);
	}

	public Optional<Category> findByCategoryname(String name) {
		return categoryRepository.findByCategoryName(name);
	}

	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	public void deleteById(int id) {
		categoryRepository.deleteById(id);
	}
}