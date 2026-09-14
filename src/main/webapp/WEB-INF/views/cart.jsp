<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="table-card-custom">
  <div class="table-responsive">
    <table class="table-custom">
      <thead>
        <tr>
          <th>San pham</th>
          <th>Don gia</th>
          <th>So luong</th>
          <th>Thanh tien</th>
          <th class="text-center">Hanh dong</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${cartItems}" var="item">
          <tr>
            <td>
              <div class="d-flex align-items-center gap-2">
                <img src="${ctx}/image/${item.product.images}" alt="${item.product.productName}"
                  style="width:44px;height:44px;object-fit:cover;border-radius:8px;">
                <span class="table-product-name">${item.product.productName}</span>
              </div>
            </td>
            <td class="table-amount"><fmt:formatNumber value="${item.product.price}" type="number" groupingUsed="true" /> d</td>
            <td>
              <form action="${ctx}/cart/update" method="post" class="d-flex align-items-center gap-1">
                <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                <input type="number" name="quantity" value="${item.quantity}" min="1"
                  max="${item.product.quantity}" class="form-control form-control-sm" style="width:70px;">
                <button type="submit" class="table-btn-action" title="Cap nhat"><i class="bi bi-arrow-repeat"></i></button>
              </form>
            </td>
            <td class="table-amount"><fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" /> d</td>
            <td>
              <div class="d-flex justify-content-center">
                <form action="${ctx}/cart/remove" method="post" onsubmit="return confirm('Xoa san pham nay khoi gio hang?');">
                  <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                  <button type="submit" class="table-btn-action delete" title="Xoa"><i class="bi bi-trash"></i></button>
                </form>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty cartItems}">
          <tr><td colspan="5" class="text-center text-muted-green py-4">Gio hang dang trong.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <c:if test="${not empty cartItems}">
    <div class="d-flex justify-content-end align-items-center gap-3 p-3 border-top">
      <div style="font-size:1.1rem;">
        Tong tien: <strong style="color: var(--brand-forest-medium);">
          <fmt:formatNumber value="${cartTotal}" type="number" groupingUsed="true" /> d
        </strong>
      </div>
      <a href="${ctx}/checkout" class="btn-custom btn-custom-primary">
        <i class="bi bi-bag-check"></i> Tien hanh dat hang
      </a>
    </div>
  </c:if>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
