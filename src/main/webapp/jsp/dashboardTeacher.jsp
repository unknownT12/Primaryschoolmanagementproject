<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Workspace | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/yt.css">
</head>
<body class="yt-body">
<%@ include file="/jsp/fragments/topNav.jspf" %>
<div class="yt-shell">
    <%@ include file="/jsp/fragments/sideNav.jspf" %>
    <main class="yt-main">
        <c:if test="${param.denied == '1'}">
            <div class="yt-card text-danger mb-3">
                You do not have permission to access that section.
            </div>
        </c:if>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <p class="text-muted mb-1">Teacher dashboard</p>
                <h2 class="fw-semibold">${sessionScope.currentUser.fullName}</h2>
            </div>
            <a href="${pageContext.request.contextPath}/grades" class="yt-button">Capture grade</a>
        </div>

        <div class="yt-stat-grid">
            <div class="yt-stat">
                <label>Students</label>
                <strong>${studentCount}</strong>
                <small class="text-muted">Total learners</small>
            </div>
            <div class="yt-stat">
                <label>Classes</label>
                <strong>${classCount}</strong>
                <small class="text-muted">Available groups</small>
            </div>
            <div class="yt-stat">
                <label>Active learners</label>
                <strong>
                    <c:choose>
                        <c:when test="${not empty statusSummary}">
                            ${statusSummary['Active'] != null ? statusSummary['Active'] : 0}
                        </c:when>
                        <c:otherwise>0</c:otherwise>
                    </c:choose>
                </strong>
                <small class="text-muted">With status = Active</small>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-lg-6">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Recent students</span>
                    </div>
                    <c:if test="${empty recentStudents}">
                        <p class="yt-empty-state">No recent registrations.</p>
                    </c:if>
                    <c:if test="${not empty recentStudents}">
                        <table class="yt-table">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Class</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="student" items="${recentStudents}">
                                <tr>
                                    <td>${student.name} ${student.surname}</td>
                                    <td>${student.schoolClass}</td>
                                    <td><span class="badge bg-success">${student.status}</span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="yt-card yt-card--list">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Quick links</span>
                    </div>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/grades" class="list-group-item">Capture or update grades</a>
                        <a href="${pageContext.request.contextPath}/classes" class="list-group-item">View class rosters</a>
                        <a href="${pageContext.request.contextPath}/search" class="list-group-item">Search for a learner</a>
                        <span class="list-group-item small text-muted">Manage students (admin only)</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-lg-7">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Status summary</span>
                    </div>
                    <c:if test="${empty statusSummary}">
                        <p class="yt-empty-state">No students captured yet.</p>
                    </c:if>
                    <c:forEach var="entry" items="${statusSummary}">
                        <div class="d-flex justify-content-between py-2 border-bottom border-dark">
                            <span class="text-uppercase text-muted">${entry.key}</span>
                            <strong>${entry.value}</strong>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="col-lg-5">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Top performers</span>
                    </div>
                    <c:if test="${empty topPerformers}">
                        <p class="yt-empty-state">No grading data yet.</p>
                    </c:if>
                    <c:if test="${not empty topPerformers}">
                        <table class="yt-table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Learner</th>
                                <th>Class</th>
                                <th>Avg</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="perf" items="${topPerformers}">
                                <tr>
                                    <td>${perf.rank}</td>
                                    <td>${perf.fullName}</td>
                                    <td>${perf.schoolClass}</td>
                                    <td><fmt:formatNumber value="${perf.averageMark}" maxFractionDigits="1"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>

