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
  <div class="d-flex align-items-center gap-3 mb-4">
    <img src="${ctx}/image/${currentImage}" alt="" style="width:56px;height:56px;object-fit:contain;">
    <h5 class="card-title mb-0">Sua danh muc</h5>
  </div>

  <form:form modelAttribute="category" action="${ctx}/admin/categories/saveOrUpdate" method="post" enctype="multipart/form-data">
    <form:hidden path="isEdit" />
    <form:hidden path="categoryId" />

    <div class="row g-3">
      <div class="col-md-8">
        <label class="form-label-custom">Ten danh muc</label>
        <form:input path="categoryName" cssClass="form-control-custom" />
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
        <label class="form-label-custom">Doi icon (bo trong neu giu nguyen)</label>
        <input type="file" name="images" accept="image/*" class="form-control-custom">
      </div>
    </div>

    <div class="d-flex gap-2 mt-4">
      <button type="submit" class="btn-custom btn-custom-primary">
        <i class="bi bi-check-lg"></i> Cap nhat
      </button>
      <a href="${ctx}/admin/categories" class="btn-custom btn-custom-outline-primary">Huy</a>
    </div>
  </form:form>
</div>

</body>
</html>
