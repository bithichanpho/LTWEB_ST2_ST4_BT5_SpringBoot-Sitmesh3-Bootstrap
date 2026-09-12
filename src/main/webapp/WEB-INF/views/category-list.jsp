<%@ include file="/WEB-INF/common/head.jsp" %>

<div class="row g-3">
  <c:forEach items="${cateList}" var="c">
    <div class="col-md-4">
      <a href="${ctx}/category/detail?id=${c.categoryId}" class="text-decoration-none">
        <div class="card d-flex flex-row align-items-center gap-3 p-3 h-100">
          <img src="${ctx}/image/${c.images}" alt="${c.categoryName}" style="width:64px;height:64px;object-fit:contain;">
          <div>
            <div class="fw-bold" style="color: var(--brand-forest-medium);">${c.categoryName}</div>
            <div class="text-muted-green small">${navCategoryCounts[c.categoryId]} san pham</div>
          </div>
        </div>
      </a>
    </div>
  </c:forEach>
  <c:if test="${empty cateList}">
    <div class="col-12 text-center text-muted-green py-4">Chua co danh muc nao.</div>
  </c:if>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
