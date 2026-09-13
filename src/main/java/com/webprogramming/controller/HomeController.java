package com.webprogramming.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.webprogramming.entity.Product;
import com.webprogramming.service.ProductService;



@Controller
public class HomeController {

	@Autowired
	private ProductService productService;

	@GetMapping("/")
	public String root() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(ModelMap model) {
		List<Product> latest = productService.findLatest(10);

		model.addAttribute("latestProducts", latest);
		model.addAttribute("pageTitle", "Trang chu");
		model.addAttribute("activeMenu", "home");
		return "home";
	}
}
