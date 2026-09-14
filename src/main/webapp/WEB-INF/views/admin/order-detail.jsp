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
<c:if test="${not empty flashError}">
  <div class="alert-custom alert-custom-danger mb-3">
    <i class="bi bi-exclamation-triangle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${flashError}</div>
  </div>
</c:if>

<div class="row g-4">
  <div class="col-lg-8">
    <div class="table-card-custom">
      <div class="table-responsive">
        <table class="table-custom">
          <thead>
            <tr>
              <th>San pham</th>
              <th>Don gia</th>
              <th>So luong</th>
              <th>Thanh tien</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${orderDetails}" var="d">
              <tr>
                <td class="table-product-name">${d.productName}</td>
                <td class="table-amount"><fmt:formatNumber value="${d.price}" type="number" groupingUsed="true" /> d</td>
                <td>${d.quantity}</td>
                <td class="table-amount"><fmt:formatNumber value="${d.subtotal}" type="number" groupingUsed="true" /> d</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end p-3 border-top" style="font-size:1.1rem;">
        Tong tien don hang:&nbsp;<strong style="color: var(--brand-forest-medium);">
          <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" /> d
        </strong>
      </div>
    </div>
  </div>

  <div class="col-lg-4">
    <div class="card p-4">
      <h5 class="mb-3" style="color: var(--brand-forest-medium); font-weight:700;">Don hang #${order.orderId}</h5>
      <div class="mb-2 text-muted-green small">Ngay dat: ${order.orderDateFormatted}</div>

      <div class="mb-1"><strong>Khach hang:</strong> ${order.user.fullname} (${order.user.email})</div>
      <div class="mb-1"><strong>Nguoi nhan:</strong> ${order.recipientName}</div>
      <div class="mb-1"><strong>SDT:</strong> ${order.phone}</div>
      <div class="mb-1"><strong>Dia chi:</strong> ${order.address}</div>
      <c:if test="${not empty order.note}">
        <div class="mb-1"><strong>Ghi chu:</strong> ${order.note}</div>
      </c:if>

      <hr>

      <div class="mb-2">
        Trang thai hien tai:
        <c:choose>
          <c:when test="${order.status == 'PENDING'}"><span class="badge-table pending">Cho xac nhan</span></c:when>
          <c:when test="${order.status == 'CONFIRMED'}"><span class="badge-table pending">Da xac nhan</span></c:when>
          <c:when test="${order.status == 'SHIPPING'}"><span class="badge-table pending">Dang giao</span></c:when>
          <c:when test="${order.status == 'COMPLETED'}"><span class="badge-table success">Hoan thanh</span></c:when>
          <c:when test="${order.status == 'CANCELLED'}"><span class="badge-table failed">Da huy</span></c:when>
        </c:choose>
      </div>
      <div class="mb-3">
        Thanh toan:
        <c:choose>
          <c:when test="${order.paid}"><span class="badge-table success">Da thanh toan</span></c:when>
          <c:otherwise><span class="badge-table pending">Chua thanh toan</span></c:otherwise>
        </c:choose>
      </div>

      <c:if test="${order.status != 'COMPLETED' && order.status != 'CANCELLED'}">
        <div class="d-flex flex-column gap-2">
          <c:if test="${order.status == 'PENDING'}">
            <form action="${ctx}/admin/order/updateStatus" method="post">
              <input type="hidden" name="orderId" value="${order.orderId}">
              <input type="hidden" name="status" value="CONFIRMED">
              <button type="submit" class="btn-custom btn-custom-primary w-100">Xac nhan don hang</button>
            </form>
          </c:if>
          <c:if test="${order.status == 'CONFIRMED'}">
            <form action="${ctx}/admin/order/updateStatus" method="post">
              <input type="hidden" name="orderId" value="${order.orderId}">
              <input type="hidden" name="status" value="SHIPPING">
              <button type="submit" class="btn-custom btn-custom-primary w-100">Chuyen giao hang</button>
            </form>
          </c:if>
          <c:if test="${order.status == 'SHIPPING'}">
            <form action="${ctx}/admin/order/updateStatus" method="post">
              <input type="hidden" name="orderId" value="${order.orderId}">
              <input type="hidden" name="status" value="COMPLETED">
              <button type="submit" class="btn-custom btn-custom-primary w-100">Xac nhan da giao xong</button>
            </form>
          </c:if>
          <c:if test="${not order.paid}">
            <form action="${ctx}/admin/order/markPaid" method="post">
              <input type="hidden" name="orderId" value="${order.orderId}">
              <button type="submit" class="btn-custom btn-custom-outline-primary w-100">Xac nhan da thanh toan</button>
            </form>
          </c:if>
          <form action="${ctx}/admin/order/updateStatus" method="post"
            onsubmit="return confirm('Huy don hang nay? Ton kho se duoc hoan lai.');">
            <input type="hidden" name="orderId" value="${order.orderId}">
            <input type="hidden" name="status" value="CANCELLED">
            <button type="submit" class="btn-custom btn-custom-outline-danger w-100">Huy don hang</button>
          </form>
        </div>
      </c:if>

      <a href="${ctx}/admin/orders" class="btn-custom btn-custom-outline-primary mt-3 w-100">
        <i class="bi bi-arrow-left"></i> Quay lai danh sach
      </a>
    </div>
  </div>
</div>

</body>
</html>
