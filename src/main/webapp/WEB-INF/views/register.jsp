<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dang ky - SHOP</title>
  <link rel="icon" type="image/png" href="${ctx}/assets/images/favicon.ico">
  <link rel="stylesheet" href="${ctx}/assets/libs/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/assets/libs/bootstrap-icons/bootstrap-icons.css">
  <link rel="stylesheet" href="${ctx}/assets/css/main.css">
</head>

<body>
  <div class="login-wrapper">
    <div class="login-bg-shape login-bg-shape-1"></div>
    <div class="login-bg-shape login-bg-shape-2"></div>

    <div class="login-card">
      <a href="${ctx}/home" class="login-brand text-decoration-none">
        <i class="bi bi-bag-heart-fill"></i>
        <span>SHOP</span>
      </a>

      <p class="login-subtitle">Tao tai khoan moi</p>

      <form:form modelAttribute="registerForm" action="${ctx}/register" method="post" id="registerForm">
        <div class="login-form-group">
          <label for="fullname" class="login-form-label">Ho ten</label>
          <div class="login-input-group">
            <i class="bi bi-person input-icon"></i>
            <form:input path="fullname" id="fullname" cssClass="login-input" placeholder="Nguyen Van A" />
          </div>
          <form:errors path="fullname" cssStyle="color:#e5484d;font-size:.8rem;" />
        </div>

        <div class="login-form-group">
          <label for="email" class="login-form-label">Email</label>
          <div class="login-input-group">
            <i class="bi bi-envelope input-icon"></i>
            <form:input path="email" id="email" cssClass="login-input" placeholder="name@company.com" />
          </div>
          <form:errors path="email" cssStyle="color:#e5484d;font-size:.8rem;" />
        </div>

        <div class="login-form-group">
          <label for="password" class="login-form-label">Mat khau</label>
          <div class="login-input-group">
            <i class="bi bi-shield-lock input-icon"></i>
            <form:password path="password" id="password" cssClass="login-input login-input-password" placeholder="Toi thieu 6 ky tu" />
          </div>
          <form:errors path="password" cssStyle="color:#e5484d;font-size:.8rem;" />
        </div>

        <div class="login-form-group">
          <label for="confirmPassword" class="login-form-label">Nhap lai mat khau</label>
          <div class="login-input-group">
            <i class="bi bi-shield-lock input-icon"></i>
            <form:password path="confirmPassword" id="confirmPassword" cssClass="login-input login-input-password" />
          </div>
          <form:errors path="confirmPassword" cssStyle="color:#e5484d;font-size:.8rem;" />
        </div>

        <button type="submit" class="btn-login">
          <span>Dang ky</span>
          <i class="bi bi-arrow-right"></i>
        </button>
      </form:form>

      <p class="login-footer-text">
        Da co tai khoan? <a href="${ctx}/login">Dang nhap</a>
      </p>
    </div>
  </div>

  <script src="${ctx}/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>

</html>
