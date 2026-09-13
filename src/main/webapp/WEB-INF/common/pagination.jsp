<%--
  Fragment phan trang dung chung cho cac trang quan tri (Category, User, ...).
  Bien can co truoc khi include:
    pageObj   - org.springframework.data.domain.Page dang hien thi
    baseUrl   - duong dan goc, vi du "${ctx}/admin/categories"
    keyword   - tu khoa tim kiem hien tai (co the rong)
--%>
<c:if test="${pageObj.totalPages > 1}">
  <nav class="d-flex justify-content-between align-items-center mt-3">
    <span class="text-muted-green small">
      Trang ${pageObj.number + 1} / ${pageObj.totalPages}
      (tong ${pageObj.totalElements} ban ghi)
    </span>
    <ul class="pagination pagination-sm mb-0">
      <li class="page-item ${pageObj.first ? 'disabled' : ''}">
        <a class="page-link"
          href="${baseUrl}?keyword=${keyword}&page=${pageObj.number - 1}&size=${pageObj.size}">&laquo;</a>
      </li>
      <c:forEach begin="0" end="${pageObj.totalPages - 1}" var="i">
        <li class="page-item ${i == pageObj.number ? 'active' : ''}">
          <a class="page-link"
            href="${baseUrl}?keyword=${keyword}&page=${i}&size=${pageObj.size}">${i + 1}</a>
        </li>
      </c:forEach>
      <li class="page-item ${pageObj.last ? 'disabled' : ''}">
        <a class="page-link"
          href="${baseUrl}?keyword=${keyword}&page=${pageObj.number + 1}&size=${pageObj.size}">&raquo;</a>
      </li>
    </ul>
  </nav>
</c:if>
