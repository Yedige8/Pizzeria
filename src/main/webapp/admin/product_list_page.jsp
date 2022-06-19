<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Список товаров" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-10 offset-1">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="product_list_page.product_list"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <a href="<c:url value="/main/admin/products/create"/>" class="btn btn-outline-primary">
          <fmt:message bundle="${lang}" key="product_list_page.create_product_button"/>
        </a>
        <div class="my-3 border-bottom"></div>
        <table class="table table-bordered m-0">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Отображение в списке</th>
            <th scope="col"><fmt:message bundle="${lang}" key="product_list_page.product_category"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="product_list_page.product_name"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="product_list_page.product_cost"/></th>
            <th scope="col"></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="product" items="${requestScope.products}">
            <tr class="align-middle">
              <th scope="row"><c:out value="${product.id}"/></th>
              <td>${product.active ? 'Отображается' : 'Не отображается'}</td>
              <td><c:out value="${product.category.name}"/></td>
              <td><c:out value="${product.name}"/></td>
              <td><c:out value="${product.cost}"/></td>
              <td>
                <a href="<c:url value="/main/admin/products/update?productId=${product.id}"/>" class="btn btn-outline-success">
                  <fmt:message bundle="${lang}" key="global.edit_button"/>
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</main>
</body>
</html>
