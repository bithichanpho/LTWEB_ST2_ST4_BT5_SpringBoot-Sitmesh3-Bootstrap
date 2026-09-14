package com.webprogramming.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.webprogramming.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
	@org.springframework.beans.factory.annotation.Autowired
	private com.webprogramming.service.UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession(false);
		User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;

		if (currentUser == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		User freshUser = userService.findById(currentUser.getUserId()).orElse(null);
		if (freshUser == null || freshUser.getStatus() != 1) {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		session.setAttribute("currentUser", freshUser);
		if (!"admin".equals(freshUser.getRole())) {
			response.sendRedirect(request.getContextPath() + "/home");
			return false;
		}
		return true;
	}
}
