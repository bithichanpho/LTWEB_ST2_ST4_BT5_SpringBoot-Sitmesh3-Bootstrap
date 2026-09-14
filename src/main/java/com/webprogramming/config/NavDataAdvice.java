package com.webprogramming.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.webprogramming.entity.Category;
import com.webprogramming.entity.User;
import com.webprogramming.service.CartService;
import com.webprogramming.service.CategoryService;
import com.webprogramming.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class NavDataAdvice {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartService cartService;

	@ModelAttribute
	public void addNavData(Map<String, Object> model, HttpServletRequest request) {
		List<Category> categories = categoryService.findAll();

		Map<Integer, Long> counts = new HashMap<>();
		for (Category c : categories) {
			counts.put(c.getCategoryId(), productService.countByCategory(c.getCategoryId()));
		}

		model.put("navCategories", categories);
		model.put("navCategoryCounts", counts);
		model.put("navTotalProducts", productService.count());

		HttpSession session = request.getSession(false);
		User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;
		model.put("navCartCount", currentUser != null ? cartService.countItems(currentUser) : 0);
	}
}