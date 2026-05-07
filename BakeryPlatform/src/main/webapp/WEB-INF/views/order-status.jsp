<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Update Order Status" scope="request"/>
<c:set var="currentPage" value="orders" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/orders">Orders</a> ›
  <a href="${pageContext.request.contextPath}/orders?action=view&id=${order.id}">Order #${order.id}</a> ›
  Update Status
</div>

<div class="form-card" style="max-width:600px">
  <div class="form-card-header">
    <div class="fch-icon">&#8635;</div>
    <div>
      <h2 class="fch-title">Update Order Status</h2>
      <p class="fch-sub">Order #${order.id} — ${order.customerName} — ${order.productName}</p>
    </div>
  </div>

  <!-- Status flow visual -->
  <div class="status-flow">
    <c:forEach var="step" items="${statuses}">
      <c:if test="${step.name() != 'CANCELLED'}">
      <div class="sf-step ${order.status == step ? 'sf-current' : ''}">
        <div class="sf-dot"></div>
        <div class="sf-lbl">${step.label}</div>
      </div>
      <c:if test="${step.name() != 'COMPLETED'}">
        <div class="sf-line"></div>
      </c:if>
      </c:if>
    </c:forEach>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/orders">
    <input type="hidden" name="action" value="status"/>
    <input type="hidden" name="orderId" value="${order.id}"/>

    <div class="status-options">
      <c:forEach var="s" items="${statuses}">
      <label class="status-opt ${order.status == s ? 'status-opt-cur' : ''}">
        <input type="radio" name="status" value="${s}" ${order.status == s ? 'checked' : ''}/>
        <div class="so-content">
          <span class="badge ${s.badgeClass}">${s.label}</span>
          <span class="so-desc">
            <c:choose>
              <c:when test="${s.name() == 'PENDING'}">Order received, awaiting confirmation</c:when>
              <c:when test="${s.name() == 'CONFIRMED'}">Confirmed and being prepared</c:when>
              <c:when test="${s.name() == 'IN_PROGRESS'}">Currently being baked / decorated</c:when>
              <c:when test="${s.name() == 'READY'}">Ready for customer pickup</c:when>
              <c:when test="${s.name() == 'COMPLETED'}">Order collected and fulfilled</c:when>
              <c:when test="${s.name() == 'CANCELLED'}">Order cancelled</c:when>
            </c:choose>
          </span>
        </div>
      </label>
      </c:forEach>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/orders" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">&#8635; Apply Status</button>
    </div>
  </form>
</div>

<%@ include file="footer.jsp" %>
