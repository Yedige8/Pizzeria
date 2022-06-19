<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="messages" var="lang"/>
<html>
<c:set var="pageTitle" value="Редактирование товара" scope="request"/>
<c:import url="/fragments/head_fragment.jsp"/>
<body class="bg-light">
<c:import url="/fragments/header_fragment.jsp"/>
<main class="container my-5">
  <div class="row">
    <div class="col-6 offset-3">
      <div class="p-4 bg-white border shadow-sm">
        <h2 class="h4 text-center">
          <fmt:message bundle="${lang}" key="update_product_page.update_product_form"/>
        </h2>
        <div class="my-3 border-bottom"></div>
        <form action="<c:url value="/main/admin/products/update"/>" method="post" enctype="multipart/form-data" class="m-0">
          <input type="hidden" name="productId" value="${requestScope.product.id}"/>
          <div class="mb-3">
            <c:if test="${not empty sessionScope.errors.category}">
              <div class="alert alert-danger">
                  ${sessionScope.errors.category}
              </div>
            </c:if>
            <label class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_category"/>
            </label>
            <label class="d-none">
              <input type="radio" name="categoryId" value="0" checked/>
              Не выбрано
            </label>
            <c:forEach var="category" items="${requestScope.categories}">
              <div class="form-check">
                <label>
                  <c:if test="${requestScope.product.category.id == category.id}">
                    <input type="radio" name="categoryId" value="${category.id}" checked class="form-check-input">
                  </c:if>
                  <c:if test="${requestScope.product.category.id != category.id}">
                    <input type="radio" name="categoryId" value="${category.id}" class="form-check-input">
                  </c:if>
                  <c:out value="${category.name}"/>
                </label>
              </div>
            </c:forEach>
          </div>
          <div class="mb-3">
            <label class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_active"/>
            </label>
            <div class="form-check">
              <label>
                <c:if test="${requestScope.product.active}">
                  <input type="radio" name="active" value="true" checked class="form-check-input">
                </c:if>
                <c:if test="${!requestScope.product.active}">
                  <input type="radio" name="active" value="true" class="form-check-input">
                </c:if>
                <fmt:message bundle="${lang}" key="update_product_page.product_active.true"/>
              </label>
            </div>
            <div class="form-check">
              <label>
                <c:if test="${!requestScope.product.active}">
                  <input type="radio" name="active" value="false" checked class="form-check-input">
                </c:if>
                <c:if test="${requestScope.product.active}">
                  <input type="radio" name="active" value="false" class="form-check-input">
                </c:if>
                <fmt:message bundle="${lang}" key="update_product_page.product_active.false"/>
              </label>
            </div>
          </div>
          <div class="mb-3">
            <c:if test="${not empty sessionScope.errors.name}">
              <div class="alert alert-danger">
                  ${sessionScope.errors.name}
              </div>
            </c:if>
            <label for="product-name" class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_name"/>
            </label>
            <input type="text" name="name" value="${requestScope.product.name}" id="product-name" class="form-control"/>
          </div>
          <div class="mb-3">
            <c:if test="${not empty sessionScope.errors.description}">
              <div class="alert alert-danger">
                  ${sessionScope.errors.description}
              </div>
            </c:if>
            <label for="product-description" class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_description"/>
            </label>
            <textarea rows="7" name="description" id="product-description" class="form-control"><c:out value="${requestScope.product.description}"/></textarea>
          </div>
          <div class="mb-3">
            <c:if test="${not empty sessionScope.errors.cost}">
              <div class="alert alert-danger">
                  ${sessionScope.errors.cost}
              </div>
            </c:if>
            <label for="product-cost" class="form-label">
              <fmt:message bundle="${lang}" key="update_product_page.product_cost"/>
            </label>
            <input type="number" name="cost" value="${requestScope.product.cost}" id="product-cost" class="form-control"/>
          </div>
          <div class="mb-3">
            <c:if test="${not empty sessionScope.errors.image}">
              <div class="alert alert-danger">
                  ${sessionScope.errors.image}
              </div>
            </c:if>
            <input type="file" name="image" class="form-control"/>
          </div>
          <button type="submit" class="btn btn-primary">
            <fmt:message bundle="${lang}" key="update_product_page.submit_form_button"/>
          </button>
        </form>
      </div>
    </div>
  </div>
</main>
</body>
</html>
