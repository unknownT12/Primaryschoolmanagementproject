<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Subjects | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<c:set var="isParent" value="${sessionScope.role == 'PARENT'}"/>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">Subjects</h3>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">Dashboard</a>
    </div>

    <c:if test="${not isParent}">
        <form action="${pageContext.request.contextPath}/subjects" method="post" class="card mt-3 p-3 shadow-sm border-0">
            <div class="row g-2">
                <div class="col-md-4">
                    <label class="form-label small text-muted">Subject name</label>
                    <input type="text" name="name" placeholder="e.g. Mathematics" class="form-control" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label small text-muted">Description</label>
                    <input type="text" name="description" class="form-control" placeholder="Short description">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">Add</button>
                </div>
            </div>
        </form>
    </c:if>

    <form action="${pageContext.request.contextPath}/subjects" method="get" class="d-flex gap-2 mt-3">
        <input type="text" name="q" class="form-control" placeholder="Search subjects..." value="${query}">
        <button class="btn btn-outline-primary">Search</button>
    </form>

    <div class="card mt-3 shadow-sm border-0">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty subjects}">
                    <p class="text-muted text-center my-4">No subjects captured.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead class="table-light">
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <c:if test="${not isParent}">
                                    <th style="width: 120px;">Actions</th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="s" items="${subjects}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/subjects?id=${s.id}" 
                                           class="text-decoration-none fw-semibold">
                                            ${s.name}
                                        </a>
                                    </td>
                                    <td>${s.description}</td>
                                    <c:if test="${not isParent}">
                                        <td>
                                            <form action="${pageContext.request.contextPath}/subjects" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${s.id}">
                                                <button class="btn btn-sm btn-outline-danger"
                                                        onclick="return confirm('Delete ${s.name}?');">Delete</button>
                                            </form>
                                        </td>
                                    </c:if>
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
