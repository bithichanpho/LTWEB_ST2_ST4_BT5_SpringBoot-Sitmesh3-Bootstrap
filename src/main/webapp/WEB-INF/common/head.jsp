<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="currentUser" value="${sessionScope.currentUser}" />
<c:set var="isAdmin"
	value="${not empty currentUser and currentUser.role == 'admin'}" />
<!DOCTYPE html>
<html lang="vi">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:out value="${pageTitle}" default="SHOP" />&nbsp;-&nbsp;SHOP</title>

<link rel="icon" type="image/png"
	href="${ctx}/assets/images/favicon.ico">

<link rel="stylesheet"
	href="${ctx}/assets/libs/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${ctx}/assets/libs/bootstrap-icons/bootstrap-icons.css">
<link rel="stylesheet"
	href="${ctx}/assets/libs/apexcharts/apexcharts.css">
<link rel="stylesheet"
	href="${ctx}/assets/libs/flatpickr/flatpickr.min.css">
<link rel="stylesheet" href="${ctx}/assets/css/main.css">
<style>
.btn-sidebar-outline {
	background-color: transparent;
	border: 1px solid rgba(255, 255, 255, .5);
	color: #fff;
}

.btn-sidebar-outline:hover {
	background-color: rgba(255, 255, 255, .12);
	border-color: #fff;
	color: #fff;
}
</style>
</head>

<body>

	<!-- ========================== Sidebar ========================== -->
	<div class="sidebar-wrapper" id="sidebar">
		<a href="${ctx}/home" class="sidebar-brand"> <i
			class="bi bi-bag-heart-fill"></i> <span>SHOP</span>
		</a>

		<div class="flex-grow-1 overflow-y-auto">

			<!-- Menu -->
			<div class="sidebar-menu-section">
				<div class="sidebar-menu-title">Menu</div>
				<ul class="sidebar-menu-list">
					<li class="sidebar-menu-item"><a href="${ctx}/home"
						class="sidebar-menu-link ${activeMenu == 'home' ? 'active' : ''}">
							<i class="bi bi-shop"></i> <span>Trang chu</span>
					</a></li>
					<c:if test="${isAdmin}">
						<li class="sidebar-menu-item"><a
							href="${ctx}/admin/dashboard"
							class="sidebar-menu-link ${activeMenu == 'dashboard' ? 'active' : ''}">
								<i class="bi bi-grid-fill"></i> <span>Dashboard</span>
						</a></li>
					</c:if>
					<c:if test="${not empty currentUser}">
						<li class="sidebar-menu-item"><a href="${ctx}/cart"
							class="sidebar-menu-link ${activeMenu == 'cart' ? 'active' : ''}">
								<i class="bi bi-cart3"></i> <span>Gio hang</span> <span
								class="sidebar-menu-badge">${navCartCount}</span>
						</a></li>
						<li class="sidebar-menu-item"><a href="${ctx}/order/history"
							class="sidebar-menu-link ${activeMenu == 'order-history' ? 'active' : ''}">
								<i class="bi bi-receipt"></i> <span>Don hang cua toi</span>
						</a></li>
					</c:if>
				</ul>
			</div>

			<!-- Category -->
			<div class="sidebar-menu-section">
				<div class="sidebar-menu-title">Category</div>
				<ul class="sidebar-menu-list">
					<c:forEach items="${navCategories}" var="nc">
						<c:set var="catMenuKey" value="category-${nc.categoryId}" />
						<li class="sidebar-menu-item"><a
							href="${ctx}/category/detail?id=${nc.categoryId}"
							class="sidebar-menu-link ${activeMenu == catMenuKey ? 'active' : ''}">
								<i class="bi bi-tag"></i> <span>${nc.categoryName}</span> <span
								class="sidebar-menu-badge">${navCategoryCounts[nc.categoryId]}</span>
						</a></li>
					</c:forEach>
					<li class="sidebar-menu-item"><a href="${ctx}/product"
						class="sidebar-menu-link ${activeMenu == 'products' ? 'active' : ''}">
							<i class="bi bi-grid-3x3-gap"></i> <span>Tat ca san pham</span> <span
							class="sidebar-menu-badge">${navTotalProducts}</span>
					</a></li>
				</ul>
			</div>

			<!-- Admin only: product management -->
			<c:if test="${isAdmin}">
				<div class="sidebar-menu-section">
					<div class="sidebar-menu-title">Quan tri</div>
					<ul class="sidebar-menu-list">
						<li class="sidebar-menu-item"><a href="${ctx}/admin/products"
							class="sidebar-menu-link ${activeMenu == 'admin-products' ? 'active' : ''}">
								<i class="bi bi-box-seam-fill"></i> <span>Quan ly san
									pham</span>
						</a></li>
						<li class="sidebar-menu-item"><a
							href="${ctx}/admin/categories"
							class="sidebar-menu-link ${activeMenu == 'admin-categories' ? 'active' : ''}">
								<i class="bi bi-tags-fill"></i> <span>Quan ly danh muc</span>
						</a></li>
						<li class="sidebar-menu-item"><a href="${ctx}/admin/users"
							class="sidebar-menu-link ${activeMenu == 'admin-users' ? 'active' : ''}">
								<i class="bi bi-people-fill"></i> <span>Quan ly nguoi
									dung</span>
						</a></li>
						<li class="sidebar-menu-item"><a href="${ctx}/admin/orders"
							class="sidebar-menu-link ${activeMenu == 'admin-orders' ? 'active' : ''}">
								<i class="bi bi-receipt-cutoff"></i> <span>Xu ly don hang</span>
						</a></li>
					</ul>
				</div>
			</c:if>
		</div>

		<!-- Sidebar footer: profile + logout, or login/register -->
		<c:choose>
			<c:when test="${not empty currentUser}">
				<div class="sidebar-profile">
					<div
						class="sidebar-profile-img d-flex align-items-center justify-content-center bg-forest-medium text-white fw-bold">
						${fn:substring(currentUser.fullname, 0, 1)}</div>
					<div class="sidebar-profile-info">
						<div class="sidebar-profile-name">${currentUser.fullname}</div>
						<div class="sidebar-profile-email">${currentUser.email}</div>
					</div>
					<a href="${ctx}/logout" class="btn-table-action" title="Dang xuat">
						<i class="bi bi-box-arrow-right"></i>
					</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="sidebar-profile justify-content-center gap-2">
					<a href="${ctx}/login"
						class="btn-custom btn-sidebar-outline btn-custom-sm w-100">Dang
						nhap</a> <a href="${ctx}/register"
						class="btn-custom btn-custom-primary btn-custom-sm w-100">Dang
						ky</a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<!-- ========================== /Sidebar ========================== -->


	<!-- ========================== Main Content ========================== -->
	<div class="main-wrapper">

		<header class="navbar-custom">
			<div class="navbar-left">
				<button
					class="btn-desktop-toggle d-none d-xl-flex align-items-center justify-content-center me-3"
					id="desktop-sidebar-toggle" aria-label="Minimize Sidebar">
					<i class="bi bi-chevron-bar-left"></i>
				</button>
				<button class="sidebar-toggle-btn me-2" id="sidebar-toggle"
					aria-label="Toggle Navigation">
					<i class="bi bi-list"></i>
				</button>
			</div>

			<div class="navbar-search-wrapper">
				<form action="${ctx}/product" method="get" class="w-100 d-flex">
					<input type="text" class="navbar-search-input" name="keyword"
						placeholder="Tim kiem..." value="${keyword}">
				</form>
			</div>

			<div class="navbar-actions">
				<c:choose>
					<c:when test="${not empty currentUser}">
						<div class="dropdown ms-2">
							<button class="navbar-profile-btn dropdown-toggle" type="button"
								data-bs-toggle="dropdown" aria-expanded="false">
								<span class="navbar-profile-name d-none d-md-inline">${currentUser.fullname}</span>
								<c:if test="${isAdmin}">
									<span class="badge-table success ms-1">admin</span>
								</c:if>
								<i class="bi bi-chevron-down navbar-profile-caret"></i>
							</button>
							<ul class="dropdown-menu dropdown-menu-end dropdown-menu-profile">
								<li class="dropdown-header">Xin chao!</li>
								<li><a class="dropdown-item text-danger"
									href="${ctx}/logout"><i class="bi bi-box-arrow-right"></i>
										Dang xuat</a></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<a href="${ctx}/login"
							class="btn-custom btn-custom-outline-primary btn-custom-sm me-2">Dang
							nhap</a>
						<a href="${ctx}/register"
							class="btn-custom btn-custom-primary btn-custom-sm">Dang ky</a>
					</c:otherwise>
				</c:choose>
			</div>
		</header>

		<c:if test="${not empty flashError}">
			<div class="alert-custom alert-custom-danger mx-4 mt-3">
				<i class="bi bi-exclamation-triangle-fill alert-custom-icon"></i>
				<div class="alert-custom-content">${flashError}</div>
			</div>
		</c:if>

		<div class="page-header">
			<div>
				<h1 class="page-title">
					<c:out value="${pageTitle}" default="SHOP" />
				</h1>
				<c:if test="${not empty pageSubtitle}">
					<p class="page-subtitle">${pageSubtitle}</p>
				</c:if>
			</div>
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb mb-0">
					<li class="breadcrumb-item"><a href="${ctx}/home"
						class="text-decoration-none text-muted-green">Home</a></li>
					<li class="breadcrumb-item active text-main" aria-current="page"><c:out
							value="${pageTitle}" /></li>
				</ol>
			</nav>
		</div>