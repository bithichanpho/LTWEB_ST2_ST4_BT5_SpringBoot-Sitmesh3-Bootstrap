package com.webprogramming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webprogramming.entity.User;
import com.webprogramming.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping("/cart")
	public String viewCart(ModelMap model, HttpServletRequest req) {
		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("cartItems", cartService.getItems(user));
		model.addAttribute("cartTotal", cartService.calculateCartTotal(user));
		model.addAttribute("pageTitle", "Gio hang");
		model.addAttribute("activeMenu", "cart");
		return "cart";
	}

	@PostMapping("/cart/add")
	public String addToCart(HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam("productId") String productId,
			@RequestParam(value = "quantity", defaultValue = "1") int quantity,
			@RequestParam(value = "redirect", required = false) String redirect) {

		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		try {
			cartService.addToCart(user, productId, quantity);
			redirectAttributes.addFlashAttribute("message", "Da them san pham vao gio hang");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
		}

		return "redirect:" + (redirect != null && !redirect.isBlank() ? redirect : "/cart");
	}

	@PostMapping("/cart/update")
	public String updateQuantity(HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam("cartItemId") int cartItemId, @RequestParam("quantity") int quantity) {

		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		try {
			cartService.updateQuantity(user, cartItemId, quantity);
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
		}
		return "redirect:/cart";
	}

	@PostMapping("/cart/remove")
	public String removeItem(HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam("cartItemId") int cartItemId) {

		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		try {
			cartService.removeItem(user, cartItemId);
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
		}
		return "redirect:/cart";
	}

	private User requireLogin(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return session != null ? (User) session.getAttribute("currentUser") : null;
	}
}
