<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Students | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/yt.css">
</head>
<body class="yt-body">
<%@ include file="/jsp/fragments/topNav.jspf" %>
<div class="yt-shell">
    <%@ include file="/jsp/fragments/sideNav.jspf" %>
    <main class="yt-main">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div>
                <p class="text-muted mb-1">Directory</p>
                <h2 class="fw-semibold">Students</h2>
            </div>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/dashboard" class="yt-table-actions">Dashboard</a>
                <a href="${pageContext.request.contextPath}/students?action=new" class="yt-button">+ Add Student</a>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/students" method="get" class="yt-card">
            <input type="hidden" name="action" value="list">
            <div class="row g-3">
                <div class="col-md-3">
                    <label class="form-label small text-muted mb-1">Search keyword</label>
                    <input type="text" name="keyword" class="form-control" placeholder="Name, certificate no..."
                           value="${keyword}">
                </div>
                <div class="col-md-3">
                    <label class="form-label small text-muted mb-1">Status</label>
                    <c:set var="statusOptions" value="${fn:split('Active,Inactive,Transferred,Graduated', ',')}"/>
                    <select name="status" class="form-select">
                        <option value="">All statuses</option>
                        <c:forEach var="statusOption" items="${statusOptions}">
                            <option value="${statusOption}" <c:if test="${statusOption == selectedStatus}">selected</c:if>>
                                ${statusOption}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label small text-muted mb-1">Class</label>
                    <select name="classFilter" class="form-select">
                        <option value="">All classes</option>
                        <c:forEach var="className" items="${classOptions}">
                            <option value="${className}" <c:if test="${className == selectedClass}">selected</c:if>>
                                ${className}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label small text-muted mb-1">Sort by</label>
                    <select name="sort" class="form-select">
                        <option value="">Surname</option>
                        <option value="class" <c:if test="${selectedSort == 'class'}">selected</c:if>>Class</option>
                        <option value="status" <c:if test="${selectedSort == 'status'}">selected</c:if>>Status</option>
                        <option value="recent" <c:if test="${selectedSort == 'recent'}">selected</c:if>>Most recent</option>
                    </select>
                </div>
            </div>
            <div class="text-end mt-3">
                <button class="yt-button me-2" type="submit">Apply</button>
                <a href="${pageContext.request.contextPath}/students" class="yt-table-actions">Reset</a>
            </div>
        </form>

        <div class="yt-card">
            <c:choose>
                <c:when test="${empty students}">
                    <p class="yt-empty-state">No students match the current filters.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="yt-table">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Birth Certificate</th>
                                <th>Class</th>
                                <th>Status</th>
                                <th>Guardian</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="s" items="${students}">
                                <tr>
                                    <td>${s.name} ${s.surname}</td>
                                    <td>${s.birthCertificateNo}</td>
                                    <td>${s.schoolClass}</td>
                                    <td><span class="badge bg-primary">${s.status}</span></td>
                                    <td>${s.guardianName}</td>
                                    <td class="yt-table-actions">
                                        <a href="${pageContext.request.contextPath}/studentDetails?id=${s.id}">View</a>
                                        <c:if test="${sessionScope.role != 'PARENT'}">
                                            <a href="${pageContext.request.contextPath}/students?action=edit&id=${s.id}">Edit</a>
                                            <a href="${pageContext.request.contextPath}/students?action=delete&id=${s.id}"
                                               onclick="return confirm('Delete ${s.name}?');">Delete</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>
</body>
</html>
