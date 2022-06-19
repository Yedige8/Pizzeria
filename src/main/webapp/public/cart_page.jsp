<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Корзина" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<div class="container my-5">
  <div class="row">
    <div class="col-8 offset-2">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="cart_page.cart"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <c:if test="${not empty requestScope.cartItems}">
          <p class="fs-5 m-0">
            <fmt:message bundle="${lang}" key="cart_page.cart_total">
              <fmt:formatNumber value="${requestScope.cartTotal}" currencyCode="KZT" type="CURRENCY" var="cartTotal"/>
              <fmt:param value="${cartTotal}"/>
            </fmt:message>
          </p>
          <div class="my-3 border-bottom"></div>
          <table class="table table-bordered m-0">
            <thead>
            <tr>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_name"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_cost_for_1"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_amount"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_total"/></th>
              <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cartItem" items="${requestScope.cartItems}">
              <tr class="align-middle">
                <td><c:out value="${cartItem.product.name}"/></td>
                <td><fmt:formatNumber value="${cartItem.product.cost}" currencyCode="KZT" type="CURRENCY"/></td>
                <td><c:out value="${cartItem.amount}"/></td>
                <td><fmt:formatNumber value="${cartItem.product.cost * cartItem.amount}" currencyCode="KZT" type="CURRENCY"/></td>
                <th>
                  <a href="<c:url value="/main/cart?action=delete&product_id=${cartItem.product.id}"/>" class="btn btn-outline-danger">
                    <fmt:message bundle="${lang}" key="cart_page.remove_item"/>
                  </a>
                </th>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <div class="mt-3">
            <a href="<c:url value="/main/create_order"/>" class="btn btn-outline-primary">
              <fmt:message bundle="${lang}" key="cart_page.create_order"/>
            </a>
            <a href="<c:url value="/main/cart?action=clear"/>" class="btn btn-outline-danger">
              <fmt:message bundle="${lang}" key="cart_page.clear_cart"/>
            </a>
          </div>
        </c:if>
        <c:if test="${empty requestScope.cartItems}">
          <p class="fs-5 text-center m-0">
            <fmt:message bundle="${lang}" key="cart_page.cart_empty"/>
          </p>
        </c:if>
      </div>
    </div>
  </div>
</div>
</body>
</html>
