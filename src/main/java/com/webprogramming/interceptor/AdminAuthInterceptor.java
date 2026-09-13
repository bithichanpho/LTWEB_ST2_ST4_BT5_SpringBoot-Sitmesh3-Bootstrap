package com.webprogramming.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.webprogramming.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);
		User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;

		if (currentUser == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		if (!"admin".equals(currentUser.getRole())) {
			response.sendRedirect(request.getContextPath() + "/home");
			return false;
		}
		return true;
	}
}
