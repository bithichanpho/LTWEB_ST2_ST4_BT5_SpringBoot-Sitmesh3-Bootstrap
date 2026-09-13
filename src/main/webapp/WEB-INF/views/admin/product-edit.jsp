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
    <img src="${ctx}/image/${currentImage}" alt="" style="width:64px;height:64px;object-fit:cover;border-radius:12px;">
    <div>
      <h5 class="card-title mb-0">Sua san pham</h5>
      <div class="text-muted-green small">Ma san pham: ${product.productId}</div>
    </div>
  </div>

  <form:form modelAttribute="product" action="${ctx}/admin/product/saveOrUpdate" method="post" enctype="multipart/form-data">
    <form:hidden path="isEdit" />
    <form:hidden path="productId" />

    <div class="row g-3">
      <div class="col-md-8">
        <label class="form-label-custom">Ten san pham</label>
        <form:input path="productName" cssClass="form-control-custom" />
        <form:errors path="productName" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Danh muc</label>
        <form:select path="categoryId" cssClass="form-select-custom">
          <form:options items="${cateList}" itemValue="categoryId" itemLabel="categoryName" />
        </form:select>
        <form:errors path="categoryId" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Gia (VND)</label>
        <form:input path="price" type="number" step="1000" min="0" cssClass="form-control-custom" />
        <form:errors path="price" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">So luong ton kho</label>
        <form:input path="quantity" type="number" min="0" cssClass="form-control-custom" />
        <form:errors path="quantity" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Doi hinh anh (bo trong neu giu nguyen)</label>
        <input type="file" name="images" accept="image/*" class="form-control-custom">
      </div>

      <div class="col-12">
        <label class="form-label-custom">Mo ta</label>
        <form:textarea path="description" rows="4" cssClass="form-control-custom" />
        <form:errors path="description" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>
    </div>

    <div class="d-flex gap-2 mt-4">
      <button type="submit" class="btn-custom btn-custom-primary">
        <i class="bi bi-check-lg"></i> Cap nhat
      </button>
      <a href="${ctx}/admin/products" class="btn-custom btn-custom-outline-primary">Huy</a>
    </div>
  </form:form>
</div>

</body>
</html>
