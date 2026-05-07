<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Customer' : 'Add Customer'}" scope="request"/>
<c:set var="currentPage" value="customers" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/customers">Customers</a> ›
  <span>${editMode ? 'Edit Customer #'.concat(customer.id) : 'Add New Customer'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">&#128101;</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Edit Customer Details' : 'Add New Customer'}</h2>
      <p class="fch-sub">${editMode ? 'Update contact details, email or membership.' : 'Insert details of a new customer into the system.'}</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/customers" class="styled-form">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="customerId" value="${customer.id}"/></c:if>

    <div class="form-section">
      <h3 class="fs-title">Personal Information</h3>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label">Full Name <span class="req">*</span></label>
          <input type="text" name="name" class="form-control" placeholder="Full name" required maxlength="100"
                 value="${editMode ? customer.name : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Phone Number <span class="req">*</span></label>
          <input type="tel" name="phone" class="form-control" placeholder="07XXXXXXXX" required maxlength="20"
                 value="${editMode ? customer.phone : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Email Address</label>
          <input type="email" name="email" class="form-control" placeholder="email@example.com" maxlength="100"
                 value="${editMode ? customer.email : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Membership Type</label>
          <select name="membershipType" class="form-control form-select">
            <option value="REGULAR"  ${editMode && 'REGULAR'.equals(customer.membershipType) ? 'selected' : ''}>Regular</option>
            <option value="PREMIUM"  ${editMode && 'PREMIUM'.equals(customer.membershipType) ? 'selected' : ''}>Premium</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Address</label>
        <textarea name="address" class="form-control form-textarea" rows="2" maxlength="200"
                  placeholder="Full address…">${editMode ? customer.address : ''}</textarea>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/customers" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">${editMode ? '✏️ Update' : '+ Add Customer'}</button>
    </div>
  </form>
</div>

<%@ include file="footer.jsp" %>
