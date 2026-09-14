package com.webprogramming.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webprogramming.entity.Order;
import com.webprogramming.service.OrderService;

/**
 * Nghiep vu danh cho nhan vien: xu ly don hang + cap nhat trang thai don hang.
 * Du an hien chi co 2 vai tro (user/admin) nen tam dung chung role "admin" cho nhan vien,
 * duoc bao ve boi AdminAuthInterceptor tren duong dan /admin/**.
 */
@Controller
@RequestMapping("/admin")
public class OrderAdminController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private OrderService orderService;

	@GetMapping("/orders")
	public String list(ModelMap model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "status", required = false, defaultValue = "") String status) {
		if (page < 1) {
			page = 1;
		}

		Page<Order> result = orderService.findAll(status, page - 1, PAGE_SIZE);

		model.addAttribute("orderList", result.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", Math.max(result.getTotalPages(), 1));
		model.addAttribute("status", status);
		model.addAttribute("pageTitle", "Xu ly don hang");
		model.addAttribute("pageSubtitle", "Xac nhan, cap nhat trang thai va theo doi don hang cua khach.");
		model.addAttribute("activeMenu", "admin-orders");
		return "admin/order-manage";
	}

	@GetMapping("/order/detail/{id}")
	public String detail(ModelMap model, RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
		Optional<Order> optOrder = orderService.findById(id);
		if (optOrder.isEmpty()) {
			redirectAttributes.addFlashAttribute("flashError", "Don hang khong ton tai");
			return "redirect:/admin/orders";
		}

		model.addAttribute("order", optOrder.get());
		model.addAttribute("orderDetails", orderService.findDetails(id));
		model.addAttribute("pageTitle", "Don hang #" + id);
		model.addAttribute("activeMenu", "admin-orders");
		return "admin/order-detail";
	}

	// Cap nhat trang thai don hang theo dung quy trinh: PENDING -> CONFIRMED -> SHIPPING -> COMPLETED
	// hoac chuyen sang CANCELLED (huy don, tra lai ton kho)
	@PostMapping("/order/updateStatus")
	public String updateStatus(RedirectAttributes redirectAttributes,
			@RequestParam("orderId") int orderId, @RequestParam("status") String status) {
		try {
			orderService.updateStatus(orderId, status);
			redirectAttributes.addFlashAttribute("message", "Cap nhat trang thai don hang thanh cong");
		} catch (IllegalArgumentException | IllegalStateException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
		}
		return "redirect:/admin/order/detail/" + orderId;
	}

	// Xac nhan da thu tien (vd: chuyen khoan) cho don hang
	@PostMapping("/order/markPaid")
	public String markPaid(RedirectAttributes redirectAttributes, @RequestParam("orderId") int orderId) {
		try {
			orderService.markPaid(orderId);
			redirectAttributes.addFlashAttribute("message", "Da xac nhan thanh toan don hang");
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
		}
		return "redirect:/admin/order/detail/" + orderId;
	}
}
