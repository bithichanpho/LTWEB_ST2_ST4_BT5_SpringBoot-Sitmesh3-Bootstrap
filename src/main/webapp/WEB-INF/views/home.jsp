<%@ include file="/WEB-INF/common/head.jsp"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<style>
.shop-product-card {
	transition: transform .15s ease, box-shadow .15s ease;
	height: 100%;
}

.shop-product-card:hover {
	transform: translateY(-4px);
	box-shadow: 0 12px 24px rgba(7, 47, 31, .12);
}

.shop-product-img-wrap {
	aspect-ratio: 1/1;
	overflow: hidden;
	border-radius: 12px;
	background: #F3F6F4;
	margin-bottom: .75rem;
}

.shop-product-img-wrap img {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

.shop-product-name {
	font-weight: 600;
	color: var(--brand-forest-medium);
	font-size: .95rem;
	min-height: 2.4em;
}

.shop-product-price {
	font-weight: 700;
	color: var(--brand-forest-medium);
	font-size: 1.05rem;
}

.shop-hero {
	background: var(--brand-forest-dark);
	border-radius: 16px;
	padding: 2.5rem;
	color: #fff;
	margin-bottom: 1.5rem;
}

.shop-hero h2 {
	font-weight: 700;
	color: #fff;
}
</style>

<div
	class="shop-hero d-flex flex-wrap align-items-center justify-content-between gap-3">
	<div>
		<h2 class="mb-2">Chao mung den voi SHOP</h2>
		<p class="mb-0" style="color: var(--brand-lime);">Kham pha bo suu
			tap Quan ao nam, Quan ao nu va Phu kien moi nhat.</p>
	</div>
	<a href="${ctx}/product"
		class="btn-custom btn-custom-primary btn-custom-lg"> Xem tat ca
		san pham <i class="bi bi-arrow-right"></i>
	</a>
</div>

<div class="row g-3 mb-4">
	<c:forEach items="${navCategories}" var="nc">
		<div class="col-md-4">
			<a href="${ctx}/category/detail?id=${nc.categoryId}"
				class="text-decoration-none">
				<div class="card d-flex flex-row align-items-center gap-3 p-3 h-100">
					<img src="${ctx}/image/${nc.images}" alt="${nc.categoryName}"
						style="width: 56px; height: 56px; object-fit: contain;">
					<div>
						<div class="fw-bold" style="color: var(--brand-forest-medium);">${nc.categoryName}</div>
						<div class="text-muted-green small">${navCategoryCounts[nc.categoryId]}
							san pham</div>
					</div>
				</div>
			</a>
		</div>
	</c:forEach>
</div>

<div class="card">
	<div class="card-header mb-3">
		<h2 class="card-title">San pham moi nhat</h2>
		<a href="${ctx}/product" class="btn-table-action">Xem tat ca</a>
	</div>

	<div class="row g-3">
		<c:forEach items="${latestProducts}" var="p">
			<div class="col-6 col-md-4 col-lg-3">
				<a href="${ctx}/product/detail?id=${p.productId}"
					class="text-decoration-none">
					<div class="shop-product-card card p-2">
						<div class="shop-product-img-wrap">
							<img src="${ctx}/image/${p.images}" alt="${p.productName}">
						</div>
						<div class="shop-product-name">${p.productName}</div>
						<div class="shop-product-price mt-1">
							<fmt:formatNumber value="${p.price}" type="number"
								groupingUsed="true" />
							d
						</div>
					</div>
				</a>
			</div>
		</c:forEach>
		<c:if test="${empty latestProducts}">
			<div class="col-12 text-center text-muted-green py-4">Chua co
				san pham nao.</div>
		</c:if>
	</div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp"%>
