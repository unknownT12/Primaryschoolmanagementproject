<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/yt.css">
</head>
<body class="yt-body">
<c:set var="isParent" value="${sessionScope.role == 'PARENT'}"/>
<%@ include file="/jsp/fragments/topNav.jspf" %>
<div class="yt-shell">
    <%@ include file="/jsp/fragments/sideNav.jspf" %>
    <main class="yt-main">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
            <div>
                <p class="text-muted mb-1">Global search</p>
                <h2 class="fw-semibold">Find anything</h2>
            </div>
        </div>

        <form class="yt-card d-flex gap-3 align-items-center" action="${pageContext.request.contextPath}/search" method="get">
            <input type="text" name="q" class="form-control" placeholder="Type to search..." value="${keyword}" required>
            <button class="yt-button" type="submit">Search</button>
        </form>

        <c:if test="${not empty keyword}">
            <p class="text-muted mt-2">Showing results for "<strong>${keyword}</strong>"</p>
        </c:if>

        <div class="row g-3 mt-1">
            <div class="col-md-6">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Students</span>
                    </div>
                    <c:if test="${empty studentResults}">
                        <p class="yt-empty-state">No students found.</p>
                    </c:if>
                    <c:forEach var="student" items="${studentResults}">
                        <c:choose>
                            <c:when test="${isParent}">
                                <c:set var="avg" value="${studentAverages[student.id]}"/>
                                <div class="border-bottom border-dark py-2">
                                    <p class="mb-1 fw-semibold">${student.name} ${student.surname}</p>
                                    <small class="text-muted d-block">Class: ${student.schoolClass}</small>
                                    <small class="text-muted d-block">Date of birth: ${student.dob}</small>
                                    <small class="text-muted d-block">
                                        Academic progress:
                                        <c:choose>
                                            <c:when test="${avg != null}">
                                                <fmt:formatNumber value="${avg}" maxFractionDigits="1"/>%
                                            </c:when>
                                            <c:otherwise>n/a</c:otherwise>
                                        </c:choose>
                                    </small>
                                    <a href="${pageContext.request.contextPath}/studentDetails?id=${student.id}" class="small text-decoration-none text-muted">View detailed progress</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="border-bottom border-dark py-2">
                                    <p class="mb-0">${student.name} ${student.surname}</p>
                                    <small class="text-muted">Class: ${student.schoolClass} &middot; Status: ${student.status}</small><br>
                                    <a href="${pageContext.request.contextPath}/studentDetails?id=${student.id}" class="small text-decoration-none">View profile</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
            <c:if test="${not isParent}">
                <div class="col-md-6">
                    <div class="yt-card">
                        <div class="yt-card__header">
                            <span class="yt-card__title">Teachers</span>
                        </div>
                        <c:if test="${empty teacherResults}">
                            <p class="yt-empty-state">No teachers found.</p>
                        </c:if>
                        <c:forEach var="teacher" items="${teacherResults}">
                            <div class="border-bottom border-dark py-2">
                                <p class="mb-0">${teacher.name} ${teacher.surname}</p>
                                <small class="text-muted">${teacher.email}</small>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>

        <c:if test="${not isParent}">
            <div class="row g-3 mt-1">
                <div class="col-md-6">
                    <div class="yt-card">
                        <div class="yt-card__header">
                            <span class="yt-card__title">Subjects</span>
                        </div>
                        <c:if test="${empty subjectResults}">
                            <p class="yt-empty-state">No subjects found.</p>
                        </c:if>
                        <c:forEach var="subject" items="${subjectResults}">
                            <div class="border-bottom border-dark py-2">
                                <p class="mb-0">${subject.name}</p>
                                <small class="text-muted">${subject.description}</small>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="yt-card">
                        <div class="yt-card__header">
                            <span class="yt-card__title">Classes</span>
                        </div>
                        <c:if test="${empty classResults}">
                            <p class="yt-empty-state">No classes found.</p>
                        </c:if>
                        <c:forEach var="className" items="${classResults}">
                            <div class="border-bottom border-dark py-2 d-flex justify-content-between">
                                <span>${className}</span>
                                <a href="${pageContext.request.contextPath}/classes?name=${className}" class="small text-decoration-none">View roster</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>
    </main>
</div>
</body>
</html>

