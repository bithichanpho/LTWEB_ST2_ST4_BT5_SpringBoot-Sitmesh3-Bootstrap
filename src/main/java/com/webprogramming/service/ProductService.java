package com.webprogramming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webprogramming.entity.Product;
import com.webprogramming.repository.IProductRepository;

@Service
public class ProductService {
	@Autowired
	private IProductRepository productRepository;
 
	public List<Product> findAll() {
		return productRepository.findAll(Sort.by(Sort.Direction.DESC, "productId"));
	}
 
	public Page<Product> findAll(int zeroBasedPage, int pageSize) {
		Pageable pageable = PageRequest.of(zeroBasedPage, pageSize);
		return productRepository.findAllByOrderByProductIdDesc(pageable);
	}
 
	public List<Product> findLatest(int limit) {
		List<Product> latest = productRepository.findTop10ByOrderByCreatedAtDesc();
		return latest.size() > limit ? latest.subList(0, limit) : latest;
	}
 
	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}
 
	public List<Product> findByCategory(int categoryId) {
		return productRepository.findByCategory_CategoryIdOrderByProductIdDesc(categoryId);
	}
 
	public long countByCategory(int categoryId) {
		return productRepository.countByCategory_CategoryId(categoryId);
	}
 
	public long count() {
		return productRepository.count();
	}
 
	public Optional<Product> findByName(String name) {
		return productRepository.findByProductName(name);
	}
 
	public boolean existsByNameAndCategory(String name, int categoryId) {
		return productRepository.existsByProductNameAndCategory_CategoryId(name, categoryId);
	}
 
	public Product save(Product product) {
		return productRepository.save(product);
	}

	private static final java.util.regex.Pattern ID_PATTERN = java.util.regex.Pattern.compile("^SP(\\d+)$");

	/**
	 * Sinh ma san pham tiep theo theo dinh dang "SP01", "SP02", ... dua tren
	 * so lon nhat dang co trong bang (khong dung @GeneratedValue vi ID la
	 * chuoi tuy bien, khong phai so tu tang).
	 */
	public String generateNextProductId() {
		int max = 0;
		for (Product p : productRepository.findAll()) {
			java.util.regex.Matcher m = ID_PATTERN.matcher(p.getProductId());
			if (m.matches()) {
				max = Math.max(max, Integer.parseInt(m.group(1)));
			}
		}
		return String.format("SP%02d", max + 1);
	}
 
	public void deleteById(String id) {
		productRepository.deleteById(id);
	}
 
	public double sumRevenue() {
		return productRepository.sumRevenue();
	}
 
	public long sumSold() {
		return productRepository.sumSold();
	}
 
	public double sumRevenueByCategory(int categoryId) {
		return productRepository.sumRevenueByCategory(categoryId);
	}
 
	public long sumSoldByCategory(int categoryId) {
		return productRepository.sumSoldByCategory(categoryId);
	}
}
