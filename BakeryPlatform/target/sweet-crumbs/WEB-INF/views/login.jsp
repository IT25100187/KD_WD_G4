<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0"/>
<title>Login — Sweet Crumbs Bakery</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:opsz,wght@9..40,300;9..40,400;9..40,500;9..40,600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body class="login-page">

<div class="login-wrap">
  <div class="login-brand">
    <div class="login-icon">&#9749;</div>
    <h1 class="login-title">Sweet Crumbs</h1>
    <p class="login-sub">Bakery &amp; Custom Cake Platform</p>
  </div>

  <div class="login-card">
    <h2 class="login-card-title">Admin Login</h2>
    <p class="login-card-sub">Sign in to manage your bakery</p>

    <c:if test="${not empty errorMsg}">
      <div class="flash flash-error" style="margin-bottom:1rem;">
        <span>&#9888;</span> ${errorMsg}
      </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login" class="login-form">
      <div class="form-group">
        <label class="form-label" for="username">Username</label>
        <input type="text" id="username" name="username" class="form-control"
               placeholder="Enter username" required autocomplete="username"/>
      </div>
      <div class="form-group">
        <label class="form-label" for="password">Password</label>
        <input type="password" id="password" name="password" class="form-control"
               placeholder="Enter password" required autocomplete="current-password"/>
      </div>
      <button type="submit" class="btn btn-primary btn-full">Sign In &rarr;</button>
    </form>

    <div class="login-hint">
      <strong>Demo credentials:</strong><br/>
      Username: <code>admin</code> &nbsp;|&nbsp; Password: <code>admin123</code><br/>
      Username: <code>manager</code> &nbsp;|&nbsp; Password: <code>manager1</code>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
