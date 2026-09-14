<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

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

      <div class="mb-2">
        Trang thai:
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
        (<c:out value="${order.paymentMethod == 'COD' ? 'Thanh toan khi nhan hang' : 'Chuyen khoan'}" />)
      </div>

      <hr>
      <div class="mb-1"><strong>Nguoi nhan:</strong> ${order.recipientName}</div>
      <div class="mb-1"><strong>SDT:</strong> ${order.phone}</div>
      <div class="mb-1"><strong>Dia chi:</strong> ${order.address}</div>
      <c:if test="${not empty order.note}">
        <div class="mb-1"><strong>Ghi chu:</strong> ${order.note}</div>
      </c:if>

      <a href="${ctx}/order/history" class="btn-custom btn-custom-outline-primary mt-3">
        <i class="bi bi-arrow-left"></i> Quay lai lich su don hang
      </a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
