package com.webprogramming.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webprogramming.entity.User;
import com.webprogramming.model.UserForm;
import com.webprogramming.service.UserService;
import com.webprogramming.util.PasswordUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String list(ModelMap model,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size) {

		Page<User> result = userService.search(keyword, page, size);

		model.addAttribute("userList", result.getContent());
		model.addAttribute("userPage", result);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageTitle", "Quan ly nguoi dung");
		model.addAttribute("pageSubtitle", "Them, sua va quan ly toan bo tai khoan trong he thong.");
		model.addAttribute("activeMenu", "admin-users");
		return "admin/user-manage";
	}

	@GetMapping("/add")
	public String add(ModelMap model) {
		UserForm form = new UserForm();
		form.setIsEdit(false);
		form.setStatus(1);
		form.setRole("user");

		model.addAttribute("userForm", form);
		model.addAttribute("pageTitle", "Them nguoi dung");
		model.addAttribute("activeMenu", "admin-users");
		return "admin/user-add";
	}

	@GetMapping("/edit/{id}")
	public String edit(ModelMap model, @PathVariable("id") int id) {
		Optional<User> optUser = userService.findById(id);
		if (optUser.isEmpty()) {
			return "redirect:/admin/users";
		}

		User entity = optUser.get();
		UserForm form = new UserForm();
		form.setUserId(entity.getUserId());
		form.setFullname(entity.getFullname());
		form.setEmail(entity.getEmail());
		form.setPhone(entity.getPhone());
		form.setRole(entity.getRole());
		form.setStatus(entity.getStatus());
		form.setIsEdit(true);

		model.addAttribute("userForm", form);
		model.addAttribute("pageTitle", "Sua nguoi dung");
		model.addAttribute("activeMenu", "admin-users");
		return "admin/user-edit";
	}

	@PostMapping("/saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, RedirectAttributes redirectAttributes,
			HttpServletRequest request,
			@Valid @ModelAttribute("userForm") UserForm form, BindingResult result) {

		boolean isEdit = Boolean.TRUE.equals(form.getIsEdit());

		// bat buoc nhap mat khau khi tao moi; khi sua duoc phep de trong (giu nguyen)
		if (!isEdit && (form.getPassword() == null || form.getPassword().isBlank())) {
			result.rejectValue("password", "password.required", "Vui long nhap mat khau");
		} else if (!isEdit && form.getPassword() != null && form.getPassword().length() < 6) {
			result.rejectValue("password", "password.size", "Mat khau phai co it nhat 6 ky tu");
		}

		Optional<User> existingByEmail = userService.findByEmail(form.getEmail());
		if (existingByEmail.isPresent() && (!isEdit || existingByEmail.get().getUserId() != form.getUserId())) {
			result.rejectValue("email", "email.duplicate", "Email nay da duoc su dung");
		}

		if (result.hasErrors()) {
			model.addAttribute("pageTitle", isEdit ? "Sua nguoi dung" : "Them nguoi dung");
			model.addAttribute("activeMenu", "admin-users");
			return new ModelAndView(isEdit ? "admin/user-edit" : "admin/user-add", model);
		}

		User entity;
		if (isEdit) {
			entity = userService.findById(form.getUserId()).orElse(new User());
		} else {
			entity = new User();
			entity.setCreatedAt(LocalDateTime.now());
		}

		entity.setFullname(form.getFullname());
		entity.setEmail(form.getEmail());
		entity.setPhone(form.getPhone());
		entity.setRole(form.getRole());
		entity.setStatus(form.getStatus());

		if (form.getPassword() != null && !form.getPassword().isBlank()) {
			entity.setPassword(PasswordUtil.hashPassword(form.getPassword()));
		}

		userService.save(entity);

		// neu admin tu sua chinh minh, cap nhat lai thong tin trong session
		HttpSession session = request.getSession(false);
		if (session != null) {
			User current = (User) session.getAttribute("currentUser");
			if (current != null && current.getUserId() == entity.getUserId()) {
				session.setAttribute("currentUser", entity);
			}
		}

		redirectAttributes.addFlashAttribute("message",
				isEdit ? "Cap nhat nguoi dung thanh cong!" : "Them nguoi dung thanh cong!");
		return new ModelAndView("redirect:/admin/users");
	}

	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes redirectAttributes, HttpServletRequest request,
			@PathVariable("id") int id) {

		HttpSession session = request.getSession(false);
		User current = session != null ? (User) session.getAttribute("currentUser") : null;

		if (current != null && current.getUserId() == id) {
			redirectAttributes.addFlashAttribute("flashError", "Khong the tu xoa chinh tai khoan dang dang nhap.");
			return "redirect:/admin/users";
		}

		userService.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "Xoa nguoi dung thanh cong!");
		return "redirect:/admin/users";
	}
}