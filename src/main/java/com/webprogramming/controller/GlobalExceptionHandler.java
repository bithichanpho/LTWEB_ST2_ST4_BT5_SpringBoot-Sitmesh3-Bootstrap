package com.webprogramming.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Bat toan bo loi khong duoc xu ly rieng (vi du: loi giai ma tham so do Tomcat nem ra
 * TRUOC KHI vao den controller, loi du lieu khong hop le, v.v...) va hien mot trang loi
 * than thien bang tieng Viet, thay vi de Spring Boot roi ve "Whitelabel Error Page".
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		ModelMap model = new ModelMap();

		String message = "Da co loi xay ra, vui long thu lai.";
		// Loi dac trung: Tomcat khong giai ma duoc tham so (thuong do du lieu autofill/copy-paste
		// bi loi font, chua ky tu khong hop le voi UTF-8)
		if (ex.getClass().getName().contains("InvalidParameterException")
				|| (ex.getMessage() != null && ex.getMessage().contains("Character decoding failed"))) {
			message = "Du lieu ban nhap (vi du: dia chi) chua ky tu khong hop le, co the do trinh duyet "
					+ "tu dien (autofill) bi loi font. Vui long xoa trang va nhap lai thu cong roi thu lai.";
		}

		model.addAttribute("errorMessage", message);
		model.addAttribute("pageTitle", "Co loi xay ra");

		ModelAndView mav = new ModelAndView("error-friendly", model);
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}
}
