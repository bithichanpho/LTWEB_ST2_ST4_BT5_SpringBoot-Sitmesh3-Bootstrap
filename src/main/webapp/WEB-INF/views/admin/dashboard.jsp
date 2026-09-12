<%@ include file="/WEB-INF/common/head.jsp" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="row g-4 mb-1">
  <div class="col-md-3">
    <div class="card card-stat">
      <div class="card-header">
        <span class="stat-label">Tong doanh thu</span>
      </div>
      <div class="stat-value"><fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" /> d</div>
    </div>
  </div>
  <div class="col-md-3">
    <div class="card card-stat">
      <div class="card-header">
        <span class="stat-label">San pham da ban</span>
      </div>
      <div class="stat-value">${totalSold}</div>
    </div>
  </div>
  <div class="col-md-3">
    <div class="card card-stat">
      <div class="card-header">
        <span class="stat-label">Tong so san pham</span>
      </div>
      <div class="stat-value">${totalProducts}</div>
    </div>
  </div>
  <div class="col-md-3">
    <div class="card card-stat">
      <div class="card-header">
        <span class="stat-label">Danh muc</span>
      </div>
      <div class="stat-value">${totalCategories}</div>
    </div>
  </div>
</div>

<div class="row g-4 mt-1">
  <div class="col-lg-6">
    <div class="card p-3 h-100">
      <h5 class="card-title mb-3">Doanh thu theo danh muc</h5>
      <div id="revenue-by-category-chart"></div>
    </div>
  </div>

  <div class="col-lg-6">
    <div class="card p-3 h-100">
      <h5 class="card-title mb-3">Top 5 san pham ban chay</h5>
      <div class="table-responsive">
        <table class="table-custom">
          <thead>
            <tr><th>San pham</th><th>Da ban</th><th>Doanh thu</th></tr>
          </thead>
          <tbody>
            <c:forEach items="${topProducts}" var="p">
              <tr>
                <td class="table-product-name">${p.productName}</td>
                <td>${p.sold}</td>
                <td class="table-amount"><fmt:formatNumber value="${p.revenue}" type="number" groupingUsed="true" /> d</td>
              </tr>
            </c:forEach>
            <c:if test="${empty topProducts}">
              <tr><td colspan="3" class="text-center text-muted-green py-3">Chua co du lieu ban hang.</td></tr>
            </c:if>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<div class="table-card-custom mt-4">
  <div class="table-header-control">
    <h5 class="card-title mb-0">Hieu qua theo danh muc</h5>
  </div>
  <div class="table-responsive">
    <table class="table-custom">
      <thead>
        <tr><th>Danh muc</th><th>So san pham</th><th>Da ban</th><th>Doanh thu</th><th>Ty trong</th></tr>
      </thead>
      <tbody>
        <c:forEach items="${categoryStats}" var="s">
          <tr>
            <td class="table-product-name">${s.category.categoryName}</td>
            <td>${s.productCount}</td>
            <td>${s.totalSold}</td>
            <td class="table-amount"><fmt:formatNumber value="${s.totalRevenue}" type="number" groupingUsed="true" /> d</td>
            <td><fmt:formatNumber value="${s.revenuePercent}" maxFractionDigits="1" />%</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<script>
  var chartEl = document.querySelector("#revenue-by-category-chart");
  if (chartEl && window.ApexCharts) {
    var categories = [
      <c:forEach items="${categoryStats}" var="s" varStatus="st">"${s.category.categoryName}"<c:if test="${!st.last}">,</c:if></c:forEach>
    ];
    var revenues = [
      <c:forEach items="${categoryStats}" var="s" varStatus="st">${s.totalRevenue}<c:if test="${!st.last}">,</c:if></c:forEach>
    ];

    new ApexCharts(chartEl, {
      chart: { type: 'bar', height: 300, toolbar: { show: false } },
      series: [{ name: 'Doanh thu', data: revenues }],
      xaxis: { categories: categories },
      colors: ['#072F1F'],
      plotOptions: { bar: { borderRadius: 6, columnWidth: '45%' } },
      dataLabels: { enabled: false }
    }).render();
  }
</script>

<%@ include file="/WEB-INF/common/foot.jsp" %>
