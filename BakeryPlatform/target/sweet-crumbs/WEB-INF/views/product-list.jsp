<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Product Management" scope="request"/>
<c:set var="currentPage" value="products" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header">
  <div>
    <h2 class="section-title">Products</h2>
    <p class="section-sub">Manage bakery catalogue — cakes, cupcakes, pastries and more</p>
  </div>
  <a href="${pageContext.request.contextPath}/products?action=new" class="btn btn-primary">+ Add Product</a>
</div>

<div class="stats-row">
  <div class="stat-pill"><span class="sp-num">${totalProducts}</span><span class="sp-lbl">Total</span></div>
  <div class="stat-pill sp-green"><span class="sp-num">${availableCount}</span><span class="sp-lbl">Available</span></div>
</div>

<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/products" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input" placeholder="Search products…" value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
  <div class="filter-tabs">
    <a href="${pageContext.request.contextPath}/products" class="ftab ${activeFilter=='ALL'?'active':''}">All</a>
    <a href="${pageContext.request.contextPath}/products?action=filter&category=CAKE"    class="ftab ${activeFilter=='CAKE'?'active':''}">🎂 Cakes</a>
    <a href="${pageContext.request.contextPath}/products?action=filter&category=CUPCAKE" class="ftab ${activeFilter=='CUPCAKE'?'active':''}">🧁 Cupcakes</a>
    <a href="${pageContext.request.contextPath}/products?action=filter&category=PASTRY"  class="ftab ${activeFilter=='PASTRY'?'active':''}">🥐 Pastries</a>
    <a href="${pageContext.request.contextPath}/products?action=filter&category=BREAD"   class="ftab ${activeFilter=='BREAD'?'active':''}">🍞 Bread</a>
    <a href="${pageContext.request.contextPath}/products?action=filter&category=CUSTOM"  class="ftab ${activeFilter=='CUSTOM'?'active':''}">⭐ Custom</a>
  </div>
</div>

<div class="table-card">
  <c:choose>
    <c:when test="${empty products}">
      <div class="empty-state">
        <div class="empty-icon">&#127856;</div>
        <p class="empty-title">No products found</p>
        <a href="${pageContext.request.contextPath}/products?action=new" class="btn btn-primary">Add First Product</a>
      </div>
    </c:when>
    <c:otherwise>
      <table class="data-table">
        <thead><tr><th>#</th><th>Product</th><th>Category</th><th>Price</th><th>Status</th><th>Actions</th></tr></thead>
        <tbody>
          <c:forEach var="p" items="${products}">
          <tr class="tr-hover">
            <td class="order-id">#${p.id}</td>
            <td>
              <div class="cell-user">
                <div class="avatar-sm" style="font-size:18px;background:var(--cream)">${p.categoryIcon}</div>
                <div>
                  <div class="cell-name">${p.name}</div>
                  <div class="cell-sub">${p.description.length() > 50 ? p.description.substring(0,50).concat('…') : p.description}</div>
                </div>
              </div>
            </td>
            <td><span class="cat-badge">${p.category}</span></td>
            <td class="td-amount">Rs.<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
            <td>
              <span class="badge ${p.available ? 'badge-completed' : 'badge-cancelled'}">
                ${p.available ? 'Available' : 'Discontinued'}
              </span>
            </td>
            <td>
              <div class="action-group">
                <a href="${pageContext.request.contextPath}/products?action=view&id=${p.id}"   class="ico-btn ico-view">&#128065;</a>
                <a href="${pageContext.request.contextPath}/products?action=edit&id=${p.id}"   class="ico-btn ico-edit">&#9998;</a>
                <a href="${pageContext.request.contextPath}/products?action=delete&id=${p.id}" class="ico-btn ico-delete"
                   onclick="return confirm('Remove product ${p.name}?')">&#10005;</a>
              </div>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="table-footer">Showing <strong>${products.size()}</strong> product(s)</div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jsp" %>
