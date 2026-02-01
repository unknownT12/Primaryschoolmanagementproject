<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm mx-auto" style="max-width: 420px;">
        <div class="card-body">
            <h4 class="text-center mb-3">Primary School Admin</h4>
            <p class="text-muted text-center small">Sign in to continue</p>

            <c:if test="${not empty flash}">
                <div class="alert alert-success">${flash}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="login">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" value="${rememberedUsername}" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" required>
                </div>
                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" name="remember" id="remember"
                           <c:if test="${not empty rememberedUsername}">checked</c:if>>
                    <label class="form-check-label" for="remember">Remember me on this device</label>
                </div>
                <button type="submit" class="btn btn-primary w-100">Sign In</button>
            </form>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">Need an account? Register</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
