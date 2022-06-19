<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Административная панель" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-4 offset-4">
      <div class="p-4 bg-white border shadow-sm">
        <a href="<c:url value="/main/admin/products"/>" class="btn btn-outline-primary w-100">
          <fmt:message bundle="${lang}" key="admin_main_page.products"/>
        </a>
        <a href="<c:url value="/main/admin/orders"/>" class="btn btn-outline-primary w-100 mt-3">
          <fmt:message bundle="${lang}" key="admin_main_page.orders"/>
        </a>
      </div>
    </div>
  </div>
</main>
</body>
</html>
