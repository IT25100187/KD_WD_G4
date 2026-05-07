<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Admin' : 'New Admin'}" scope="request"/>
<c:set var="currentPage" value="admins" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/admins">Admins</a> ›
  <span>${editMode ? 'Edit Admin #'.concat(admin.id) : 'Create New Admin'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">&#128737;</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Update Admin Details' : 'Create New Admin'}</h2>
      <p class="fch-sub">${editMode ? 'Update profile or permissions. Leave password blank to keep existing.' : 'Insert new admin details into the system.'}</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/admins" class="styled-form">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="adminId" value="${admin.id}"/></c:if>

    <div class="form-section">
      <h3 class="fs-title">Admin Details</h3>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label">Username <span class="req">*</span></label>
          <input type="text" name="username" class="form-control" required maxlength="50"
                 placeholder="e.g. admin2" value="${editMode ? admin.username : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Full Name</label>
          <input type="text" name="fullName" class="form-control" maxlength="100"
                 placeholder="Full name" value="${editMode ? admin.fullName : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Email</label>
          <input type="email" name="email" class="form-control"
                 placeholder="admin@sweetcrumbs.lk" value="${editMode ? admin.email : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Role</label>
          <select name="role" class="form-control form-select">
            <c:forEach var="r" items="${roles}">
              <option value="${r}" ${editMode && admin.role.equals(r) ? 'selected' : ''}>${r}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Password ${editMode ? '(blank = no change)' : ''} <c:if test="${!editMode}"><span class="req">*</span></c:if></label>
          <input type="password" name="password" class="form-control"
                 placeholder="${editMode ? 'Leave blank to keep existing' : 'Enter password'}"
                 ${editMode ? '' : 'required'}/>
        </div>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/admins" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">${editMode ? '✏️ Update Admin' : '+ Create Admin'}</button>
    </div>
  </form>
</div>

<%@ include file="footer.jsp" %>
