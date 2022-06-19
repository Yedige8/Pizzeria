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
    <div class="col-12">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="order_list_page.order_list"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <table class="table table-bordered m-0">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col"><fmt:message bundle="${lang}" key="order_list_page.order_status"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="order_list_page.order_user_first_name"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="order_list_page.order_user_last_name"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="order_list_page.order_user_last_name"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="profile_page.order_address"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="cart_page.product_total"/></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="order" items="${requestScope.orders}">
            <tr class="align-middle">
              <th scope="row"><c:out value="${order.id}"/></th>
              <td><c:out value="${order.status.displayName}"/></td>
              <td><c:out value="${order.user.firstName}"/></td>
              <td><c:out value="${order.user.lastName}"/></td>
              <td>${order.street}, ${order.house}, ${order.apartment}</td>
              <td>${order.orderProducts.stream().map(orderProduct -> orderProduct.product.cost * orderProduct.amount).sum()}</td>
              <td>
                <a href="<c:url value="/main/admin/orders/update?orderId=${order.id}"/>" class="btn btn-outline-success">
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
