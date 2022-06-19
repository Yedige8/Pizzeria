<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Главная" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <c:forEach var="category" items="${requestScope.categories}">
    <c:if test="${not empty category.products}">
      <div class="row mt-4">
        <div class="col-6 offset-3">
          <div class="p-4 bg-white border shadow-sm">
            <h2 class="h4 text-center">
              <c:out value="${category.name}"/>
            </h2>
            <div class="my-3 border-bottom"></div>
            <div class="row">
              <c:forEach var="product" items="${category.products}">
                <div class="col-6">
                  <div class="card">
                    <div class="p-3 pb-0">
                      <img src="<c:url value="/upload/${product.id}.jpeg"/>" class="card-img-top" alt="Product image">
                    </div>
                    <div class="card-body">
                      <a href="<c:url value="/main/view_product?productId=${product.id}"/>">
                        <h5 class="card-title">
                          <c:out value="${product.name}"/>
                        </h5>
                      </a>
                      <p class="card-text mb-2 fs-5 fw-bold">
                        <fmt:formatNumber value="${product.cost}" currencyCode="KZT" type="CURRENCY"/>
                      </p>
                      <a href="<c:url value="/main/cart?action=add&product_id=${product.id}"/>" class="btn btn-outline-primary">
                        <fmt:message bundle="${lang}" key="main_page.add_to_cart_button"/>
                      </a>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </c:if>
  </c:forEach>
</main>
</body>
</html>
