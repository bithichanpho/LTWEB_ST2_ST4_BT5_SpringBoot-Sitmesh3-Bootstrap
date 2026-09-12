<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<c:if test="${not empty message}">
  <div class="alert-custom alert-custom-success mb-3">
    <i class="bi bi-check-circle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${message}</div>
  </div>
</c:if>

<div class="table-card-custom">
  <div class="table-header-control">
    <div class="table-search-box">
      <i class="bi bi-search table-search-icon"></i>
      <input type="text" class="table-search-input" id="productSearch" placeholder="Tim san pham...">
    </div>
    <div class="table-filter-group">
      <a href="${ctx}/admin/product/add" class="btn-custom btn-custom-primary btn-custom-sm">
        <i class="bi bi-plus-lg"></i> Them san pham
      </a>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table-custom" id="productTable">
      <thead>
        <tr>
          <th>Ma SP</th>
          <th>Hinh anh</th>
          <th>Ten san pham</th>
          <th>Danh muc</th>
          <th>Gia</th>
          <th>Ton kho</th>
          <th>Da ban</th>
          <th class="text-center">Hanh dong</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${productList}" var="p">
          <tr>
            <td class="table-order-id">${p.productId}</td>
            <td><img src="${ctx}/image/${p.images}" alt="${p.productName}" style="width:44px;height:44px;object-fit:cover;border-radius:8px;"></td>
            <td class="table-product-name">${p.productName}</td>
            <td>${p.category.categoryName}</td>
            <td class="table-amount"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true" /> d</td>
            <td>
              <c:choose>
                <c:when test="${p.quantity <= 0}"><span class="badge-table failed">Het hang</span></c:when>
                <c:when test="${p.quantity < 10}"><span class="badge-table pending">${p.quantity}</span></c:when>
                <c:otherwise><span class="badge-table success">${p.quantity}</span></c:otherwise>
              </c:choose>
            </td>
            <td>${p.sold}</td>
            <td>
              <div class="d-flex justify-content-center gap-1">
                <a href="${ctx}/product/detail?id=${p.productId}" class="table-btn-action" title="Xem"><i class="bi bi-eye"></i></a>
                <a href="${ctx}/admin/product/edit/${p.productId}" class="table-btn-action" title="Sua"><i class="bi bi-pencil"></i></a>
                <a href="${ctx}/admin/product/delete/${p.productId}" class="table-btn-action delete" title="Xoa"
                  onclick="return confirm('Xoa san pham \'${p.productName}\'?');"><i class="bi bi-trash"></i></a>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty productList}">
          <tr><td colspan="8" class="text-center text-muted-green py-4">Chua co san pham nao.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<script>
  document.getElementById('productSearch').addEventListener('input', function () {
    var q = this.value.toLowerCase();
    document.querySelectorAll('#productTable tbody tr').forEach(function (row) {
      row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
    });
  });
</script>

<%@ include file="/WEB-INF/common/foot.jsp" %>
