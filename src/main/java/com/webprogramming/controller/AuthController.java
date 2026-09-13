package com.webprogramming.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.webprogramming.entity.User;
import com.webprogramming.model.LoginForm;
import com.webprogramming.model.RegisterForm;
import com.webprogramming.service.UserService;
import com.webprogramming.util.PasswordUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String loginForm(ModelMap model) {
		model.addAttribute("loginForm", new LoginForm());
		model.addAttribute("pageTitle", "Dang nhap");
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView login(ModelMap model, HttpServletRequest req,
			@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("login", model);
		}

		Optional<User> user = userService.authenticate(form.getEmail(), form.getPassword());
		if (user.isEmpty()) {
			model.addAttribute("error", "Email hoac mat khau khong dung");
			return new ModelAndView("login", model);
		}

		HttpSession session = req.getSession(true);
		session.setAttribute("currentUser", user.get());

		String redirect = "admin".equals(user.get().getRole()) ? "/admin/dashboard" : "/home";
		return new ModelAndView("redirect:" + redirect);
	}

	@GetMapping("/register")
	public String registerForm(ModelMap model) {
		model.addAttribute("registerForm", new RegisterForm());
		model.addAttribute("pageTitle", "Dang ky");
		return "register";
	}

	@PostMapping("/register")
	public ModelAndView register(ModelMap model,
			@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult result) {

		if (form.getPassword() != null && form.getConfirmPassword() != null
				&& !form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "mismatch", "Mat khau khong khop");
		}

		if (userService.findByEmail(form.getEmail()).isPresent()) {
			result.rejectValue("email", "duplicate", "Email nay da duoc dang ky");
		}

		if (result.hasErrors()) {
			return new ModelAndView("register", model);
		}

		User user = new User();
		user.setFullname(form.getFullname());
		user.setEmail(form.getEmail());
		user.setPassword(PasswordUtil.hashPassword(form.getPassword()));
		user.setRole("user");
		user.setStatus(1); // active immediately (no OTP verification in this pass)
		user.setCreatedAt(LocalDateTime.now());

		userService.save(user);

		model.addAttribute("registered", true);
		model.addAttribute("loginForm", new LoginForm());
		model.addAttribute("pageTitle", "Dang nhap");
		return new ModelAndView("login", model);
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/home";
	}
}
