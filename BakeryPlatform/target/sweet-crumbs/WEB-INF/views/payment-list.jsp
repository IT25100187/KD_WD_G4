<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Payment Management" scope="request"/>
<c:set var="currentPage" value="payments" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header">
  <div><h2 class="section-title">Payments</h2>
       <p class="section-sub">Track and manage payment records for all orders</p></div>
  <a href="${pageContext.request.contextPath}/payments?action=new" class="btn btn-primary">+ Record Payment</a>
</div>

<div class="stats-row">
  <div class="stat-pill"><span class="sp-num">${totalPayments}</span><span class="sp-lbl">Total</span></div>
  <div class="stat-pill sp-green"><span class="sp-num">${paidCount}</span><span class="sp-lbl">Paid</span></div>
  <div class="stat-pill sp-amber"><span class="sp-num">${pendingCount}</span><span class="sp-lbl">Pending</span></div>
  <div class="stat-pill sp-teal">
    <span class="sp-num">Rs.<fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></span>
    <span class="sp-lbl">Revenue</span>
  </div>
</div>

<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/payments" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input"
             placeholder="Search by customer, order ID or transaction ref…"
             value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
  <div class="filter-tabs">
    <a href="${pageContext.request.contextPath}/payments" class="ftab ${activeFilter=='ALL'?'active':''}">All</a>
    <a href="${pageContext.request.contextPath}/payments?action=filter&status=PENDING"  class="ftab ${activeFilter=='PENDING'?'active':''}">Pending</a>
    <a href="${pageContext.request.contextPath}/payments?action=filter&status=PAID"     class="ftab ${activeFilter=='PAID'?'active':''}">Paid</a>
    <a href="${pageContext.request.contextPath}/payments?action=filter&status=FAILED"   class="ftab ${activeFilter=='FAILED'?'active':''}">Failed</a>
    <a href="${pageContext.request.contextPath}/payments?action=filter&status=REFUNDED" class="ftab ${activeFilter=='REFUNDED'?'active':''}">Refunded</a>
  </div>
</div>

<div class="table-card">
  <c:choose>
    <c:when test="${empty payments}">
      <div class="empty-state">
        <div class="empty-icon">&#128179;</div>
        <p class="empty-title">No payments found</p>
        <a href="${pageContext.request.contextPath}/payments?action=new" class="btn btn-primary">Record First Payment</a>
      </div>
    </c:when>
    <c:otherwise>
      <table class="data-table">
        <thead>
          <tr>
            <th>#</th><th>Order</th><th>Customer</th>
            <th>Amount</th><th>Method</th><th>Ref</th><th>Status</th><th>Date</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="p" items="${payments}">
          <tr class="tr-hover">
            <td class="order-id">#${p.id}</td>
            <td><a href="${pageContext.request.contextPath}/orders?action=view&id=${p.orderId}"
                   class="link-btn">Order #${p.orderId}</a></td>
            <td>${p.customerName}</td>
            <td class="td-amount">Rs.<fmt:formatNumber value="${p.amount}" pattern="#,##0.00"/></td>
            <td><span class="cat-badge">${p.method}</span></td>
            <td class="td-date"><code>${p.transactionRef}</code></td>
            <td><span class="badge ${p.statusBadgeClass}">${p.status}</span></td>
            <td class="td-date">${p.createdAtFormatted}</td>
            <td>
              <div class="action-group">
                <a href="${pageContext.request.contextPath}/payments?action=edit&id=${p.id}"   class="ico-btn ico-edit">&#9998;</a>
                <a href="${pageContext.request.contextPath}/payments?action=delete&id=${p.id}" class="ico-btn ico-delete"
                   onclick="return confirm('Remove payment #${p.id}?')">&#10005;</a>
              </div>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="table-footer">Showing <strong>${payments.size()}</strong> payment(s)</div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jsp" %>
