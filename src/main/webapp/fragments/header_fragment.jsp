<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<header class="navbar navbar-expand-md bg-white border-bottom shadow-sm">
  <div class="container">
    <a href="<c:url value="/main/"/>" class="navbar-brand">Pizzeria</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a href="<c:url value="/main/"/>" class="nav-link">
            <fmt:message bundle="${lang}" key="header.main_page"/>
          </a>
        </li>
        <li href="<c:url value="/main/cart"/>" class="nav-item">
          <a href="<c:url value="/main/cart"/>" class="nav-link">
            <fmt:message bundle="${lang}" key="header.cart_page"/>
          </a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="locales" role="button" data-bs-toggle="dropdown" aria-expanded="false" style="outline: none;">
            <fmt:message bundle="${lang}" key="header.lang_button"/>
          </a>
          <ul class="dropdown-menu" aria-labelledby="locales" style="min-width: min-content;">
            <c:forEach var="supportedLocale" items="${applicationScope.supportedLocales}">
              <li>
                <a class="dropdown-item" href="<c:url value="/main/set_locale?locale=${supportedLocale.language}"/>">
                  <c:out value="${supportedLocale.getDisplayLanguage(supportedLocale).toLowerCase()}"/>
                </a>
              </li>
            </c:forEach>
          </ul>
        </li>
      </ul>
      <div class="d-flex flex-column flex-md-row">
        <c:if test="${empty sessionScope.authUser}">
          <a href="<c:url value="/main/login"/>" class="btn btn-outline-primary me-md-3 mb-3 mb-md-0">
            <fmt:message bundle="${lang}" key="header.sign_in_button"/>
          </a>
          <a href="<c:url value="/main/registration"/>" class="btn btn-outline-primary">
            <fmt:message bundle="${lang}" key="header.sign_up_button"/>
          </a>
        </c:if>
        <c:if test="${not empty sessionScope.authUser}">
          <c:if test="${sessionScope.authUser.role.serviceName == 'admin'}">
            <a href="<c:url value="/main/admin"/>" class="btn btn-outline-success me-md-3 mb-3 mb-md-0">
              <fmt:message bundle="${lang}" key="header.admin_panel_button"/>
            </a>
          </c:if>
          <a href="<c:url value="/main/profile"/>" class="btn btn-outline-primary me-md-3 mb-3 mb-md-0">
            <fmt:message bundle="${lang}" key="header.profile_button">
              <fmt:param value="${sessionScope.authUser.login}"/>
            </fmt:message>
          </a>
          <a href="<c:url value="/main/logout"/>" class="btn btn-outline-danger">
            <fmt:message bundle="${lang}" key="header.logout_button"/>
          </a>
        </c:if>
      </div>
    </div>
  </div>
</header>
