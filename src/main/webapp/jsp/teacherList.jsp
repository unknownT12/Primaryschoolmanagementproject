<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Teachers | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">Teachers</h3>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">Dashboard</a>
            <a href="${pageContext.request.contextPath}/teachers?action=new" class="btn btn-primary">+ Add Teacher</a>
        </div>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
            ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/teachers" method="get" class="d-flex gap-2 mt-3">
        <input type="text" name="q" class="form-control" placeholder="Search by name or subject" value="${keyword}">
        <button class="btn btn-outline-primary">Search</button>
    </form>

    <div class="card mt-3 shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty teachers}">
                    <p class="text-muted text-center my-4">No teachers found.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead class="table-light">
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Subjects Qualified</th>
                                <th>Date Joined</th>
                                <th style="width: 220px;">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="t" items="${teachers}">
                                <tr>
                                    <td>${t.name} ${t.surname}</td>
                                    <td>${t.email}</td>
                                    <td>
                                        <c:forEach var="s" items="${t.subjectsQualified}">
                                            <span class="badge bg-secondary me-1 mb-1">${s}</span>
                                        </c:forEach>
                                    </td>
                                    <td>${t.dateJoined}</td>
                                    <td class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/teachers?action=edit&id=${t.id}"
                                           class="btn btn-sm btn-outline-primary">Edit</a>
                                        <a href="${pageContext.request.contextPath}/teachers?action=assign&id=${t.id}"
                                           class="btn btn-sm btn-outline-success">Assign Classes</a>
                                        <a href="${pageContext.request.contextPath}/teachers?action=delete&id=${t.id}"
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Delete ${t.name}?');">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
