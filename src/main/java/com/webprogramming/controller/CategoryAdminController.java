package com.webprogramming.controller;

import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
import com.webprogramming.entity.Category;
import com.webprogramming.model.CategoryForm;
import com.webprogramming.service.CategoryService;
import com.webprogramming.service.ProductService;
import com.webprogramming.util.ImageStorageService;

import jakarta.validation.Valid;
 
@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {
 
	@Autowired
	private CategoryService categoryService;
 
	@Autowired
	private ProductService productService;
 
	@Autowired
	private ImageStorageService imageStorageService;
 
	@GetMapping("")
	public String list(ModelMap model) {
		List<Category> list = categoryService.findAll();
 
		model.addAttribute("cateList", list);
		model.addAttribute("pageTitle", "Quan ly danh muc");
		model.addAttribute("activeMenu", "admin-categories");
		return "admin/category-manage";
	}
 
	@GetMapping("/add")
	public String add(ModelMap model) {
		CategoryForm form = new CategoryForm();
		form.setIsEdit(false);
		form.setStatus(0);
 
		model.addAttribute("category", form);
		model.addAttribute("pageTitle", "Them danh muc");
		model.addAttribute("activeMenu", "admin-categories");
		return "admin/category-add";
	}
 
	@GetMapping("/edit/{id}")
	public String edit(ModelMap model, @PathVariable("id") int id) {
		Optional<Category> optCategory = categoryService.findById(id);
		if (optCategory.isEmpty()) {
			return "redirect:/admin/categories";
		}
 
		Category entity = optCategory.get();
		CategoryForm form = new CategoryForm();
		BeanUtils.copyProperties(entity, form);
		form.setIsEdit(true);
 
		model.addAttribute("category", form);
		model.addAttribute("currentImage", entity.getImages());
		model.addAttribute("pageTitle", "Sua danh muc");
		model.addAttribute("activeMenu", "admin-categories");
		return "admin/category-edit";
	}
 
	@PostMapping("/saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("category") CategoryForm form, BindingResult result,
			@RequestParam(value = "images", required = false) MultipartFile imageFile) throws Exception {
 
		boolean isEdit = Boolean.TRUE.equals(form.getIsEdit());
 
		if (!isEdit && (imageFile == null || imageFile.isEmpty())) {
			result.rejectValue("categoryName", "images.required", "Vui long chon icon cho danh muc");
		}
 
		if (result.hasErrors()) {
			model.addAttribute("pageTitle", isEdit ? "Sua danh muc" : "Them danh muc");
			model.addAttribute("activeMenu", "admin-categories");
			return new ModelAndView(isEdit ? "admin/category-edit" : "admin/category-add", model);
		}
 
		Optional<Category> existing = categoryService.findByCategoryname(form.getCategoryName());
		if (existing.isPresent() && (!isEdit || existing.get().getCategoryId() != form.getCategoryId())) {
			model.addAttribute("message", "Ten danh muc '" + form.getCategoryName() + "' da ton tai");
			model.addAttribute("pageTitle", isEdit ? "Sua danh muc" : "Them danh muc");
			model.addAttribute("activeMenu", "admin-categories");
			return new ModelAndView(isEdit ? "admin/category-edit" : "admin/category-add", model);
		}
 
		Category entity;
		if (isEdit) {
			entity = categoryService.findById(form.getCategoryId()).orElse(new Category());
		} else {
			entity = new Category();
		}
 
		entity.setCategoryName(form.getCategoryName());
		entity.setStatus(form.getStatus());
 
		if (imageFile != null && !imageFile.isEmpty()) {
			entity.setImages(imageStorageService.store(imageFile, "categories"));
		}
 
		categoryService.save(entity);
 
		redirectAttributes.addFlashAttribute("message",
				isEdit ? "Cap nhat danh muc thanh cong!" : "Them danh muc thanh cong!");
		return new ModelAndView("redirect:/admin/categories");
	}
 
	@GetMapping("/delete/{id}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("id") int id) {
		long count = productService.countByCategory(id);
		if (count > 0) {
			redirectAttributes.addFlashAttribute("flashError",
					"Khong the xoa: danh muc nay con " + count + " san pham.");
		} else {
			categoryService.deleteById(id);
		}
		return "redirect:/admin/categories";
	}
}