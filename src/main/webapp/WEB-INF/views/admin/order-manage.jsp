<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<html>
<head>
<title>${pageTitle}</title>
</head>
<body>
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
      <input type="text" class="table-search-input" id="orderSearch" placeholder="Tim theo ma don...">
    </div>
    <div class="table-filter-group">
      <a href="${ctx}/admin/orders" class="btn-custom btn-custom-sm ${empty status ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Tat ca</a>
      <a href="${ctx}/admin/orders?status=PENDING" class="btn-custom btn-custom-sm ${status == 'PENDING' ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Cho xac nhan</a>
      <a href="${ctx}/admin/orders?status=CONFIRMED" class="btn-custom btn-custom-sm ${status == 'CONFIRMED' ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Da xac nhan</a>
      <a href="${ctx}/admin/orders?status=SHIPPING" class="btn-custom btn-custom-sm ${status == 'SHIPPING' ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Dang giao</a>
      <a href="${ctx}/admin/orders?status=COMPLETED" class="btn-custom btn-custom-sm ${status == 'COMPLETED' ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Hoan thanh</a>
      <a href="${ctx}/admin/orders?status=CANCELLED" class="btn-custom btn-custom-sm ${status == 'CANCELLED' ? 'btn-custom-primary' : 'btn-custom-outline-primary'}">Da huy</a>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table-custom" id="orderTable">
      <thead>
        <tr>
          <th>Ma don</th>
          <th>Khach hang</th>
          <th>Ngay dat</th>
          <th>Tong tien</th>
          <th>Thanh toan</th>
          <th>Trang thai</th>
          <th class="text-center">Hanh dong</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${orderList}" var="o">
          <tr>
            <td class="table-order-id">#${o.orderId}</td>
            <td>${o.recipientName} <div class="text-muted-green small">${o.phone}</div></td>
            <td>${o.orderDateFormatted}</td>
            <td class="table-amount"><fmt:formatNumber value="${o.totalAmount}" type="number" groupingUsed="true" /> d</td>
            <td>
              <c:choose>
                <c:when test="${o.paid}"><span class="badge-table success">Da thanh toan</span></c:when>
                <c:otherwise><span class="badge-table pending">Chua thanh toan</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${o.status == 'PENDING'}"><span class="badge-table pending">Cho xac nhan</span></c:when>
                <c:when test="${o.status == 'CONFIRMED'}"><span class="badge-table pending">Da xac nhan</span></c:when>
                <c:when test="${o.status == 'SHIPPING'}"><span class="badge-table pending">Dang giao</span></c:when>
                <c:when test="${o.status == 'COMPLETED'}"><span class="badge-table success">Hoan thanh</span></c:when>
                <c:when test="${o.status == 'CANCELLED'}"><span class="badge-table failed">Da huy</span></c:when>
              </c:choose>
            </td>
            <td>
              <div class="d-flex justify-content-center">
                <a href="${ctx}/admin/order/detail/${o.orderId}" class="table-btn-action" title="Xu ly don hang"><i class="bi bi-eye"></i></a>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty orderList}">
          <tr><td colspan="7" class="text-center text-muted-green py-4">Khong co don hang nao.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<c:if test="${totalPages > 1}">
  <nav class="mt-4">
    <ul class="pagination justify-content-center">
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="${ctx}/admin/orders?page=${i}&status=${status}">${i}</a>
        </li>
      </c:forEach>
    </ul>
  </nav>
</c:if>

<script>
  document.getElementById('orderSearch').addEventListener('input', function () {
    var q = this.value.toLowerCase();
    document.querySelectorAll('#orderTable tbody tr').forEach(function (row) {
      row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
    });
  });
</script>

</body>
</html>
