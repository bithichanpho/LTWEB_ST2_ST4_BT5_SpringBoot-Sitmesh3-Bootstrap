package com.webprogramming.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class AppSiteMeshFilter extends ConfigurableSiteMeshFilter {

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		// Map URL gốc trên trình duyệt
		builder.addDecoratorPath("/admin/*", "/WEB-INF/decorators/main.jsp")
		       // Loại trừ các file tĩnh để tránh lỗi render sai
		       .addExcludedPath("/assets/*");
	}
}