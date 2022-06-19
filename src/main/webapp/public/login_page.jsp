<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Вход" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-6 offset-3">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="sign_in_page.sign_in_form"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <form action="<c:url value="/main/login"/>" method="post" class="m-0">
          <c:if test="${not empty sessionScope.authError}">
            <div class="alert alert-danger">
              <c:out value="${sessionScope.authError}"/>
              <c:remove var="authError" scope="session"/>
            </div>
          </c:if>
          <div class="mb-3">
            <label for="user-login" class="form-label">
              <fmt:message bundle="${lang}" key="sign_in_page.user_login"/>
            </label>
            <input type="text" name="login" id="user-login" class="form-control" aria-describedby="user-login-help"/>
            <div id="user-login-help" class="form-text">
              <fmt:message bundle="${lang}" key="sign_in_page.user_login_help"/>
            </div>
          </div>
          <div class="mb-3">
            <label for="user-password" class="form-label">
              <fmt:message bundle="${lang}" key="sign_in_page.user_password"/>
            </label>
            <input type="password" name="password" id="user-password" class="form-control" aria-describedby="user-password-help"/>
          </div>
          <button type="submit" class="btn btn-primary">
            <fmt:message bundle="${lang}" key="sign_in_page.submit_form_button"/>
          </button>
        </form>
      </div>
    </div>
  </div>
</main>
</body>
</html>
