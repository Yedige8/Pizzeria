<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Просмотр товара" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-6 offset-3">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="view_product_page.view_product"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="view_product_page.product_info"/>
        </div>
        <p class="fs-5">
          <fmt:message bundle="${lang}" key="view_product_page.product_average_rating"/>:
          <span class="fw-bold">${requestScope.averageRating} (отзывов: ${requestScope.reviews.size()})</span>
        </p>
        <p class="fs-5">
          <fmt:message bundle="${lang}" key="view_product_page.product_category"/>:
          <span class="fw-bold">${requestScope.product.category.name}</span>
        </p>
        <p class="fs-5">
          <fmt:message bundle="${lang}" key="view_product_page.product_name"/>:
          <span class="fw-bold">${requestScope.product.name}</span>
        </p>
        <p class="fs-5">
          <fmt:message bundle="${lang}" key="view_product_page.product_cost"/>:
          <span class="fw-bold">
            <fmt:formatNumber value="${requestScope.product.cost}" currencyCode="KZT" type="CURRENCY"/>
          </span>
        </p>
        <p class="fs-5">
          <fmt:message bundle="${lang}" key="view_product_page.product_description"/>:
          <span>${requestScope.product.description}</span>
        </p>
        <div class="alert alert-primary">
          <fmt:message bundle="${lang}" key="view_product_page.reviews"/>
        </div>
        <c:if test="${not empty sessionScope.authUser and not requestScope.reviewExists}">
          <form action="<c:url value="/main/create_review"/>" method="post">
            <input type="hidden" name="productId" value="${requestScope.product.id}"/>
            <div class="mb-2">
              <label class="form-label m-0 me-2">
                <fmt:message bundle="${lang}" key="view_product_page.review_rating"/>
              </label>
              <label><input type="radio" name="rating" value="1" class="form-check-input"/> 1</label>
              <label><input type="radio" name="rating" value="2" class="form-check-input"/> 2</label>
              <label><input type="radio" name="rating" value="3" class="form-check-input"/> 3</label>
              <label><input type="radio" name="rating" value="4" class="form-check-input"/> 4</label>
              <label><input type="radio" name="rating" value="5" class="form-check-input"/> 5</label>
            </div>
            <div class="mb-3">
              <label for="review-comment" class="form-label">
                <fmt:message bundle="${lang}" key="view_product_page.review_comment"/>
              </label>
              <textarea rows="3" name="comment" id="review-comment" class="form-control"></textarea>
            </div>
            <button type="submit" class="btn btn-outline-primary">
              <fmt:message bundle="${lang}" key="view_product_page.submit_form_button"/>
            </button>
          </form>
        </c:if>
        <c:if test="${not empty sessionScope.authUser and requestScope.reviewExists}">
          <p class="fs-5 text-center m-0">
            <fmt:message bundle="${lang}" key="view_product_page.already_reviewed"/>
          </p>
        </c:if>
        <c:if test="${not empty requestScope.reviews}">
          <c:forEach var="review" items="${requestScope.reviews}">
            <div class="my-3 border-bottom"></div>
            <p class="mb-1">
              <fmt:message bundle="${lang}" key="view_product_page.review_user"/>:
              <span class="fw-bold">${review.user.firstName} ${review.user.lastName}</span>
            </p>
            <p class="mb-1">
              <fmt:message bundle="${lang}" key="view_product_page.review_rating"/>:
              <span class="fw-bold">${review.rating}</span>
            </p>
            <p>
              <fmt:message bundle="${lang}" key="view_product_page.review_comment"/>:
              <span class="fw-bold">${review.comment}</span>
            </p>
          </c:forEach>
        </c:if>
        <c:if test="${empty requestScope.reviews}">
          <p class="fs-5 text-center m-0">
            <fmt:message bundle="${lang}" key="view_product_page.no_reviews"/>
          </p>
        </c:if>
      </div>
    </div>
  </div>
</main>
</body>
</html>
