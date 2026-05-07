<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Customer Profile" scope="request"/>
<c:set var="currentPage" value="customers" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/customers">Customers</a> › ${customer.name}
</div>

<div class="detail-card">
  <div class="detail-hdr">
    <div class="ds-user">
      <div class="avatar-lg">${customer.initial}</div>
      <div>
        <h2 class="detail-id">${customer.name}</h2>
        <p class="detail-meta">Customer #${customer.id} &nbsp;|&nbsp; Member since ${customer.createdAtFormatted}</p>
      </div>
    </div>
    <span class="badge badge-lg ${'PREMIUM'.equals(customer.membershipType) ? 'badge-completed' : 'badge-confirmed'}">
      ${customer.membershipType}
    </span>
  </div>

  <div class="detail-grid-2">
    <div class="detail-section">
      <h4 class="ds-title">Contact</h4>
      <table class="info-table">
        <tr><td class="it-label">Phone</td><td>${customer.phone}</td></tr>
        <tr><td class="it-label">Email</td><td>${customer.email}</td></tr>
        <tr><td class="it-label">Address</td><td>${customer.address}</td></tr>
      </table>
    </div>
    <div class="detail-section">
      <h4 class="ds-title">Order History</h4>
      <c:forEach var="o" items="${orders}">
        <div class="order-mini">
          <span class="order-id">#${o.id}</span>
          <span>${o.productName}</span>
          <span>Rs.<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0"/></span>
          <span class="badge ${o.status.badgeClass}">${o.status.label}</span>
        </div>
      </c:forEach>
      <c:if test="${empty orders}"><p class="empty-sub">No orders yet.</p></c:if>
    </div>
  </div>

  <div class="detail-actions">
    <a href="${pageContext.request.contextPath}/customers" class="btn btn-ghost">&larr; Back</a>
    <a href="${pageContext.request.contextPath}/customers?action=edit&id=${customer.id}" class="btn btn-outline">&#9998; Edit</a>
    <a href="${pageContext.request.contextPath}/customers?action=delete&id=${customer.id}" class="btn btn-danger"
       onclick="return confirm('Remove customer ${customer.name}?')">&#10005; Remove</a>
  </div>
</div>

<%@ include file="footer.jsp" %>
