<%@ include file="/WEB-INF/common/head.jsp" %>

<c:if test="${not empty message}">
  <div class="alert-custom alert-custom-success mb-3">
    <i class="bi bi-check-circle-fill alert-custom-icon"></i>
    <div class="alert-custom-content">${message}</div>
  </div>
</c:if>

<div class="table-card-custom">
  <div class="table-header-control">
    <div class="table-search-box">
      <i class="bi bi-search table-search-icon"></i>
      <input type="text" class="table-search-input" id="cateSearch" placeholder="Tim danh muc...">
    </div>
    <div class="table-filter-group">
      <a href="${ctx}/admin/categories/add" class="btn-custom btn-custom-primary btn-custom-sm">
        <i class="bi bi-plus-lg"></i> Them danh muc
      </a>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table-custom" id="cateTable">
      <thead>
        <tr>
          <th>Icon</th>
          <th>Ten danh muc</th>
          <th>So san pham</th>
          <th>Trang thai</th>
          <th class="text-center">Hanh dong</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${cateList}" var="c">
          <tr>
            <td><img src="${ctx}/image/${c.images}" alt="${c.categoryName}" style="width:40px;height:40px;object-fit:contain;"></td>
            <td class="table-product-name">${c.categoryName}</td>
            <td>${navCategoryCounts[c.categoryId]}</td>
            <td>
              <c:choose>
                <c:when test="${c.status == 0}"><span class="badge-table success">Hoat dong</span></c:when>
                <c:otherwise><span class="badge-table failed">An</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <div class="d-flex justify-content-center gap-1">
                <a href="${ctx}/admin/categories/edit/${c.categoryId}" class="table-btn-action" title="Sua"><i class="bi bi-pencil"></i></a>
                <a href="${ctx}/admin/categories/delete/${c.categoryId}" class="table-btn-action delete" title="Xoa"
                  onclick="return confirm('Xoa danh muc \'${c.categoryName}\'?');"><i class="bi bi-trash"></i></a>
              </div>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<script>
  document.getElementById('cateSearch').addEventListener('input', function () {
    var q = this.value.toLowerCase();
    document.querySelectorAll('#cateTable tbody tr').forEach(function (row) {
      row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
    });
  });
</script>

<%@ include file="/WEB-INF/common/foot.jspf" %>
