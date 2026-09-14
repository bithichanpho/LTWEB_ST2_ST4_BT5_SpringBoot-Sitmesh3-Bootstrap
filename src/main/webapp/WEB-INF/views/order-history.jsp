<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="table-card-custom">
  <div class="table-responsive">
    <table class="table-custom">
      <thead>
        <tr>
          <th>Ma don</th>
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
                <c:otherwise>${o.status}</c:otherwise>
              </c:choose>
            </td>
            <td>
              <div class="d-flex justify-content-center">
                <a href="${ctx}/order/detail/${o.orderId}" class="table-btn-action" title="Xem chi tiet"><i class="bi bi-eye"></i></a>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty orderList}">
          <tr><td colspan="6" class="text-center text-muted-green py-4">Ban chua co don hang nao.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
