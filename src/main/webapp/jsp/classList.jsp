<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Classes | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
            <h3 class="mb-0">Classes</h3>
            <p class="text-muted mb-0">${classCount} classes tracked</p>
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">Dashboard</a>
    </div>

    <form action="${pageContext.request.contextPath}/classes" method="post" class="row g-2 mt-3 card p-3 shadow-sm border-0">
        <div class="col-md-8">
            <label class="form-label small text-muted mb-1">New class name</label>
            <input type="text" name="className" class="form-control" placeholder="e.g. Standard 3" required>
        </div>
        <div class="col-md-4 d-flex align-items-end">
            <button class="btn btn-primary w-100">Add Class</button>
        </div>
    </form>

    <form action="${pageContext.request.contextPath}/classes" method="get" class="d-flex gap-2 mt-3">
        <input type="text" name="q" value="${query}" class="form-control" placeholder="Search classes...">
        <button class="btn btn-outline-primary">Search</button>
    </form>

    <div class="card mt-3 shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty classes}">
                    <p class="text-center text-muted my-4">No classes found.</p>
                </c:when>
                <c:otherwise>
                    <table class="table mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Name</th>
                            <th style="width: 200px;">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cItem" items="${classes}">
                            <tr>
                                <td>${cItem}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/classes?name=${cItem}"
                                       class="btn btn-sm btn-outline-primary">View roster</a>
                                    <a href="${pageContext.request.contextPath}/classes?action=delete&name=${cItem}"
                                       class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Remove ${cItem}? Assigned students will be unassigned.');">
                                        Delete
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
