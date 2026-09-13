<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<style>
  .shop-product-card { transition: transform .15s ease, box-shadow .15s ease; height: 100%; }
  .shop-product-card:hover { transform: translateY(-4px); box-shadow: 0 12px 24px rgba(7,47,31,.12); }
  .shop-product-img-wrap { aspect-ratio: 1/1; overflow: hidden; border-radius: 12px; background: #F3F6F4; margin-bottom: .75rem; }
  .shop-product-img-wrap img { width: 100%; height: 100%; object-fit: cover; }
  .shop-product-name { font-weight: 600; color: var(--brand-forest-medium); font-size: .95rem; min-height: 2.4em; }
  .shop-product-price { font-weight: 700; color: var(--brand-forest-medium); font-size: 1.05rem; }
</style>

<div class="row g-3">
  <c:forEach items="${productList}" var="p">
    <div class="col-6 col-md-4 col-lg-3">
      <a href="${ctx}/product/detail?id=${p.productId}" class="text-decoration-none">
        <div class="shop-product-card card p-2">
          <div class="shop-product-img-wrap">
            <img src="${ctx}/image/${p.images}" alt="${p.productName}">
          </div>
          <div class="shop-product-name">${p.productName}</div>
          <div class="shop-product-price mt-1">
            <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true" /> d
          </div>
        </div>
      </a>
    </div>
  </c:forEach>
  <c:if test="${empty productList}">
    <div class="col-12 text-center text-muted-green py-4">
      <c:choose>
        <c:when test="${not empty keyword}">Khong tim thay san pham nao khop voi "${keyword}".</c:when>
        <c:otherwise>Chua co san pham nao.</c:otherwise>
      </c:choose>
    </div>
  </c:if>
</div>

<c:if test="${totalPages > 1}">
  <nav class="mt-4">
    <ul class="pagination justify-content-center">
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="${ctx}/product?page=${i}&keyword=${keyword}">${i}</a>
        </li>
      </c:forEach>
    </ul>
  </nav>
</c:if>

<%@ include file="/WEB-INF/common/foot.jsp" %>
