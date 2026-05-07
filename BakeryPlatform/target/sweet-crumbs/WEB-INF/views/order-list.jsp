<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Order Management" scope="request"/>
<c:set var="currentPage" value="orders" scope="request"/>
<%@ include file="header.jsp" %>

<!-- Page Header -->
<div class="page-header">
  <div>
    <h2 class="section-title">Orders</h2>
    <p class="section-sub">Manage all customer cake &amp; bakery orders</p>
  </div>
  <a href="${pageContext.request.contextPath}/orders?action=new" class="btn btn-primary">+ Place New Order</a>
</div>

<!-- Stats Row -->
<div class="stats-row">
  <div class="stat-pill">
    <span class="sp-num">${totalOrders}</span><span class="sp-lbl">Total</span>
  </div>
  <div class="stat-pill sp-amber">
    <span class="sp-num">${pendingCount}</span><span class="sp-lbl">Pending</span>
  </div>
  <div class="stat-pill sp-blue">
    <span class="sp-num">${confirmedCount}</span><span class="sp-lbl">Confirmed</span>
  </div>
  <div class="stat-pill sp-green">
    <span class="sp-num">${completedCount}</span><span class="sp-lbl">Completed</span>
  </div>
  <div class="stat-pill sp-teal">
    <span class="sp-num">Rs.<fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></span>
    <span class="sp-lbl">Revenue</span>
  </div>
</div>

<!-- Toolbar -->
<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/orders" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input"
             placeholder="Search by Order ID or customer name…"
             value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
  <div class="filter-tabs">
    <a href="${pageContext.request.contextPath}/orders" class="ftab ${activeFilter=='ALL'?'active':''}">All</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=PENDING"     class="ftab ${activeFilter=='PENDING'?'active':''}">Pending</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=CONFIRMED"   class="ftab ${activeFilter=='CONFIRMED'?'active':''}">Confirmed</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=IN_PROGRESS" class="ftab ${activeFilter=='IN_PROGRESS'?'active':''}">In Progress</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=READY"       class="ftab ${activeFilter=='READY'?'active':''}">Ready</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=COMPLETED"   class="ftab ${activeFilter=='COMPLETED'?'active':''}">Completed</a>
    <a href="${pageContext.request.contextPath}/orders?action=filter&status=CANCELLED"   class="ftab ${activeFilter=='CANCELLED'?'active':''}">Cancelled</a>
  </div>
</div>

<!-- Table -->
<div class="table-card">
  <c:choose>
    <c:when test="${empty orders}">
      <div class="empty-state">
        <div class="empty-icon">&#128203;</div>
        <p class="empty-title">No orders found</p>
        <p class="empty-sub"><c:if test="${not empty searchQuery}">No results for "<b>${searchQuery}</b>"</c:if></p>
        <a href="${pageContext.request.contextPath}/orders?action=new" class="btn btn-primary">Place First Order</a>
      </div>
    </c:when>
    <c:otherwise>
      <table class="data-table">
        <thead>
          <tr>
            <th>#ID</th>
            <th>Customer</th>
            <th>Product</th>
            <th>Qty</th>
            <th>Total</th>
            <th>Pickup Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="o" items="${orders}">
          <tr class="tr-hover">
            <td class="order-id">#${o.id}</td>
            <td>
              <div class="cell-user">
                <div class="avatar-sm">${o.customerName.charAt(0)}</div>
                <div>
                  <div class="cell-name">${o.customerName}</div>
                  <div class="cell-sub">ID: ${o.customerId}</div>
                </div>
              </div>
            </td>
            <td class="td-product">${o.productName}</td>
            <td>${o.quantity}</td>
            <td class="td-amount">Rs.<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
            <td class="td-date">${o.pickupDate}</td>
            <td><span class="badge ${o.status.badgeClass}">${o.status.label}</span></td>
            <td>
              <div class="action-group">
                <a href="${pageContext.request.contextPath}/orders?action=view&id=${o.id}"   class="ico-btn ico-view"   title="View">&#128065;</a>
                <a href="${pageContext.request.contextPath}/orders?action=edit&id=${o.id}"   class="ico-btn ico-edit"   title="Edit">&#9998;</a>
                <a href="${pageContext.request.contextPath}/orders?action=status&id=${o.id}" class="ico-btn ico-status" title="Update Status">&#8635;</a>
                <a href="${pageContext.request.contextPath}/orders?action=delete&id=${o.id}" class="ico-btn ico-delete" title="Delete"
                   onclick="return confirm('Cancel Order #${o.id} for ${o.customerName}?')">&#10005;</a>
              </div>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="table-footer">
        Showing <strong>${orders.size()}</strong> order(s)
        <c:if test="${not empty searchQuery}"> matching "<em>${searchQuery}</em>"</c:if>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jsp" %>
