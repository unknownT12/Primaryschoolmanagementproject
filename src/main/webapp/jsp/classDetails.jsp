<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${className} | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">Class: ${className}</h3>
        <a href="${pageContext.request.contextPath}/classes" class="btn btn-outline-secondary">Back to classes</a>
    </div>

    <div class="row g-3 mt-1">
        <div class="col-lg-7">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white">
                    <strong>Enrolled students</strong>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${empty students}">
                            <p class="text-muted text-center my-4">No students assigned to this class.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th>Name</th>
                                    <th>Status</th>
                                    <th>Remarks</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="s" items="${students}">
                                    <tr>
                                        <td>${s.name} ${s.surname}</td>
                                        <td><span class="badge bg-primary">${s.status}</span></td>
                                        <td>${s.remarks}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white">
                    <strong>Top 5 performers</strong>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${empty topStudents}">
                            <p class="text-muted text-center my-4">No grading data available.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Student</th>
                                    <th>Avg</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="perf" items="${topStudents}">
                                    <tr>
                                        <td>${perf.rank}</td>
                                        <td>${perf.fullName}</td>
                                        <td><fmt:formatNumber value="${perf.averageMark}" maxFractionDigits="1"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
