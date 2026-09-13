<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<html>
<head>
<title>${pageTitle}</title>
</head>
<body>

<c:if test="${not empty message}">
  <div class="alert-custom alert-custom-success mb-3">
    <i class="bi bi-check-circle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${message}</div>
  </div>
</c:if>

<div class="table-card-custom">
  <div class="table-header-control">
    <form action="${ctx}/admin/users" method="get" class="table-search-box">
      <i class="bi bi-search table-search-icon"></i>
      <input type="text" name="keyword" class="table-search-input"
        placeholder="Tim theo ho ten hoac email..." value="${keyword}">
    </form>
    <div class="table-filter-group">
      <a href="${ctx}/admin/users/add" class="btn-custom btn-custom-primary btn-custom-sm">
        <i class="bi bi-plus-lg"></i> Them nguoi dung
      </a>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table-custom" id="userTable">
      <thead>
        <tr>
          <th>Ho ten</th>
          <th>Email</th>
          <th>Dien thoai</th>
          <th>Vai tro</th>
          <th>Trang thai</th>
          <th class="text-center">Hanh dong</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${userList}" var="u">
          <tr>
            <td class="table-product-name">${u.fullname}</td>
            <td>${u.email}</td>
            <td>${not empty u.phone ? u.phone : '-'}</td>
            <td>
              <c:choose>
                <c:when test="${u.role == 'admin'}"><span class="badge-table success">Admin</span></c:when>
                <c:otherwise><span class="badge-table pending">User</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${u.status == 1}"><span class="badge-table success">Kich hoat</span></c:when>
                <c:otherwise><span class="badge-table failed">Chua kich hoat</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <div class="d-flex justify-content-center gap-1">
                <a href="${ctx}/admin/users/edit/${u.userId}" class="table-btn-action" title="Sua"><i class="bi bi-pencil"></i></a>
                <a href="${ctx}/admin/users/delete/${u.userId}" class="table-btn-action delete" title="Xoa"
                  onclick="return confirm('Xoa nguoi dung \'${u.fullname}\'?');"><i class="bi bi-trash"></i></a>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty userList}">
          <tr><td colspan="6" class="text-center text-muted-green py-3">Khong tim thay nguoi dung nao.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <c:set var="pageObj" value="${userPage}" />
  <c:set var="baseUrl" value="${ctx}/admin/users" />
  <%@ include file="/WEB-INF/common/pagination.jsp" %>
</div>

</body>
</html>