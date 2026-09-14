package com.webprogramming.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Dang ky rieng mot CharacterEncodingFilter voi do uu tien CAO NHAT (chay truoc tat ca
 * cac filter khac, ke ca FormContentFilter/RequestContextFilter cua Spring). Muc dich:
 * dam bao request.setCharacterEncoding("UTF-8") duoc goi TRUOC KHI bat ky filter/servlet
 * nao doc request parameter lan dau (vi Tomcat chi decode dung neu encoding duoc set
 * truoc lan doc getParameter() dau tien). Neu khong co filter nay chay truoc nhat, viec
 * chi khai bao server.servlet.encoding.* trong application.properties co the khong du
 * de ngan loi giai ma tieng Viet (dau `<html>` bi lech dau, xuat hien dau `?`).
 */
@Configuration
public class EncodingFilterConfig {

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true); // ep UTF-8 cho ca request lan response

		FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>(filter);
		registration.addUrlPatterns("/*");
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registration;
	}
}
