package com.webprogramming.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.webprogramming.entity.Category;
import com.webprogramming.entity.Product;
import com.webprogramming.model.CategoryStat;
import com.webprogramming.service.CategoryService;
import com.webprogramming.service.ProductService;

@Controller
public class DashboardController {
	@Autowired
	private com.webprogramming.repository.IOrderRepository orderRepository;
	@Autowired
	private com.webprogramming.repository.IOrderDetailRepository detailRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/admin/dashboard")
	public String dashboard(ModelMap model) {
		List<Product> products = productService.findAll();
		List<Category> categories = categoryService.findAll();

		double totalRevenue = productService.sumRevenue();
		long totalSold = productService.sumSold();
		long completedCount = orderRepository.countByStatus("COMPLETED");
		double avgOrderValue = completedCount == 0 ? 0 : totalRevenue / completedCount;

		List<CategoryStat> categoryStats = new ArrayList<>();
		for (Category c : categories) {
			double revenue = productService.sumRevenueByCategory(c.getCategoryId());
			long sold = productService.sumSoldByCategory(c.getCategoryId());
			long productCount = productService.countByCategory(c.getCategoryId());
			double percent = totalRevenue > 0 ? (revenue / totalRevenue) * 100.0 : 0;
			categoryStats.add(new CategoryStat(c, productCount, sold, revenue, percent));
		}
		categoryStats.sort(Comparator.comparingDouble(CategoryStat::getTotalRevenue).reversed());
		CategoryStat topCategory = categoryStats.isEmpty() ? null : categoryStats.get(0);

		java.util.Map<String, Product> byId = new java.util.HashMap<>();
		products.forEach(p -> byId.put(p.getProductId(), p));
		List<com.webprogramming.model.ProductSales> topProducts = new ArrayList<>();
		for (Object[] row : detailRepository.completedProductSales()) {
			Product p = byId.get((String) row[0]);
			if (p != null) topProducts.add(new com.webprogramming.model.ProductSales(
				p.getProductId(), p.getProductName(), ((Number) row[1]).longValue(), ((Number) row[2]).doubleValue()));
		}
		topProducts.sort(Comparator.comparingDouble(com.webprogramming.model.ProductSales::getRevenue).reversed());
		if (topProducts.size() > 5) {
			topProducts = topProducts.subList(0, 5);
		}

		model.addAttribute("totalRevenue", totalRevenue);
		model.addAttribute("totalSold", totalSold);
		model.addAttribute("totalProducts", products.size());
		model.addAttribute("totalCategories", categories.size());
		model.addAttribute("avgOrderValue", avgOrderValue);
		model.addAttribute("topCategory", topCategory);
		model.addAttribute("categoryStats", categoryStats);
		model.addAttribute("topProducts", topProducts);

		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("pageSubtitle", "Tong quan doanh thu va san pham ban chay.");
		model.addAttribute("activeMenu", "dashboard");
		return "admin/dashboard";
	}
}
