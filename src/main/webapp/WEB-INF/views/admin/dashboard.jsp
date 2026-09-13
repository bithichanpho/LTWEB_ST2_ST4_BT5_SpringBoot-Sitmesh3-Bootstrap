<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="row g-4 mb-1">

	<!-- Tong doanh thu -->
	<div class="col-md-3">
		<div class="card card-stat">
			<div class="card-header">
				<span class="stat-label">Tong doanh thu</span>
			</div>
			<div class="stat-value">
				<fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" /> d
			</div>
		</div>
	</div>

	<!-- San pham da ban -->
	<div class="col-md-3">
		<div class="card card-stat">
			<div class="card-header">
				<span class="stat-label">San pham da ban</span>
			</div>
			<div class="stat-value">${totalSold}</div>
		</div>
	</div>

	<!-- Tong so san pham -->
	<div class="col-md-3">
		<div class="card card-stat">
			<div class="card-header">
				<span class="stat-label">Tong so san pham</span>
			</div>
			<div class="stat-value">${totalProducts}</div>
		</div>
	</div>

	<!-- Danh muc -->
	<div class="col-md-3">
		<div class="card card-stat">
			<div class="card-header">
				<span class="stat-label">Danh muc</span>
			</div>
			<div class="stat-value">${totalCategories}</div>
		</div>
	</div>

</div>

<!-- CHARTS: DOANH THU + HIEU QUA DANH MUC -->
<div class="row g-4 mt-1">

	<!-- Doanh thu theo danh muc -->
	<div class="col-lg-6">
		<div class="card p-3 h-100">
			<h5 class="card-title mb-3">Doanh thu theo danh muc</h5>
			<div id="revenue-by-category-chart"></div>
		</div>
	</div>

	<!-- Hieu qua theo danh muc (pie chart) -->
	<div class="col-lg-6">
		<div class="card p-3 h-100">
			<h5 class="card-title mb-3">Hieu qua theo danh muc</h5>
			<div id="category-performance-chart"></div>
		</div>
	</div>

</div>

<!-- TOP SAN PHAM BAN CHAY -->
<div class="table-card-custom mt-4">

	<div class="table-header-control">
		<h5 class="card-title mb-0">Top 5 san pham ban chay</h5>
	</div>

	<div class="table-responsive">
		<table class="table-custom">
			<thead>
				<tr>
					<th>San pham</th>
					<th>Da ban</th>
					<th>Doanh thu</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${topProducts}" var="p">
					<tr>
						<td class="table-product-name">${p.productName}</td>
						<td>${p.sold}</td>
						<td class="table-amount">
							<fmt:formatNumber value="${p.revenue}" type="number" groupingUsed="true" /> d
						</td>
					</tr>
				</c:forEach>

				<c:if test="${empty topProducts}">
					<tr>
						<td colspan="3" class="text-center text-muted-green py-3">Chua co du lieu ban hang.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>

</div>

<!-- APEX CHART -->
<script>
	document.addEventListener("DOMContentLoaded", function () {

		var chartEl = document.querySelector("#revenue-by-category-chart");
		var pieChartEl = document.querySelector("#category-performance-chart");

		if (typeof ApexCharts === "undefined") {
			console.warn("ApexCharts chua duoc load.");
			return;
		}

		// Du lieu danh muc va doanh thu (dung chung cho ca 2 chart)
		var categories = [
			<c:forEach items="${categoryStats}" var="s" varStatus="st">
				"${s.category.categoryName}"<c:if test="${!st.last}">,</c:if>
			</c:forEach>
		];

		var revenues = [
			<c:forEach items="${categoryStats}" var="s" varStatus="st">
				${s.totalRevenue}<c:if test="${!st.last}">,</c:if>
			</c:forEach>
		];

		// Mau rieng cho tung danh muc, dung chung ca bar chart va pie chart
		var categoryColors = [
			"#2563EB", "#DB2777", "#D97706", "#7C3AED",
			"#059669", "#DC2626", "#0891B2", "#CA8A04"
		];

		var currencyFormatter = function (value) {
			return new Intl.NumberFormat("vi-VN").format(value) + " d";
		};

		// ===== Bar chart: Doanh thu theo danh muc =====
		if (chartEl) {
			if (categories.length === 0) {
				chartEl.innerHTML = '<div class="text-center text-muted-green py-5">Chua co du lieu doanh thu.</div>';
			} else {
				new ApexCharts(chartEl, {
					chart: { type: "bar", height: 300, toolbar: { show: false } },
					series: [{ name: "Doanh thu", data: revenues }],
					xaxis: { categories: categories },
					colors: categoryColors,
					plotOptions: { bar: { borderRadius: 6, columnWidth: "45%", distributed: true } },
					legend: { show: false },
					dataLabels: { enabled: false },
					tooltip: { y: { formatter: currencyFormatter } }
				}).render();
			}
		}

		// ===== Pie chart: Hieu qua theo danh muc =====
		if (pieChartEl) {
			if (categories.length === 0) {
				pieChartEl.innerHTML = '<div class="text-center text-muted-green py-5">Chua co du lieu danh muc.</div>';
			} else {
				new ApexCharts(pieChartEl, {
					chart: { type: "pie", height: 300 },
					series: revenues,
					labels: categories,
					colors: categoryColors,
					legend: { position: "bottom" },
					dataLabels: { formatter: function (val) { return val.toFixed(1) + "%"; } },
					tooltip: { y: { formatter: currencyFormatter } }
				}).render();
			}
		}

	});
</script>