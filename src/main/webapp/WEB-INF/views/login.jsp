<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dang nhap - SHOP</title>
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

      <p class="login-subtitle">Dang nhap de tiep tuc mua sam</p>

      <c:if test="${param.registered or registered}">
        <div class="alert-custom alert-custom-success mb-3">
          <i class="bi bi-check-circle-fill alert-custom-icon"></i>
          <div class="alert-custom-content">Dang ky thanh cong! Hay dang nhap.</div>
        </div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert-custom alert-custom-danger mb-3">
          <i class="bi bi-exclamation-triangle-fill alert-custom-icon"></i>
          <div class="alert-custom-content">${error}</div>
        </div>
      </c:if>

      <form:form acceptCharset="UTF-8" modelAttribute="loginForm" action="${ctx}/login" method="post" id="loginForm">
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
            <form:password path="password" id="password" cssClass="login-input login-input-password" placeholder="********" />
          </div>
          <form:errors path="password" cssStyle="color:#e5484d;font-size:.8rem;" />
        </div>

        <button type="submit" class="btn-login" id="btn-submit">
          <span>Dang nhap</span>
          <i class="bi bi-arrow-right"></i>
        </button>
      </form:form>

      <p class="login-footer-text">
        Chua co tai khoan? <a href="${ctx}/register">Dang ky ngay</a>
      </p>
      <p class="login-footer-text">
        <a href="${ctx}/home"><i class="bi bi-arrow-left"></i> Ve trang chu</a>
      </p>
    </div>
  </div>

  <script src="${ctx}/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>

</html>
