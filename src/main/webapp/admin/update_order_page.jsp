<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Редактирование заказа" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-8 offset-2">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="update_product_page.update_product_form"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <form action="<c:url value="/main/admin/orders/update"/>" method="post" class="m-0">
          <input type="hidden" name="orderId" value="${requestScope.order.id}"/>
          <div class="mb-3">
            <label class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_category"/>
            </label>
            <c:forEach var="status" items="${requestScope.statuses}">
              <div class="form-check">
                <label>
                  <c:if test="${requestScope.order.status.id == status.id}">
                    <input type="radio" name="statusId" value="${status.id}" checked class="form-check-input">
                  </c:if>
                  <c:if test="${requestScope.order.status.id != status.id}">
                    <input type="radio" name="statusId" value="${status.id}" class="form-check-input">
                  </c:if>
                  <c:out value="${status.displayName}"/>
                </label>
              </div>
            </c:forEach>
          </div>
          <div class="alert alert-primary">
            <fmt:message bundle="${lang}" key="create_order_page.order_products"/>
          </div>
          <table class="table table-bordered mb-3">
            <thead>
            <tr>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_name"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_cost_for_1"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_amount"/></th>
              <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_total"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="orderProduct" items="${requestScope.orderProducts}">
              <tr class="align-middle">
                <td><c:out value="${orderProduct.product.name}"/></td>
                <td><fmt:formatNumber value="${orderProduct.product.cost}" currencyCode="KZT" type="CURRENCY"/></td>
                <td><c:out value="${orderProduct.amount}"/></td>
                <td><fmt:formatNumber value="${orderProduct.product.cost * orderProduct.amount}" currencyCode="KZT" type="CURRENCY"/></td>
              </tr>
            </c:forEach>
            <tr>
              <th scope="rowgroup" colspan="3">
                <fmt:message bundle="${lang}" key="create_order_page.order_total"/>
              </th>
              <td>
                <c:set var="total" value="${requestScope.orderProducts.stream().map(orderProduct -> orderProduct.product.cost * orderProduct.amount).sum()}"/>
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
            <input type="text" value="${requestScope.order.user.firstName}" id="order-user-first-name" class="form-control" readonly/>
          </div>
          <div class="mb-3">
            <label for="order-user-last-name" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.user_last_name"/>
            </label>
            <input type="text" value="${requestScope.order.user.lastName}" id="order-user-last-name" class="form-control" readonly/>
          </div>
          <div class="mb-3">
            <label for="order-user-email" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.user_email"/>
            </label>
            <input type="text" value="${requestScope.order.user.email}" id="order-user-email" class="form-control" readonly/>
          </div>
          <div class="mb-3">
            <label for="order-user-phone" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.user_phone"/>
            </label>
            <input type="text" value="${requestScope.order.user.phone}" id="order-user-phone" class="form-control" readonly/>
          </div>
          <div class="my-3 border-bottom"></div>
          <div class="alert alert-primary">
            <fmt:message bundle="${lang}" key="create_order_page.address_info"/>
          </div>
          <div class="mb-3">
            <label for="order-street" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_street"/>
            </label>
            <input type="text" value="${requestScope.order.street}" id="order-street" class="form-control" readonly/>
          </div>
          <div class="mb-3">
            <label for="order-house" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_house"/>
            </label>
            <input type="text" value="${requestScope.order.house}" id="order-house" class="form-control" readonly/>
          </div>
          <div class="mb-3">
            <label for="order-apartment" class="form-label">
              <fmt:message bundle="${lang}" key="create_order_page.order_apartment"/>
            </label>
            <input type="text" value="${requestScope.order.apartment}" id="order-apartment" class="form-control" readonly/>
          </div>
          <button type="submit" class="btn btn-primary">
            <fmt:message bundle="${lang}" key="update_order_page.submit_form_button"/>
          </button>
        </form>
      </div>
    </div>
  </div>
</main>
</body>
</html>
