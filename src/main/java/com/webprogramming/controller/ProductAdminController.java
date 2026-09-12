package com.webprogramming.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import com.webprogramming.entity.Product;
import com.webprogramming.model.ProductForm;
import com.webprogramming.service.CategoryService;
import com.webprogramming.service.ProductService;
import com.webprogramming.util.ImageStorageService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ProductAdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ImageStorageService imageStorageService;

	@GetMapping("/products")
	public String list(ModelMap model) {
		List<Product> list = productService.findAll();

		model.addAttribute("productList", list);
		model.addAttribute("pageTitle", "Quan ly san pham");
		model.addAttribute("pageSubtitle", "Them, sua va quan ly toan bo san pham trong cua hang.");
		model.addAttribute("activeMenu", "admin-products");
		return "admin/product-manage";
	}

	@GetMapping("/product/add")
	public String add(ModelMap model) {
		ProductForm form = new ProductForm();
		form.setIsEdit(false);

		model.addAttribute("product", form);
		model.addAttribute("cateList", categoryService.findAll());
		model.addAttribute("pageTitle", "Them san pham");
		model.addAttribute("activeMenu", "admin-products");
		return "admin/product-add";
	}

	@GetMapping("/product/edit/{id}")
	public String edit(ModelMap model, @PathVariable("id") String id) {
		Optional<Product> optProduct = productService.findById(id);
		if (optProduct.isEmpty()) {
			return "redirect:/admin/products";
		}

		Product entity = optProduct.get();
		ProductForm form = new ProductForm();
		form.setProductId(entity.getProductId());
		form.setProductName(entity.getProductName());
		form.setPrice(entity.getPrice());
		form.setQuantity(entity.getQuantity());
		form.setDescription(entity.getDescription());
		form.setCategoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : null);
		form.setIsEdit(true);

		model.addAttribute("product", form);
		model.addAttribute("currentImage", entity.getImages());
		model.addAttribute("cateList", categoryService.findAll());
		model.addAttribute("pageTitle", "Sua san pham");
		model.addAttribute("activeMenu", "admin-products");
		return "admin/product-edit";
	}

	@PostMapping("/product/saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("product") ProductForm form, BindingResult result,
			@RequestParam(value = "images", required = false) MultipartFile imageFile) throws Exception {

		boolean isEdit = Boolean.TRUE.equals(form.getIsEdit());

		// bat buoc chon anh khi tao moi san pham
		if (!isEdit && (imageFile == null || imageFile.isEmpty())) {
			result.rejectValue("productName", "images.required", "Vui long chon hinh anh san pham");
		}

		Optional<Category> optCategory = form.getCategoryId() != null
				? categoryService.findById(form.getCategoryId())
				: Optional.empty();

		if (result.hasErrors() || optCategory.isEmpty()) {
			if (optCategory.isEmpty()) {
				model.addAttribute("message", "Danh muc khong ton tai");
			}
			model.addAttribute("cateList", categoryService.findAll());
			model.addAttribute("pageTitle", isEdit ? "Sua san pham" : "Them san pham");
			model.addAttribute("activeMenu", "admin-products");
			return new ModelAndView(isEdit ? "admin/product-edit" : "admin/product-add", model);
		}

		// kiem tra trung ten (cung danh muc), giu dung nguyen tac cua ban goc
		Optional<Product> existing = productService.findByName(form.getProductName());
		boolean duplicate = existing.isPresent()
				&& (!isEdit || !existing.get().getProductId().equals(form.getProductId()));
		if (duplicate) {
			model.addAttribute("message", "Ten san pham '" + form.getProductName() + "' da ton tai");
			model.addAttribute("cateList", categoryService.findAll());
			model.addAttribute("pageTitle", isEdit ? "Sua san pham" : "Them san pham");
			model.addAttribute("activeMenu", "admin-products");
			return new ModelAndView(isEdit ? "admin/product-edit" : "admin/product-add", model);
		}

		Product entity;
		if (isEdit) {
			entity = productService.findById(form.getProductId()).orElse(new Product());
		} else {
			entity = new Product();
			entity.setProductId(productService.generateNextProductId()); // "SP01", "SP02"...
			entity.setCreatedAt(LocalDateTime.now());
		}

		entity.setProductName(form.getProductName());
		entity.setPrice(form.getPrice());
		entity.setQuantity(form.getQuantity());
		entity.setDescription(form.getDescription());
		entity.setCategory(optCategory.get());

		if (imageFile != null && !imageFile.isEmpty()) {
			entity.setImages(imageStorageService.store(imageFile, "products"));
		}

		productService.save(entity);

		redirectAttributes.addFlashAttribute("message",
				isEdit ? "Cap nhat san pham thanh cong!" : "Them san pham thanh cong!");
		return new ModelAndView("redirect:/admin/products");
	}

	@GetMapping("/product/delete/{id}")
	public String delete(@PathVariable("id") String id) {
		productService.deleteById(id);
		return "redirect:/admin/products";
	}
}