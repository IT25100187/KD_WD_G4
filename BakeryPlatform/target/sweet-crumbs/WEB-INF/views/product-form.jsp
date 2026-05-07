<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="${editMode ? 'Edit Product' : 'Add Product'}" scope="request"/>
<c:set var="currentPage" value="products" scope="request"/>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
  <a href="${pageContext.request.contextPath}/products">Products</a> ›
  <span>${editMode ? 'Edit Product #'.concat(product.id) : 'Add New Product'}</span>
</div>

<div class="form-card">
  <div class="form-card-header">
    <div class="fch-icon">&#127856;</div>
    <div>
      <h2 class="fch-title">${editMode ? 'Modify Product Details' : 'Add New Product'}</h2>
      <p class="fch-sub">${editMode ? 'Update name, price, category, or availability.' : 'Add a new cake or bakery item to the catalogue.'}</p>
    </div>
  </div>

  <form method="post" action="${pageContext.request.contextPath}/products" class="styled-form">
    <input type="hidden" name="action" value="${editMode ? 'update' : 'save'}"/>
    <c:if test="${editMode}"><input type="hidden" name="productId" value="${product.id}"/></c:if>

    <div class="form-section">
      <h3 class="fs-title">Product Details</h3>
      <div class="form-group">
        <label class="form-label">Product Name <span class="req">*</span></label>
        <input type="text" name="name" class="form-control" required maxlength="150"
               placeholder="e.g. Chocolate Fudge Cake"
               value="${editMode ? product.name : ''}"/>
      </div>
      <div class="form-grid-2">
        <div class="form-group">
          <label class="form-label">Category</label>
          <select name="category" class="form-control form-select">
            <c:forEach var="cat" items="${categories}">
              <option value="${cat}" ${editMode && product.category == cat ? 'selected' : ''}>${cat}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Price (Rs.) <span class="req">*</span></label>
          <input type="number" name="price" class="form-control" required min="0" step="0.01"
                 placeholder="0.00" value="${editMode ? product.price : ''}"/>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Description</label>
        <textarea name="description" class="form-control form-textarea" rows="3" maxlength="300"
                  placeholder="Brief product description…">${editMode ? product.description : ''}</textarea>
      </div>
      <div class="form-group">
        <label class="form-label">Image Tag / Emoji (optional)</label>
        <input type="text" name="imageTag" class="form-control" maxlength="10"
               placeholder="e.g. 🎂" value="${editMode ? product.imageTag : ''}"/>
      </div>
      <div class="form-group">
        <label class="check-label">
          <input type="checkbox" name="available" value="true"
                 ${!editMode || product.available ? 'checked' : ''}/>
          &nbsp;Available for ordering
        </label>
      </div>
    </div>

    <div class="form-actions">
      <a href="${pageContext.request.contextPath}/products" class="btn btn-ghost">Cancel</a>
      <button type="submit" class="btn btn-primary">${editMode ? '✏️ Update Product' : '+ Add Product'}</button>
    </div>
  </form>
</div>

<%@ include file="footer.jsp" %>
