<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Review Management" scope="request"/>
<c:set var="currentPage" value="reviews" scope="request"/>
<%@ include file="header.jsp" %>

<div class="page-header">
  <div><h2 class="section-title">Reviews &amp; Feedback</h2>
       <p class="section-sub">Moderate customer reviews for bakery products</p></div>
  <a href="${pageContext.request.contextPath}/reviews?action=new" class="btn btn-primary">+ Add Review</a>
</div>

<div class="stats-row">
  <div class="stat-pill"><span class="sp-num">${totalReviews}</span><span class="sp-lbl">Total</span></div>
  <div class="stat-pill sp-green"><span class="sp-num">${approvedCount}</span><span class="sp-lbl">Approved</span></div>
  <div class="stat-pill sp-amber"><span class="sp-num">${pendingCount}</span><span class="sp-lbl">Pending</span></div>
</div>

<div class="toolbar">
  <form method="get" action="${pageContext.request.contextPath}/reviews" class="search-form">
    <input type="hidden" name="action" value="search"/>
    <div class="search-wrap">
      <span class="search-icon">&#128269;</span>
      <input type="text" name="q" class="search-input" placeholder="Search reviews…" value="${searchQuery}"/>
      <button type="submit" class="btn btn-sm btn-outline">Search</button>
    </div>
  </form>
</div>

<div class="table-card">
  <c:choose>
    <c:when test="${empty reviews}">
      <div class="empty-state">
        <div class="empty-icon">&#11088;</div>
        <p class="empty-title">No reviews found</p>
      </div>
    </c:when>
    <c:otherwise>
      <table class="data-table">
        <thead><tr><th>#</th><th>Customer</th><th>Product</th><th>Rating</th><th>Comment</th><th>Status</th><th>Actions</th></tr></thead>
        <tbody>
          <c:forEach var="r" items="${reviews}">
          <tr class="tr-hover">
            <td class="order-id">#${r.id}</td>
            <td>${r.customerName}</td>
            <td>${r.productName}</td>
            <td>${r.stars}</td>
            <td class="td-product">${r.comment.length() > 60 ? r.comment.substring(0,60).concat('…') : r.comment}</td>
            <td>
              <span class="badge ${r.approved ? 'badge-completed' : 'badge-pending'}">
                ${r.approved ? 'Approved' : 'Pending'}
              </span>
            </td>
            <td>
              <div class="action-group">
                <c:if test="${!r.approved}">
                  <a href="${pageContext.request.contextPath}/reviews?action=approve&id=${r.id}"
                     class="ico-btn ico-status" title="Approve">&#10003;</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/reviews?action=edit&id=${r.id}"   class="ico-btn ico-edit">&#9998;</a>
                <a href="${pageContext.request.contextPath}/reviews?action=delete&id=${r.id}" class="ico-btn ico-delete"
                   onclick="return confirm('Delete this review?')">&#10005;</a>
              </div>
            </td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="table-footer">Showing <strong>${reviews.size()}</strong> review(s)</div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="footer.jsp" %>
