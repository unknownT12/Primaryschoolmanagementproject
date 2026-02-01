<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${subject.name} | Primary School Admin</title>
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
                <p class="text-muted mb-1">Subject details</p>
                <h2 class="fw-semibold mb-0">${subject.name}</h2>
            </div>
            <a href="${pageContext.request.contextPath}/subjects" class="yt-button">Back to Subjects</a>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-lg-8">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">About this subject</span>
                    </div>
                    <p class="mb-0">${subject.description != null && !subject.description.isEmpty() ? subject.description : 'No description available for this subject.'}</p>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Performance overview</span>
                    </div>
                    <div class="mb-3">
                        <p class="text-muted small mb-1">Average Grade</p>
                        <p class="h4 mb-0">
                            <c:choose>
                                <c:when test="${averageGrade != null}">
                                    <fmt:formatNumber value="${averageGrade}" maxFractionDigits="1"/>%
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="mb-3">
                        <p class="text-muted small mb-1">Students Enrolled</p>
                        <p class="h5 mb-0">${studentCount}</p>
                    </div>
                    <div>
                        <p class="text-muted small mb-1">Total Grades Recorded</p>
                        <p class="h5 mb-0">${gradeCount}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="yt-card">
            <div class="yt-card__header">
                <span class="yt-card__title">Teachers</span>
            </div>
            <c:choose>
                <c:when test="${empty teachers}">
                    <p class="yt-empty-state">No teachers assigned to this subject yet.</p>
                </c:when>
                <c:otherwise>
                    <div class="row g-3">
                        <c:forEach var="teacher" items="${teachers}">
                            <div class="col-md-6 col-lg-4">
                                <div class="yt-card" style="background: var(--yt-surface-alt);">
                                    <p class="fw-semibold mb-1">${teacher.name} ${teacher.surname}</p>
                                    <c:if test="${teacher.qualifications != null && !teacher.qualifications.isEmpty()}">
                                        <p class="text-muted small mb-1">${teacher.qualifications}</p>
                                    </c:if>
                                    <c:if test="${teacher.email != null && !teacher.email.isEmpty()}">
                                        <p class="text-muted small mb-0">${teacher.email}</p>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
</body>
</html>

