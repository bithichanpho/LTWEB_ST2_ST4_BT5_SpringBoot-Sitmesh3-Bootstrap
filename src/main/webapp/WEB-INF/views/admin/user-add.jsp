<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>${pageTitle}</title>
</head>
<body>

<c:if test="${not empty message}">
  <div class="alert-custom alert-custom-danger mb-3">
    <i class="bi bi-exclamation-triangle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${message}</div>
  </div>
</c:if>

<div class="card border-light shadow-sm p-4">
  <h5 class="card-title mb-4">Thong tin nguoi dung</h5>

  <form:form modelAttribute="userForm" action="${ctx}/admin/users/saveOrUpdate" method="post">
    <form:hidden path="isEdit" />

    <div class="row g-3">
      <div class="col-md-6">
        <label class="form-label-custom">Ho ten</label>
        <form:input path="fullname" cssClass="form-control-custom" placeholder="Vd: Nguyen Van A" />
        <form:errors path="fullname" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Email</label>
        <form:input path="email" type="email" cssClass="form-control-custom" placeholder="vd: a@example.com" />
        <form:errors path="email" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Mat khau</label>
        <form:password path="password" cssClass="form-control-custom" placeholder="Toi thieu 6 ky tu" autocomplete="new-password" />
        <form:errors path="password" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Dien thoai</label>
        <form:input path="phone" cssClass="form-control-custom" placeholder="Vd: 0901234567" />
        <form:errors path="phone" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Vai tro</label>
        <form:select path="role" cssClass="form-select-custom">
          <form:option value="user" label="User" />
          <form:option value="admin" label="Admin" />
        </form:select>
        <form:errors path="role" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Trang thai</label>
        <form:select path="status" cssClass="form-select-custom">
          <form:option value="1" label="Kich hoat" />
          <form:option value="0" label="Chua kich hoat" />
        </form:select>
        <form:errors path="status" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>
    </div>

    <div class="d-flex gap-2 mt-4">
      <button type="submit" class="btn-custom btn-custom-primary">
        <i class="bi bi-check-lg"></i> Luu nguoi dung
      </button>
      <a href="${ctx}/admin/users" class="btn-custom btn-custom-outline-primary">Huy</a>
    </div>
  </form:form>
</div>

</body>
</html>