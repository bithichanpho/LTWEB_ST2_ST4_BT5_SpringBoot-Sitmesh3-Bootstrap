<%@ include file="/WEB-INF/common/head.jsp" %>

<div class="card p-4 text-center">
  <i class="bi bi-exclamation-triangle-fill" style="font-size:2.5rem; color:#e5484d;"></i>
  <h4 class="mt-3 mb-2">Co loi xay ra</h4>
  <p class="text-muted-green mb-4">${errorMessage}</p>
  <div class="d-flex justify-content-center gap-2">
    <a href="${ctx}/home" class="btn-custom btn-custom-outline-primary">Ve trang chu</a>
    <a href="${ctx}/cart" class="btn-custom btn-custom-primary">Quay lai gio hang</a>
  </div>
</div>

<%@ include file="/WEB-INF/common/foot.jsp" %>
