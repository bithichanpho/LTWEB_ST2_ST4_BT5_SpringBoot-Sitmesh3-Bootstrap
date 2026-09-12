package com.webprogramming.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.webprogramming.interceptor.AdminAuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
 
	@Value("${app.upload.dir}")
	private String uploadDir;
 
	@Autowired
	private AdminAuthInterceptor adminAuthInterceptor;
 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Checked in order: admin-uploaded images on disk first, then the
		// sample images bundled with the app (src/main/resources/static/image).
		registry.addResourceHandler("/image/**")
				.addResourceLocations("file:" + uploadDir + "/", "classpath:/static/image/");
 
		registry.addResourceHandler("/assets/**")
				.addResourceLocations("classpath:/static/assets/");
	}
 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminAuthInterceptor).addPathPatterns("/admin/**");
	}
}
