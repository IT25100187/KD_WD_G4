<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Payment' : 'Record Payment'}" scope="request"/>
<c:set var="currentPage" value="payments" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/payments">Payments</a> ›
  <span>${editMode ? 'Edit Payment #'.concat(payment.id) : 'Record New Payment'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">&#128179;</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Update Payment Details' : 'Record Payment'}</h2>
      <p class="fch-sub">${editMode ? 'Update payment status or correct payment details.' : 'Add payment details for a customer order.'}</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/payments" class="styled-form">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="paymentId" value="${payment.id}"/></c:if>

    <div class="form-section">
      <h3 class="fs-title">Payment Details</h3>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label">Order ID <span class="req">*</span></label>
          <input type="number" name="orderId" class="form-control" required min="1"
                 placeholder="e.g. 1" value="${editMode ? payment.orderId : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Customer ID</label>
          <input type="number" name="customerId" class="form-control" min="0"
                 value="${editMode ? payment.customerId : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Customer Name</label>
          <input type="text" name="customerName" class="form-control"
                 placeholder="Customer full name"
                 value="${editMode ? payment.customerName : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Amount (Rs.) <span class="req">*</span></label>
          <input type="number" name="amount" class="form-control" required min="0" step="0.01"
                 placeholder="0.00" value="${editMode ? payment.amount : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Payment Method</label>
          <select name="method" class="form-control form-select">
            <c:forEach var="m" items="${methods}">
              <option value="${m}" ${editMode && payment.method == m ? 'selected' : ''}>${m}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Payment Status</label>
          <select name="status" class="form-control form-select">
            <c:forEach var="s" items="${statuses}">
              <option value="${s}" ${editMode && payment.status == s ? 'selected' : ''}>${s}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Transaction Reference</label>
          <input type="text" name="transactionRef" class="form-control"
                 placeholder="Auto-generated if blank"
                 value="${editMode ? payment.transactionRef : ''}"/>
        </div>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/payments" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">
        ${editMode ? '✏️ Update Payment' : '💳 Record Payment'}
      </button>
    </div>
  </form>
</div>

<%@ include file="footer.jsp" %>
