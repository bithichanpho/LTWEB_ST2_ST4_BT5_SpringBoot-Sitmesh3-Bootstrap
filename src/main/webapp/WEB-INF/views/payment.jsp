<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="row justify-content-center">
  <div class="col-lg-7">
    <div class="card p-4">
      <h5 class="mb-2" style="color: var(--brand-forest-medium); font-weight:700;">Thanh toan don hang #${order.orderId}</h5>
      <p class="text-muted-green mb-4">
        Phuong thuc: Chuyen khoan ngan hang. Day la man hinh mo phong cong thanh toan de kiem tra dung luong Sequence Diagram.
      </p>

      <div class="d-flex justify-content-between mb-2">
        <span>Tong tien can thanh toan</span>
        <strong style="color: var(--brand-forest-medium);">
          <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" /> d
        </strong>
      </div>
      <div class="d-flex justify-content-between mb-3">
        <span>Trang thai giao dich truoc do</span>
        <c:choose>
          <c:when test="${order.paymentStatus == 'FAILED'}"><span class="badge-table failed">Thanh toan that bai</span></c:when>
          <c:otherwise><span class="badge-table pending">Chua thanh toan</span></c:otherwise>
        </c:choose>
      </div>

      <hr>

      <div class="d-grid gap-2">
        <form action="${ctx}/order/payment/${order.orderId}" method="post">
          <input type="hidden" name="result" value="SUCCESS">
          <button type="submit" class="btn-custom btn-custom-primary w-100">
            <i class="bi bi-check-circle"></i> Mo phong thanh toan thanh cong
          </button>
        </form>

        <form action="${ctx}/order/payment/${order.orderId}" method="post">
          <input type="hidden" name="result" value="FAILED">
          <button type="submit" class="btn-custom btn-custom-outline-danger w-100">
            <i class="bi bi-x-circle"></i> Mo phong thanh toan that bai
          </button>
        </form>
      </div>

      <a href="${ctx}/order/detail/${order.orderId}" class="btn-custom btn-custom-outline-primary mt-3 w-100">
        <i class="bi bi-arrow-left"></i> Quay lai don hang
      </a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
