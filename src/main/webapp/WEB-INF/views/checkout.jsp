<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="row g-4">
  <div class="col-lg-7">
    <div class="card p-4">
      <h5 class="mb-3" style="color: var(--brand-forest-medium); font-weight:700;">Thong tin giao hang</h5>
      <form action="${ctx}/order/place" method="post" accept-charset="UTF-8">
        <div class="mb-3">
          <label class="form-label">Ho ten nguoi nhan</label>
          <input type="text" name="recipientName" class="form-control" value="${currentUser.fullname}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">So dien thoai</label>
          <input type="text" name="phone" class="form-control" value="${currentUser.phone}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Dia chi giao hang</label>
          <textarea name="address" class="form-control" rows="2" autocomplete="off" required></textarea>
        </div>
        <div class="mb-3">
          <label class="form-label">Ghi chu (khong bat buoc)</label>
          <textarea name="note" class="form-control" rows="2"></textarea>
        </div>
        <div class="mb-4">
          <label class="form-label d-block">Phuong thuc thanh toan</label>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="paymentMethod" id="payCod" value="COD" checked>
            <label class="form-check-label" for="payCod">Thanh toan khi nhan hang (COD)</label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="paymentMethod" id="payBanking" value="BANKING">
            <label class="form-check-label" for="payBanking">Chuyen khoan ngan hang</label>
          </div>
        </div>
        <button type="submit" class="btn-custom btn-custom-primary w-100">
          <i class="bi bi-bag-check"></i> Dat hang
        </button>
      </form>
    </div>
  </div>

  <div class="col-lg-5">
    <div class="card p-4">
      <h5 class="mb-3" style="color: var(--brand-forest-medium); font-weight:700;">Don hang cua ban</h5>
      <c:forEach items="${cartItems}" var="item">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div>
            <div class="table-product-name">${item.product.productName}</div>
            <div class="text-muted-green small">SL: ${item.quantity}</div>
          </div>
          <div class="table-amount"><fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" /> d</div>
        </div>
      </c:forEach>
      <hr>
      <div class="d-flex justify-content-between" style="font-size:1.1rem;">
        <span>Tong tien</span>
        <strong style="color: var(--brand-forest-medium);">
          <fmt:formatNumber value="${cartTotal}" type="number" groupingUsed="true" /> d
        </strong>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
