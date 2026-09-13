package com.webprogramming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webprogramming.entity.Category;
import com.webprogramming.repository.ICategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final ICategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Page<Category> search(String keyword, int zeroBasedPage, int pageSize) {
		Pageable pageable = PageRequest.of(Math.max(zeroBasedPage, 0), pageSize,
				Sort.by(Sort.Direction.ASC, "categoryId"));
		String kw = keyword == null ? "" : keyword.trim();
		return categoryRepository.findByCategoryNameContainingIgnoreCase(kw, pageable);
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