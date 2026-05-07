<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<c:set var="currentPage" value="dashboard" scope="request"/>
<%@ include file="header.jsp" %>

<!-- STAT CARDS -->
<div class="stats-grid">
  <a href="${pageContext.request.contextPath}/orders" class="stat-card stat-blue">
    <div class="stat-icon">&#128203;</div>
    <div class="stat-num">${totalOrders}</div>
    <div class="stat-lbl">Total Orders</div>
  </a>
  <a href="${pageContext.request.contextPath}/orders?action=filter&status=PENDING" class="stat-card stat-amber">
    <div class="stat-icon">&#9203;</div>
    <div class="stat-num">${pendingOrders}</div>
    <div class="stat-lbl">Pending Orders</div>
  </a>
  <a href="${pageContext.request.contextPath}/customers" class="stat-card stat-teal">
    <div class="stat-icon">&#128101;</div>
    <div class="stat-num">${totalCustomers}</div>
    <div class="stat-lbl">Customers</div>
  </a>
  <a href="${pageContext.request.contextPath}/products" class="stat-card stat-rose">
    <div class="stat-icon">&#127856;</div>
    <div class="stat-num">${totalProducts}</div>
    <div class="stat-lbl">Products</div>
  </a>
  <a href="${pageContext.request.contextPath}/reviews" class="stat-card stat-purple">
    <div class="stat-icon">&#11088;</div>
    <div class="stat-num">${pendingReviews}</div>
    <div class="stat-lbl">Reviews Pending</div>
  </a>
  <a href="${pageContext.request.contextPath}/payments" class="stat-card stat-green">
    <div class="stat-icon">&#128179;</div>
    <div class="stat-num">Rs.<fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
    <div class="stat-lbl">Total Revenue</div>
  </a>
</div>

<!-- RECENT ORDERS + QUICK ACTIONS -->
<div class="dash-grid">
  <!-- Recent Orders -->
  <div class="card">
    <div class="card-header">
      <h3 class="card-title">Recent Orders</h3>
      <a href="${pageContext.request.contextPath}/orders?action=new" class="btn btn-primary btn-sm">+ New Order</a>
    </div>
    <table class="data-table">
      <thead><tr><th>#</th><th>Customer</th><th>Product</th><th>Total</th><th>Status</th><th></th></tr></thead>
      <tbody>
      <c:forEach var="o" items="${recentOrders}">
        <tr>
          <td class="order-id">#${o.id}</td>
          <td>${o.customerName}</td>
          <td>${o.productName}</td>
          <td>Rs.<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0"/></td>
          <td><span class="badge ${o.status.badgeClass}">${o.status.label}</span></td>
          <td><a href="${pageContext.request.contextPath}/orders?action=view&id=${o.id}" class="link-btn">View</a></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <div class="card-footer">
      <a href="${pageContext.request.contextPath}/orders">View all orders &rarr;</a>
    </div>
  </div>

  <!-- Quick Actions + Recent Reviews -->
  <div class="dash-right">
    <div class="card">
      <div class="card-header"><h3 class="card-title">Quick Actions</h3></div>
      <div class="quick-actions">
        <a href="${pageContext.request.contextPath}/orders?action=new"    class="qa-btn">&#128203; Place Order</a>
        <a href="${pageContext.request.contextPath}/customers?action=new" class="qa-btn">&#128101; Add Customer</a>
        <a href="${pageContext.request.contextPath}/products?action=new"  class="qa-btn">&#127856; Add Product</a>
        <a href="${pageContext.request.contextPath}/payments?action=new"  class="qa-btn">&#128179; Record Payment</a>
      </div>
    </div>

    <div class="card" style="margin-top:1rem">
      <div class="card-header">
        <h3 class="card-title">Recent Reviews</h3>
        <a href="${pageContext.request.contextPath}/reviews" class="link-btn">All</a>
      </div>
      <c:forEach var="r" items="${recentReviews}">
      <div class="review-mini">
        <div class="review-mini-top">
          <span class="review-name">${r.customerName}</span>
          <span class="review-stars">${r.stars}</span>
        </div>
        <div class="review-mini-product">${r.productName}</div>
        <div class="review-mini-comment">${r.comment}</div>
        <c:if test="${!r.approved}">
          <a href="${pageContext.request.contextPath}/reviews?action=approve&id=${r.id}"
             class="badge badge-pending" style="text-decoration:none;cursor:pointer;">Approve</a>
        </c:if>
      </div>
      </c:forEach>
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>
