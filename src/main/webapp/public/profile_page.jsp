<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Профиль" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-8 offset-2">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="profile_page.profile"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="profile_page.user_info"/>
        </div>
        <div class="mb-3">
          <label for="user-login" class="form-label">
            <fmt:message bundle="${lang}" key="profile_page.user_login"/>
          </label>
          <input type="text" value="${sessionScope.authUser.login}" id="user-login" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="user-email" class="form-label">
            <fmt:message bundle="${lang}" key="profile_page.user_email"/>
          </label>
          <input type="text" value="${sessionScope.authUser.email}" id="user-email" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="user-phone" class="form-label">
            <fmt:message bundle="${lang}" key="profile_page.user_phone"/>
          </label>
          <input type="text" value="${sessionScope.authUser.phone}" id="user-phone" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="user-first-name" class="form-label">
            <fmt:message bundle="${lang}" key="profile_page.user_first_name"/>
          </label>
          <input type="text" value="${sessionScope.authUser.firstName}" id="user-first-name" class="form-control" readonly/>
        </div>
        <div class="mb-3">
          <label for="user-last-name" class="form-label">
            <fmt:message bundle="${lang}" key="profile_page.user_last_name"/>
          </label>
          <input type="text" value="${sessionScope.authUser.lastName}" id="user-last-name" class="form-control" readonly/>
        </div>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="profile_page.order_list"/>
        </div>
        <table class="table table-bordered m-0">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col"><fmt:message bundle="${lang}" key="profile_page.order_status"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="profile_page.order_address"/></th>
            <th scope="col"><fmt:message bundle="${lang}" key="profile_page.order_total"/></th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="order" items="${requestScope.orders}">
            <tr class="align-middle">
              <th scope="row"><c:out value="${order.id}"/></th>
              <td><c:out value="${order.status.displayName}"/></td>
              <td>${order.street}, ${order.house}, ${order.apartment}</td>
              <td>${order.orderProducts.stream().map(orderProduct -> orderProduct.product.cost * orderProduct.amount).sum()}</td>
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
