<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0"/>
<title>${pageTitle != null ? pageTitle : 'Sweet Crumbs'} — Bakery Platform</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:opsz,wght@9..40,300;9..40,400;9..40,500;9..40,600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body>

<!-- SIDEBAR NAV -->
<aside class="sidebar">
  <div class="sidebar-brand">
    <span class="brand-icon">&#9749;</span>
    <div>
      <div class="brand-name">Sweet Crumbs</div>
      <div class="brand-sub">Bakery Platform</div>
    </div>
  </div>

  <nav class="sidebar-nav">
    <a href="${pageContext.request.contextPath}/dashboard"
       class="nav-item ${currentPage=='dashboard'?'active':''}">
      <span class="nav-icon">&#127968;</span> Dashboard
    </a>
    <div class="nav-section-label">Manage</div>
    <a href="${pageContext.request.contextPath}/orders"
       class="nav-item ${currentPage=='orders'?'active':''}">
      <span class="nav-icon">&#128203;</span> Orders
    </a>
    <a href="${pageContext.request.contextPath}/customers"
       class="nav-item ${currentPage=='customers'?'active':''}">
      <span class="nav-icon">&#128101;</span> Customers
    </a>
    <a href="${pageContext.request.contextPath}/products"
       class="nav-item ${currentPage=='products'?'active':''}">
      <span class="nav-icon">&#127856;</span> Products
    </a>
    <a href="${pageContext.request.contextPath}/reviews"
       class="nav-item ${currentPage=='reviews'?'active':''}">
      <span class="nav-icon">&#11088;</span> Reviews
    </a>
    <a href="${pageContext.request.contextPath}/payments"
       class="nav-item ${currentPage=='payments'?'active':''}">
      <span class="nav-icon">&#128179;</span> Payments
    </a>
    <div class="nav-section-label">System</div>
    <a href="${pageContext.request.contextPath}/admins"
       class="nav-item ${currentPage=='admins'?'active':''}">
      <span class="nav-icon">&#128737;</span> Admins
    </a>
    <a href="${pageContext.request.contextPath}/admins?action=logout"
       class="nav-item nav-logout">
      <span class="nav-icon">&#x2192;</span> Logout
    </a>
  </nav>

  <c:if test="${sessionScope.loggedInAdmin != null}">
  <div class="sidebar-user">
    <div class="user-avatar">${sessionScope.loggedInAdmin.initial}</div>
    <div>
      <div class="user-name">${sessionScope.loggedInAdmin.fullName}</div>
      <div class="user-role">${sessionScope.loggedInAdmin.role}</div>
    </div>
  </div>
  </c:if>
</aside>

<!-- MAIN AREA -->
<main class="main-content">
<div class="topbar">
  <button class="sidebar-toggle" onclick="document.body.classList.toggle('sidebar-open')">&#9776;</button>
  <h1 class="topbar-title">${pageTitle != null ? pageTitle : 'Dashboard'}</h1>
  <div class="topbar-right">
    <span class="topbar-date" id="topbarDate"></span>
  </div>
</div>

<!-- Flash messages -->
<c:if test="${not empty sessionScope.successMsg}">
  <div class="flash flash-success">
    <span>&#10003;</span> ${sessionScope.successMsg}
    <button class="flash-close" onclick="this.parentElement.remove()">&times;</button>
  </div>
  <c:remove var="successMsg" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.errorMsg}">
  <div class="flash flash-error">
    <span>&#9888;</span> ${sessionScope.errorMsg}
    <button class="flash-close" onclick="this.parentElement.remove()">&times;</button>
  </div>
  <c:remove var="errorMsg" scope="session"/>
</c:if>
<c:if test="${not empty errorMsg}">
  <div class="flash flash-error">
    <span>&#9888;</span> ${errorMsg}
    <button class="flash-close" onclick="this.parentElement.remove()">&times;</button>
  </div>
</c:if>

<div class="page-body">
