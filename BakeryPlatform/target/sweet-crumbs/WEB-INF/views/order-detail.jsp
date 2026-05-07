<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Order Detail" scope="request"/>
<c:set var="currentPage" value="orders" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/orders">Orders</a> › Order #${order.id}
</div>

<div class="detail-card">
  <div class="detail-hdr">
    <div>
      <h2 class="detail-id">Order #${order.id}</h2>
      <p class="detail-meta">Placed: ${order.createdAtFormatted} &nbsp;|&nbsp; Updated: ${order.updatedAtFormatted}</p>
    </div>
    <span class="badge badge-lg ${order.status.badgeClass}">${order.status.label}</span>
  </div>

  <div class="detail-grid-2">
    <div class="detail-section">
      <h4 class="ds-title">Customer</h4>
      <div class="ds-user">
        <div class="avatar-lg">${order.customerName.charAt(0)}</div>
        <div>
          <div class="ds-name">${order.customerName}</div>
          <div class="ds-sub">Customer ID: ${order.customerId}</div>
        </div>
      </div>
    </div>
    <div class="detail-section">
      <h4 class="ds-title">Pickup Date</h4>
      <div class="ds-pickup">${order.pickupDate}</div>
    </div>
  </div>

  <div class="detail-section">
    <h4 class="ds-title">Order Summary</h4>
    <table class="detail-table">
      <thead><tr><th>Product</th><th>Qty</th><th>Unit Price</th><th>Total</th></tr></thead>
      <tbody>
        <tr>
          <td>${order.productName}</td>
          <td>${order.quantity}</td>
          <td>Rs.<fmt:formatNumber value="${order.unitPrice}" pattern="#,##0.00"/></td>
          <td class="td-total">Rs.<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
        </tr>
      </tbody>
    </table>
  </div>

  <c:if test="${not empty order.specialNotes}">
  <div class="detail-section">
    <h4 class="ds-title">Special Notes</h4>
    <div class="detail-notes">${order.specialNotes}</div>
  </div>
  </c:if>

  <div class="detail-actions">
    <a href="${pageContext.request.contextPath}/orders" class="btn btn-ghost">&larr; Back</a>
    <a href="${pageContext.request.contextPath}/orders?action=edit&id=${order.id}"   class="btn btn-outline">&#9998; Edit</a>
    <a href="${pageContext.request.contextPath}/orders?action=status&id=${order.id}" class="btn btn-primary">&#8635; Update Status</a>
    <a href="${pageContext.request.contextPath}/orders?action=delete&id=${order.id}" class="btn btn-danger"
       onclick="return confirm('Cancel and remove Order #${order.id}?')">&#10005; Cancel Order</a>
  </div>
</div>

<%@ include file="footer.jsp" %>
