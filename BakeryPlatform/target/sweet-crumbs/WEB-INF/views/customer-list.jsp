<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Customer Management" scope="request"/>
<c:set var="currentPage" value="customers" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header">
  <div>
    <h2 class="section-title">Customers</h2>
    <p class="section-sub">Manage customer accounts and information</p>
  </div>
  <a href="${pageContext.request.contextPath}/customers?action=new" class="btn btn-primary">+ Add Customer</a>
</div>

<div class="stats-row">
  <div class="stat-pill"><span class="sp-num">${totalCustomers}</span><span class="sp-lbl">Total</span></div>
  <div class="stat-pill sp-teal"><span class="sp-num">${premiumCount}</span><span class="sp-lbl">Premium</span></div>
</div>

<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/customers" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input"
             placeholder="Search by name, phone or email…" value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
</div>

<div class="table-card">
  <c:choose>
    <c:when test="${empty customers}">
      <div class="empty-state">
        <div class="empty-icon">&#128101;</div>
        <p class="empty-title">No customers found</p>
        <a href="${pageContext.request.contextPath}/customers?action=new" class="btn btn-primary">Add First Customer</a>
      </div>
    </c:when>
    <c:otherwise>
      <table class="data-table">
        <thead><tr><th>#ID</th><th>Name</th><th>Phone</th><th>Email</th><th>Membership</th><th>Actions</th></tr></thead>
        <tbody>
          <c:forEach var="c" items="${customers}">
          <tr class="tr-hover">
            <td class="order-id">#${c.id}</td>
            <td>
              <div class="cell-user">
                <div class="avatar-sm">${c.initial}</div>
                <span class="cell-name">${c.name}</span>
              </div>
            </td>
            <td>${c.phone}</td>
            <td>${c.email}</td>
            <td>
              <span class="badge ${'PREMIUM'.equals(c.membershipType) ? 'badge-completed' : 'badge-confirmed'}">
                ${c.membershipType}
              </span>
            </td>
            <td>
              <div class="action-group">
                <a href="${pageContext.request.contextPath}/customers?action=view&id=${c.id}"   class="ico-btn ico-view">&#128065;</a>
                <a href="${pageContext.request.contextPath}/customers?action=edit&id=${c.id}"   class="ico-btn ico-edit">&#9998;</a>
                <a href="${pageContext.request.contextPath}/customers?action=delete&id=${c.id}" class="ico-btn ico-delete"
                   onclick="return confirm('Remove customer ${c.name}?')">&#10005;</a>
              </div>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="table-footer">Showing <strong>${customers.size()}</strong> customer(s)</div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jsp" %>
