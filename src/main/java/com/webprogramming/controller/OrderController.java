package com.webprogramming.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webprogramming.entity.Order;
import com.webprogramming.entity.User;
import com.webprogramming.service.CartService;
import com.webprogramming.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	// Man hinh xac nhan thong tin giao hang / thanh toan truoc khi dat
	@GetMapping("/checkout")
	public String checkout(ModelMap model, HttpServletRequest req, RedirectAttributes redirectAttributes) {
		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		if (cartService.getItems(user).isEmpty()) {
			redirectAttributes.addFlashAttribute("flashError", "Gio hang dang trong");
			return "redirect:/cart";
		}

		model.addAttribute("cartItems", cartService.getItems(user));
		model.addAttribute("cartTotal", cartService.calculateCartTotal(user));
		model.addAttribute("currentUser", user);
		model.addAttribute("pageTitle", "Thanh toan");
		model.addAttribute("activeMenu", "cart");
		return "checkout";
	}

	// Xu ly dat hang: tao Order + OrderDetail tu gio hang, tinh tong tien
	@PostMapping("/order/place")
	public String placeOrder(HttpServletRequest req, RedirectAttributes redirectAttributes,
			@RequestParam("recipientName") String recipientName,
			@RequestParam("phone") String phone,
			@RequestParam("address") String address,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod) {

		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		try {
			Order order = orderService.placeOrder(user, recipientName, phone, address, note, paymentMethod);

			if (Order.PAYMENT_BANKING.equals(order.getPaymentMethod())) {
				redirectAttributes.addFlashAttribute("message",
						"Tao don hang thanh cong. Vui long thanh toan online de hoan tat don.");
				return "redirect:/order/payment/" + order.getOrderId();
			}

			redirectAttributes.addFlashAttribute("message",
					"Dat hang thanh cong! Ma don hang: #" + order.getOrderId());
			return "redirect:/order/detail/" + order.getOrderId();
		} catch (IllegalStateException | IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
			return "redirect:/cart";
		}
	}

	// Lich su don hang cua khach hang dang dang nhap
	@GetMapping("/order/history")
	public String history(ModelMap model, HttpServletRequest req) {
		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		List<Order> orders = orderService.findByUser(user);
		model.addAttribute("orderList", orders);
		model.addAttribute("pageTitle", "Lich su don hang");
		model.addAttribute("activeMenu", "order-history");
		return "order-history";
	}

	// Chi tiet 1 don hang - khach hang chi xem duoc don hang cua chinh minh
	@GetMapping("/order/detail/{id}")
	public String detail(ModelMap model, HttpServletRequest req, @PathVariable("id") int id) {
		User user = requireLogin(req);
		if (user == null) {
			return "redirect:/login";
		}

		Optional<Order> optOrder = orderService.findById(id);
		if (optOrder.isEmpty()) {
			return "redirect:/order/history";
		}

		Order order = optOrder.get();
		boolean isOwner = order.getUser().getUserId() == user.getUserId();
		boolean isAdmin = "admin".equals(user.getRole());
		if (!isOwner && !isAdmin) {
			return "redirect:/order/history";
		}

		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderService.findDetails(id));
		model.addAttribute("pageTitle", "Don hang #" + order.getOrderId());
		model.addAttribute("activeMenu", "order-history");
		return "order-detail";
	}

	private User requireLogin(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return session != null ? (User) session.getAttribute("currentUser") : null;
	}
}
