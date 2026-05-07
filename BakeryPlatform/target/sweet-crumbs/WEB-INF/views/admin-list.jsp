<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Admin Management" scope="request"/>
<c:set var="currentPage" value="admins" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header">
  <div><h2 class="section-title">Admin Management</h2>
       <p class="section-sub">Manage system administrator accounts</p></div>
  <a href="${pageContext.request.contextPath}/admins?action=new" class="btn btn-primary">+ New Admin</a>
</div>

<div class="stats-row">
  <div class="stat-pill"><span class="sp-num">${totalAdmins}</span><span class="sp-lbl">Total Admins</span></div>
</div>

<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/admins" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input" placeholder="Search by username or name…" value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
</div>

<div class="table-card">
  <table class="data-table">
    <thead><tr><th>#</th><th>Admin</th><th>Username</th><th>Email</th><th>Role</th><th>Created</th><th>Actions</th></tr></thead>
    <tbody>
      <c:forEach var="a" items="${admins}">
      <tr class="tr-hover">
        <td class="order-id">#${a.id}</td>
        <td>
          <div class="cell-user">
            <div class="avatar-sm">${a.initial}</div>
            <span class="cell-name">${a.fullName}</span>
          </div>
        </td>
        <td><code>${a.username}</code></td>
        <td>${a.email}</td>
        <td><span class="badge ${a.superAdmin ? 'badge-rose' : 'badge-confirmed'}">${a.role}</span></td>
        <td class="td-date">${a.createdAtFormatted}</td>
        <td>
          <div class="action-group">
            <a href="${pageContext.request.contextPath}/admins?action=edit&id=${a.id}" class="ico-btn ico-edit">&#9998;</a>
            <a href="${pageContext.request.contextPath}/admins?action=delete&id=${a.id}" class="ico-btn ico-delete"
               onclick="return confirm('Remove admin ${a.username}?')">&#10005;</a>
          </div>
        </td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
  <div class="table-footer">Showing <strong>${admins.size()}</strong> admin(s)</div>
</div>

<%@ include file="footer.jsp" %>
