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
  <h5 class="card-title mb-4">Thong tin san pham</h5>

  <form:form modelAttribute="product" action="${ctx}/admin/product/saveOrUpdate" method="post" enctype="multipart/form-data">
    <form:hidden path="isEdit" />

    <div class="row g-3">
      <div class="col-md-8">
        <label class="form-label-custom">Ten san pham</label>
        <form:input path="productName" cssClass="form-control-custom" placeholder="Vd: Ao thun regular fit" />
        <form:errors path="productName" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Danh muc</label>
        <form:select path="categoryId" cssClass="form-select-custom">
          <form:option value="" label="-- Chon danh muc --" />
          <form:options items="${cateList}" itemValue="categoryId" itemLabel="categoryName" />
        </form:select>
        <form:errors path="categoryId" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Gia (VND)</label>
        <form:input path="price" type="number" step="1000" min="0" cssClass="form-control-custom" placeholder="0" />
        <form:errors path="price" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">So luong ton kho</label>
        <form:input path="quantity" type="number" min="0" cssClass="form-control-custom" placeholder="0" />
        <form:errors path="quantity" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>

      <div class="col-md-4">
        <label class="form-label-custom">Hinh anh san pham</label>
        <input type="file" name="images" accept="image/*" class="form-control-custom">
      </div>

      <div class="col-12">
        <label class="form-label-custom">Mo ta</label>
        <form:textarea path="description" rows="4" cssClass="form-control-custom" placeholder="Mo ta chat lieu, kich thuoc, cach bao quan..." />
        <form:errors path="description" cssStyle="color:#e5484d;font-size:.85rem;" />
      </div>
    </div>

    <div class="d-flex gap-2 mt-4">
      <button type="submit" class="btn-custom btn-custom-primary">
        <i class="bi bi-check-lg"></i> Luu san pham
      </button>
      <a href="${ctx}/admin/products" class="btn-custom btn-custom-outline-primary">Huy</a>
    </div>
  </form:form>
</div>

</body>
</html>
