<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Subjects | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/yt.css">
</head>
<body class="yt-body">
<%@ include file="/jsp/fragments/topNav.jspf" %>
<div class="yt-shell">
    <%@ include file="/jsp/fragments/sideNav.jspf" %>
    <main class="yt-main">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-4">
            <div>
                <p class="text-muted mb-1">Browse subjects</p>
                <h2 class="fw-semibold mb-0">Subjects Offered</h2>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/subjects" method="get" class="mb-4">
            <div class="d-flex gap-2">
                <input type="text" name="q" class="form-control" placeholder="Search subjects..." value="${query}">
                <button class="btn btn-primary">Search</button>
            </div>
        </form>

        <c:choose>
            <c:when test="${empty subjects}">
                <div class="yt-card">
                    <p class="yt-empty-state">No subjects found.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-3">
                    <c:forEach var="s" items="${subjects}">
                        <div class="col-md-6 col-lg-4">
                            <div class="yt-card">
                                <div class="yt-card__header">
                                    <a href="${pageContext.request.contextPath}/subjects?id=${s.id}" class="text-decoration-none">
                                        <h5 class="mb-0">${s.name}</h5>
                                    </a>
                                </div>
                                <p class="text-muted mb-2 small">${s.description != null && !s.description.isEmpty() ? s.description : 'No description available.'}</p>
                                <a href="${pageContext.request.contextPath}/subjects?id=${s.id}" class="btn btn-sm btn-outline-primary">
                                    View Details
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</div>
</body>
</html>

