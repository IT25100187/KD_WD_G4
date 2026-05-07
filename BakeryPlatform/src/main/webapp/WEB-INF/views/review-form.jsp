<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Review' : 'Submit Review'}" scope="request"/>
<c:set var="currentPage" value="reviews" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/reviews">Reviews</a> ›
  <span>${editMode ? 'Edit Review #'.concat(review.id) : 'Submit Review'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">&#11088;</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Edit Review' : 'Submit New Review'}</h2>
      <p class="fch-sub">Customers submit ratings and comments for bakery products.</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/reviews" class="styled-form">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="reviewId" value="${review.id}"/></c:if>

    <div class="form-section">
      <h3 class="fs-title">Review Details</h3>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label">Customer ID <span class="req">*</span></label>
          <input type="number" name="customerId" class="form-control" required min="1"
                 value="${editMode ? review.customerId : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Customer Name <span class="req">*</span></label>
          <input type="text" name="customerName" class="form-control" required
                 value="${editMode ? review.customerName : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Product ID <span class="req">*</span></label>
          <input type="number" name="productId" class="form-control" required min="1"
                 value="${editMode ? review.productId : ''}"/>
        </div>
        <div class="form-group">
          <label class="form-label">Product Name <span class="req">*</span></label>
          <input type="text" name="productName" class="form-control" required
                 value="${editMode ? review.productName : ''}"/>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Rating (1–5) <span class="req">*</span></label>
        <div class="star-select" id="starSelect">
          <c:forEach begin="1" end="5" var="i">
            <span class="star-opt ${editMode && review.rating >= i ? 'star-on' : ''}"
                  onclick="setRating(${i})" data-val="${i}">&#9733;</span>
          </c:forEach>
        </div>
        <input type="hidden" name="rating" id="ratingInput" value="${editMode ? review.rating : '5'}"/>
      </div>
      <div class="form-group">
        <label class="form-label">Comment <span class="req">*</span></label>
        <textarea name="comment" class="form-control form-textarea" rows="4" required maxlength="500"
                  placeholder="Share your experience with this product…">${editMode ? review.comment : ''}</textarea>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/reviews" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">${editMode ? '✏️ Update Review' : '⭐ Submit Review'}</button>
    </div>
  </form>
</div>

<script>
function setRating(val) {
  document.getElementById('ratingInput').value = val;
  document.querySelectorAll('.star-opt').forEach(function(s, i) {
    s.classList.toggle('star-on', i < val);
  });
}
</script>

<%@ include file="footer.jsp" %>
