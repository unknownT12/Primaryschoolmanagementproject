<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${student.name} ${student.surname} | Parent View</title>
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
                <p class="text-muted mb-1">Student profile</p>
                <h2 class="fw-semibold mb-0">${student.name} ${student.surname}</h2>
                <small class="text-muted">Birth certificate: ${student.birthCertificateNo}</small>
            </div>
            <a href="${pageContext.request.contextPath}/search" class="yt-button">Back to search</a>
        </div>

        <div class="row g-3">
            <div class="col-lg-6">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Personal details</span>
                    </div>
                    <p><strong>Gender:</strong> ${student.gender}</p>
                    <p><strong>Date of birth:</strong> ${student.dob}</p>
                    <p><strong>Address:</strong> ${student.address}</p>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Academic snapshot</span>
                    </div>
                    <p><strong>Class:</strong> ${student.schoolClass}</p>
                    <p><strong>Status:</strong> <span class="badge bg-primary">${student.status}</span></p>
                    <p><strong>Registered:</strong> ${student.registrationDate}</p>
                    <p><strong>Average grade:</strong>
                        <c:choose>
                            <c:when test="${average != null}">
                                <fmt:formatNumber value="${average}" maxFractionDigits="1"/>%
                            </c:when>
                            <c:otherwise>n/a</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </div>

        <div class="yt-card">
            <div class="yt-card__header">
                <span class="yt-card__title">Subjects currently taking</span>
            </div>
            <c:if test="${empty enrolledSubjects}">
                <p class="yt-empty-state">No subjects recorded yet.</p>
            </c:if>
            <c:if test="${not empty enrolledSubjects}">
                <div class="yt-tag-cloud">
                    <c:forEach var="subjectName" items="${enrolledSubjects}">
                        <span class="yt-tag">${subjectName}</span>
                    </c:forEach>
                </div>
            </c:if>
        </div>

        <div class="yt-card">
            <div class="yt-card__header">
                <span class="yt-card__title">Academic progress</span>
            </div>
            <c:choose>
                <c:when test="${empty grades}">
                    <p class="yt-empty-state">No grades recorded.</p>
                </c:when>
                <c:otherwise>
                    <table class="yt-table">
                        <thead>
                        <tr>
                            <th>Subject</th>
                            <th>Mark (%)</th>
                            <th>Term</th>
                            <th>Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="g" items="${grades}">
                            <tr>
                                <td>${g.subjectName}</td>
                                <td>${g.mark}</td>
                                <td>${g.term}</td>
                                <td>${g.createdAt}</td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
</body>
</html>

