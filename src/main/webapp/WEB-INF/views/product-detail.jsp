<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="card p-4">
  <div class="row g-4">
    <div class="col-md-5">
      <img src="${ctx}/image/${product.images}" alt="${product.productName}"
        style="width:100%;aspect-ratio:1/1;object-fit:cover;border-radius:14px;background:#F3F6F4;">
    </div>
    <div class="col-md-7">
      <div class="text-muted-green small mb-1">${product.category.categoryName}</div>
      <h2 class="mb-2" style="color: var(--brand-forest-medium); font-weight:700;">${product.productName}</h2>
      <div class="mb-3" style="font-size:1.6rem; font-weight:700; color: var(--brand-forest-medium);">
        <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /> d
      </div>

      <div class="mb-3">
        <c:choose>
          <c:when test="${product.quantity <= 0}">
            <span class="badge-table failed">Het hang</span>
          </c:when>
          <c:otherwise>
            <span class="badge-table success">Con hang (${product.quantity})</span>
          </c:otherwise>
        </c:choose>
      </div>

      <p class="text-muted-green" style="white-space: pre-line;">${product.description}</p>

      <c:if test="${product.quantity > 0}">
        <form action="${ctx}/cart/add" method="post" class="d-flex align-items-center gap-2 mb-3">
          <input type="hidden" name="productId" value="${product.productId}">
          <input type="hidden" name="redirect" value="${ctx}/product/detail?id=${product.productId}">
          <input type="number" name="quantity" value="1" min="1" max="${product.quantity}"
            class="form-control" style="width:90px;">
          <button type="submit" class="btn-custom btn-custom-primary">
            <i class="bi bi-cart-plus"></i> Them vao gio hang
          </button>
        </form>
      </c:if>

      <a href="${ctx}/category/detail?id=${product.category.categoryId}" class="btn-custom btn-custom-outline-primary mt-3">
        <i class="bi bi-arrow-left"></i> Xem them san pham cung danh muc
      </a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
