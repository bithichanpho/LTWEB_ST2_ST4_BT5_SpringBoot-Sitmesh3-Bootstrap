package com.webprogramming.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webprogramming.entity.Category;
import com.webprogramming.entity.Product;
import com.webprogramming.service.CategoryService;
import com.webprogramming.service.ProductService;

@Controller
public class CategoryController {
 
	@Autowired
	private CategoryService categoryService;
 
	@Autowired
	private ProductService productService;
 
	@GetMapping("/categories")
	public String list(ModelMap model) {
		List<Category> list = categoryService.findAll();
 
		model.addAttribute("cateList", list);
		model.addAttribute("pageTitle", "Danh muc");
		model.addAttribute("activeMenu", "categories");
		return "category-list";
	}
 
	@GetMapping("/category/detail")
	public String detail(ModelMap model, @RequestParam("id") int id) {
		Optional<Category> optCategory = categoryService.findById(id);
 
		if (optCategory.isEmpty()) {
			return "redirect:/categories";
		}
 
		Category category = optCategory.get();
		List<Product> products = productService.findByCategory(id);
 
		model.addAttribute("cate", category);
		model.addAttribute("productList", products);
		model.addAttribute("pageTitle", category.getCategoryName());
		model.addAttribute("activeMenu", "category-" + id);
		return "category-products";
	}
}
