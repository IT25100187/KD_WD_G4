<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Product Detail" scope="request"/>
<c:set var="currentPage" value="products" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/products">Products</a> › ${product.name}
</div>

<div class="detail-card">
  <div class="detail-hdr">
    <div style="display:flex;gap:1rem;align-items:center">
      <div class="avatar-lg" style="font-size:28px;background:var(--cream)">${product.categoryIcon}</div>
      <div>
        <h2 class="detail-id">${product.name}</h2>
        <p class="detail-meta">${product.category} &nbsp;|&nbsp; #${product.id}</p>
      </div>
    </div>
    <span class="badge badge-lg ${product.available ? 'badge-completed' : 'badge-cancelled'}">
      ${product.available ? 'Available' : 'Discontinued'}
    </span>
  </div>

  <div class="detail-grid-2">
    <div class="detail-section">
      <h4 class="ds-title">Price</h4>
      <div class="ds-pickup">Rs.<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></div>
    </div>
    <div class="detail-section">
      <h4 class="ds-title">Description</h4>
      <p>${product.description}</p>
    </div>
  </div>

  <c:if test="${not empty reviews}">
  <div class="detail-section">
    <h4 class="ds-title">Customer Reviews</h4>
    <c:forEach var="r" items="${reviews}">
    <div class="review-mini">
      <div class="review-mini-top">
        <span class="review-name">${r.customerName}</span>
        <span class="review-stars">${r.stars}</span>
      </div>
      <div class="review-mini-comment">${r.comment}</div>
    </div>
    </c:forEach>
  </div>
  </c:if>

  <div class="detail-actions">
    <a href="${pageContext.request.contextPath}/products" class="btn btn-ghost">&larr; Back</a>
    <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}" class="btn btn-outline">&#9998; Edit</a>
    <a href="${pageContext.request.contextPath}/products?action=delete&id=${product.id}" class="btn btn-danger"
       onclick="return confirm('Remove product ${product.name}?')">&#10005; Remove</a>
  </div>
</div>

<%@ include file="footer.jsp" %>
