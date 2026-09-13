<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<html>
<head>
<title>${pageTitle}</title>
</head>
<body>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${not empty message}">
  <div class="alert-custom alert-custom-danger mb-3">
    <i class="bi bi-exclamation-triangle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${message}</div>
  </div>
</c:if>

<div class="card border-light shadow-sm p-4">
  <h5 class="card-title mb-4">Thong tin danh muc</h5>

  <form:form modelAttribute="category" action="${ctx}/admin/categories/saveOrUpdate" method="post" enctype="multipart/form-data">
    <form:hidden path="isEdit" />

    <div class="row g-3">
      <div class="col-md-8">
        <label class="form-label-custom">Ten danh muc</label>
        <form:input path="categoryName" cssClass="form-control-custom" placeholder="Vd: Quan ao nam" />
        <form:errors path="categoryName" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Trang thai</label>
        <form:select path="status" cssClass="form-select-custom">
          <form:option value="0" label="Hoat dong" />
          <form:option value="1" label="An" />
        </form:select>
      </div>

      <div class="col-md-6">
        <label class="form-label-custom">Icon danh muc</label>
        <input type="file" name="images" accept="image/*" class="form-control-custom">
      </div>
    </div>

    <div class="d-flex gap-2 mt-4">
      <button type="submit" class="btn-custom btn-custom-primary">
        <i class="bi bi-check-lg"></i> Luu danh muc
      </button>
      <a href="${ctx}/admin/categories" class="btn-custom btn-custom-outline-primary">Huy</a>
    </div>
  </form:form>
</div>

</body>
</html>
