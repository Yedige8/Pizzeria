<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Оформление заказа" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row mt-4">
    <div class="col-8 offset-2">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="create_order_page.create_order"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary" role="alert">
          <fmt:message bundle="${lang}" key="create_order_page.order_products"/>
        </div>
        <table class="table table-bordered m-0">
          <thead>
          <tr>
            <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_name"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_cost_for_1"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_amount"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_total"/></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="cartItem" items="${requestScope.cartItems}">
            <tr class="align-middle">
              <td><c:out value="${cartItem.product.name}"/></td>
              <td><fmt:formatNumber value="${cartItem.product.cost}" currencyCode="KZT" type="CURRENCY"/></td>
              <td><c:out value="${cartItem.amount}"/></td>
              <td><fmt:formatNumber value="${cartItem.product.cost * cartItem.amount}" currencyCode="KZT" type="CURRENCY"/></td>
            </tr>
          </c:forEach>
          <tr>
            <th scope="rowgroup" colspan="3">
              <fmt:message bundle="${lang}" key="create_order_page.order_total"/>
            </th>
            <td>
              <c:set var="total" value="${requestScope.cartItems.stream().map(cartItem -> cartItem.product.cost * cartItem.amount).sum()}"/>
              <fmt:formatNumber value="${total}" currencyCode="KZT" type="CURRENCY"/>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="create_order_page.user_info"/>
        </div>
        <div class="mb-3">
          <label for="order-user-first-name" class="form-label">
            <fmt:message bundle="${lang}" key="create_order_page.user_first_name"/>
          </label>
          <input type="text" value="${sessionScope.authUser.firstName}" id="order-user-first-name" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="order-user-last-name" class="form-label">
            <fmt:message bundle="${lang}" key="create_order_page.user_last_name"/>
          </label>
          <input type="text" value="${sessionScope.authUser.lastName}" id="order-user-last-name" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="order-user-email" class="form-label">
            <fmt:message bundle="${lang}" key="create_order_page.user_email"/>
          </label>
          <input type="text" value="${sessionScope.authUser.email}" id="order-user-email" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="order-user-phone" class="form-label">
            <fmt:message bundle="${lang}" key="create_order_page.user_phone"/>
          </label>
          <input type="text" value="${sessionScope.authUser.phone}" id="order-user-phone" class="form-control" readonly/>
        </div>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="create_order_page.address_info"/>
        </div>
        <form action="<c:url value="/main/create_order"/>" method="post" class="m-0">
          <div class="mb-3">
            <label for="order-street" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_street"/>
            </label>
            <input type="text" name="street" id="order-street" class="form-control"/>
          </div>
          <div class="mb-3">
            <label for="order-house" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_house"/>
            </label>
            <input type="text" name="house" id="order-house" class="form-control"/>
          </div>
          <div class="mb-3">
            <label for="order-apartment" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_apartment"/>
            </label>
            <input type="text" name="apartment" id="order-apartment" class="form-control"/>
          </div>
          <button type="submit" class="btn btn-primary">
            <fmt:message bundle="${lang}" key="create_order_page.submit_form_button"/>
          </button>
        </form>
      </div>
    </div>
  </div>
</main>
</body>
</html>
