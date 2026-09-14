package com.webprogramming.controller;

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
import com.webprogramming.service.OrderService;
import com.webprogramming.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/order/payment/{id}")
    public String paymentPage(ModelMap model, HttpServletRequest req,
            RedirectAttributes redirectAttributes, @PathVariable("id") int id) {

        User user = requireLogin(req);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Order> optOrder = orderService.findById(id);
        if (optOrder.isEmpty()) {
            redirectAttributes.addFlashAttribute("flashError", "Don hang khong ton tai");
            return "redirect:/order/history";
        }

        Order order = optOrder.get();
        if (order.getUser().getUserId() != user.getUserId()) {
            redirectAttributes.addFlashAttribute("flashError", "Ban khong co quyen thanh toan don hang nay");
            return "redirect:/order/history";
        }

        if (!Order.PAYMENT_BANKING.equals(order.getPaymentMethod())) {
            redirectAttributes.addFlashAttribute("flashError", "Don hang nay khong su dung thanh toan online");
            return "redirect:/order/detail/" + id;
        }

        if (order.isPaid()) {
            redirectAttributes.addFlashAttribute("message", "Don hang da duoc thanh toan");
            return "redirect:/order/detail/" + id;
        }

        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Thanh toan don hang #" + id);
        model.addAttribute("activeMenu", "order-history");
        return "payment";
    }

    @PostMapping("/order/payment/{id}")
    public String processPayment(HttpServletRequest req, RedirectAttributes redirectAttributes,
            @PathVariable("id") int id, @RequestParam("result") String result) {

        User user = requireLogin(req);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Optional<Order> optOrder = orderService.findById(id);
            if (optOrder.isEmpty()) {
                throw new IllegalArgumentException("Don hang khong ton tai");
            }

            Order order = optOrder.get();
            if (order.getUser().getUserId() != user.getUserId()) {
                throw new IllegalArgumentException("Ban khong co quyen thanh toan don hang nay");
            }
            if (!Order.PAYMENT_BANKING.equals(order.getPaymentMethod())) {
                throw new IllegalStateException("Don hang nay khong su dung thanh toan online");
            }
            if (order.isPaid()) {
                redirectAttributes.addFlashAttribute("message", "Don hang da duoc thanh toan");
                return "redirect:/order/detail/" + id;
            }

            boolean success = paymentService.processPayment(order.getTotalAmount(), result);
            Order updated = orderService.processOnlinePayment(id, success);

            if (success) {
                redirectAttributes.addFlashAttribute("message",
                        "Thanh toan thanh cong. Don hang #" + updated.getOrderId() + " da duoc ghi nhan.");
                return "redirect:/order/detail/" + updated.getOrderId();
            }

            redirectAttributes.addFlashAttribute("flashError",
                    "Thanh toan that bai. Ban co the thu lai.");
            return "redirect:/order/payment/" + id;

        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("flashError", ex.getMessage());
            return "redirect:/order/detail/" + id;
        }
    }

    private User requireLogin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? (User) session.getAttribute("currentUser") : null;
    }
}
