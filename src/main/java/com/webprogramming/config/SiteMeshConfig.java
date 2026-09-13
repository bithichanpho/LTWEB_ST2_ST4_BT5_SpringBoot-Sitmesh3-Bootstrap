package com.webprogramming.config;

import java.util.EnumSet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.DispatcherType;

@Configuration
public class SiteMeshConfig {

	@Bean
	public FilterRegistrationBean<AppSiteMeshFilter> siteMeshFilterRegistration() {
		FilterRegistrationBean<AppSiteMeshFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new AppSiteMeshFilter());

		registration.addUrlPatterns("/admin/*", "/WEB-INF/views/admin/*");

		registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE));
		registration.setName("sitemesh");
		registration.setOrder(1);
		return registration;
	}
}