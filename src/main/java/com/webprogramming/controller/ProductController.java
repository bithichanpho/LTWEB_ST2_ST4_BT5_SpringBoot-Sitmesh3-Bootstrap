package com.webprogramming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webprogramming.entity.Product;
import com.webprogramming.service.ProductService;

@Controller
public class ProductController {

	private static final int PAGE_SIZE = 8;

	@Autowired
	private ProductService productService;

	@GetMapping("/product")
	public String list(ModelMap model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
		if (page < 1) {
			page = 1;
		}

		Page<Product> result = productService.search(keyword, page - 1, PAGE_SIZE);

		model.addAttribute("productList", result.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", Math.max(result.getTotalPages(), 1));
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageTitle", "San pham");
		model.addAttribute("activeMenu", "products");
		return "product-list";
	}

	@GetMapping("/product/detail")
	public String detail(ModelMap model, @RequestParam("id") String id) {
		return productService.findById(id)
				.map(product -> {
					model.addAttribute("product", product);
					model.addAttribute("pageTitle", product.getProductName());
					model.addAttribute("activeMenu", "products");
					return "product-detail";
				})
				.orElse("redirect:/product");
	}
}