<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Регистрация" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="col-6 offset-3">
    <div class="p-4 bg-white border shadow-sm">
      <h2 class="h4 text-center">
        <fmt:message bundle="${lang}" key="sign_up_page.sign_up_form"/>
      </h2>
      <div class="my-3 border-bottom"></div>
      <form action="<c:url value="/main/registration"/>" method="post" class="m-0">
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.login}">
            <div class="alert alert-danger">
              ${sessionScope.errors.login}
            </div>
          </c:if>
          <label for="user-login" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_login"/>
          </label>
          <input type="text" name="login" id="user-login" class="form-control"/>
        </div>
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.password}">
            <div class="alert alert-danger">
                ${sessionScope.errors.password}
            </div>
          </c:if>
          <label for="user-password" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_password"/>
          </label>
          <input type="password" name="password" id="user-password" class="form-control"/>
        </div>
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.email}">
            <div class="alert alert-danger">
                ${sessionScope.errors.email}
            </div>
          </c:if>
          <label for="user-email" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_email"/>
          </label>
          <input type="text" name="email" id="user-email" class="form-control"/>
        </div>
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.phone}">
            <div class="alert alert-danger">
                ${sessionScope.errors.phone}
            </div>
          </c:if>
          <label for="user-phone" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_phone"/>
          </label>
          <input type="text" name="phone" id="user-phone" class="form-control"/>
        </div>
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.firstName}">
            <div class="alert alert-danger">
                ${sessionScope.errors.firstName}
            </div>
          </c:if>
          <label for="user-first-name" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_first_name"/>
          </label>
          <input type="text" name="firstName" id="user-first-name" class="form-control"/>
        </div>
        <div class="mb-3">
          <c:if test="${not empty sessionScope.errors.lastName}">
            <div class="alert alert-danger">
                ${sessionScope.errors.lastName}
            </div>
          </c:if>
          <label for="user-last-name" class="form-label">
            <fmt:message bundle="${lang}" key="sign_up_page.user_last_name"/>
          </label>
          <input type="text" name="lastName" id="user-last-name" class="form-control"/>
        </div>
        <button type="submit" class="btn btn-primary">
          <fmt:message bundle="${lang}" key="sign_up_page.submit_form_button"/>
        </button>
        <c:remove var="errors" scope="session"/>
        <c:remove var="old" scope="session"/>
      </form>
    </div>
  </div>
</main>
</body>
</html>
