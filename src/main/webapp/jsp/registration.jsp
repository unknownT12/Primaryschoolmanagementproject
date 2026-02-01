<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm mx-auto" style="max-width: 480px;">
        <div class="card-body">
            <h4 class="text-center mb-3">Create account</h4>
            <p class="text-muted text-center small">Parents can register to monitor student progress.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" class="mt-3">
                <div class="mb-3">
                    <label class="form-label">Full name</label>
                    <input type="text" name="fullName" class="form-control" value="${param.fullName}" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" value="${param.username}" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Confirm password</label>
                    <input type="password" name="confirmPassword" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Role</label>
                    <select name="role" class="form-select">
                        <option value="PARENT" <c:if test="${param.role == 'PARENT'}">selected</c:if>>Parent</option>
                        <option value="TEACHER" <c:if test="${param.role == 'TEACHER'}">selected</c:if>>Teacher</option>
                    </select>
                </div>
                <button class="btn btn-primary w-100">Register</button>
            </form>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/auth" class="text-decoration-none">Already have an account? Sign in</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

