<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Order' : 'Place New Order'}" scope="request"/>
<c:set var="currentPage" value="orders" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/orders">Orders</a> ›
  <span>${editMode ? 'Edit Order #'.concat(order.id) : 'Place New Order'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">${editMode ? '✏️' : '🎂'}</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Modify Order Details' : 'Place a New Order'}</h2>
      <p class="fch-sub">${editMode ? 'Update pickup date, status, quantity, or notes.' : 'Fill in the customer and product details to record a new order.'}</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/orders" class="styled-form" id="orderForm">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="orderId" value="${order.id}"/></c:if>

    <!-- Section: Customer -->
    <div class="form-section">
      <h3 class="fs-title">Customer Information</h3>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label" for="customerId">Customer ID <span class="req">*</span></label>
          <input type="number" id="customerId" name="customerId" class="form-control"
                 placeholder="e.g. 101" min="1" required
                 value="${editMode ? order.customerId : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="customerName">Customer Name <span class="req">*</span></label>
          <input type="text" id="customerName" name="customerName" class="form-control"
                 placeholder="Full name" required maxlength="100"
                 value="${editMode ? order.customerName : ''}"/>
        </div>
      </div>
    </div>

    <!-- Section: Order -->
    <div class="form-section">
      <h3 class="fs-title">Order Details</h3>

      <div class="form-group">
        <label class="form-label" for="productName">Product / Cake Name <span class="req">*</span></label>
        <input type="text" id="productName" name="productName" class="form-control"
               placeholder="e.g. Chocolate Fudge Cake, Custom Wedding Cake" required maxlength="150"
               value="${editMode ? order.productName : ''}"/>
      </div>

      <div class="form-grid-3">
        <div class="form-group">
          <label class="form-label" for="quantity">Quantity <span class="req">*</span></label>
          <input type="number" id="quantity" name="quantity" class="form-control"
                 min="1" max="999" required
                 value="${editMode ? order.quantity : '1'}"
                 oninput="calcTotal()"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="unitPrice">Unit Price (Rs.) <span class="req">*</span></label>
          <input type="number" id="unitPrice" name="unitPrice" class="form-control"
                 min="0" step="0.01" required placeholder="0.00"
                 value="${editMode ? order.unitPrice : ''}"
                 oninput="calcTotal()"/>
        </div>
        <div class="form-group">
          <label class="form-label">Total Amount</label>
          <div class="total-display" id="totalDisplay">
            <c:choose>
              <c:when test="${editMode}">Rs. <fmt:formatNumber xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" value="${order.totalAmount}" pattern="#,##0.00"/></c:when>
              <c:otherwise>—</c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>

      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label" for="pickupDate">Pickup Date <span class="req">*</span></label>
          <input type="date" id="pickupDate" name="pickupDate" class="form-control" required
                 value="${editMode ? order.pickupDate : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label" for="status">Status</label>
          <select id="status" name="status" class="form-control form-select">
            <c:forEach var="s" items="${statuses}">
              <option value="${s}" ${editMode && order.status == s ? 'selected' : ''}>${s.label}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="specialNotes">Special Notes / Customisation</label>
        <textarea id="specialNotes" name="specialNotes" class="form-control form-textarea"
                  rows="3" maxlength="500"
                  placeholder="e.g. No nuts, fondant roses, Spiderman theme, 3 tiers, photo print…">${editMode ? order.specialNotes : ''}</textarea>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/orders" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">
        ${editMode ? '✏️ Update Order' : '📋 Place Order'}
      </button>
    </div>
  </form>
</div>

<script>
function calcTotal() {
  const qty   = parseFloat(document.getElementById('quantity').value)   || 0;
  const price = parseFloat(document.getElementById('unitPrice').value)  || 0;
  const total = qty * price;
  const el    = document.getElementById('totalDisplay');
  if (total > 0) {
    el.textContent = 'Rs. ' + total.toLocaleString('en-LK', {minimumFractionDigits:2, maximumFractionDigits:2});
    el.classList.add('total-active');
  } else {
    el.textContent = '—';
    el.classList.remove('total-active');
  }
}
document.getElementById('pickupDate').min = new Date().toISOString().split('T')[0];
</script>

<%@ include file="footer.jsp" %>
