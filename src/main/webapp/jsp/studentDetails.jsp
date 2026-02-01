<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${student.name} ${student.surname} | Primary School Admin</title>
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
            <div class="d-flex gap-2">
                <c:if test="${sessionScope.role != 'PARENT'}">
                    <a href="${pageContext.request.contextPath}/students?action=edit&id=${student.id}" class="yt-table-actions">Edit</a>
                    <a href="${pageContext.request.contextPath}/students?action=delete&id=${student.id}"
                       class="yt-table-actions"
                       onclick="return confirm('Delete ${student.name}?');">Delete</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/students" class="yt-button">Back to list</a>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-lg-6">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Personal details</span>
                    </div>
                    <p><strong>Gender:</strong> ${student.gender}</p>
                    <p><strong>DOB:</strong> ${student.dob}</p>
                    <p><strong>Address:</strong> ${student.address}</p>
                    <hr>
                    <p class="text-muted text-uppercase small mb-1">Guardian</p>
                    <p class="mb-0"><strong>Name:</strong> ${student.guardianName}</p>
                    <p class="mb-0"><strong>Contact:</strong> ${student.guardianContact}</p>
                    <p><strong>Email:</strong> ${student.guardianEmail}</p>
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
                                <fmt:formatNumber value="${average}" maxFractionDigits="1"/>
                            </c:when>
                            <c:otherwise>n/a</c:otherwise>
                        </c:choose>
                    </p>
                    <c:if test="${sessionScope.role != 'PARENT'}">
                        <hr>
                        <form action="${pageContext.request.contextPath}/students" method="post" class="row g-2 align-items-end">
                            <input type="hidden" name="action" value="assignClass">
                            <input type="hidden" name="studentId" value="${student.id}">
                            <div class="col-md-8">
                                <label class="form-label small text-muted mb-1">Assign to class</label>
                                <select name="className" class="form-select">
                                    <c:forEach var="className" items="${classOptions}">
                                        <option value="${className}" <c:if test="${className == student.schoolClass}">selected</c:if>>${className}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <button class="yt-button w-100">Update class</button>
                            </div>
                        </form>
                    </c:if>
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

        <div class="row g-3">
            <div class="col-lg-7">
                <div class="yt-card">
                    <div class="yt-card__header">
                        <span class="yt-card__title">Grade history</span>
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
                                    <th>Mark</th>
                                    <th>Term</th>
                                    <th>Teacher</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="g" items="${grades}">
                                    <tr>
                                        <td>${g.subjectName}</td>
                                        <td>${g.mark}</td>
                                        <td>${g.term}</td>
                                        <td>${g.teacherName}</td>
                                        <td>${g.createdAt}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <c:if test="${sessionScope.role != 'PARENT'}">
                <div class="col-lg-5">
                    <div class="yt-card">
                        <div class="yt-card__header">
                            <span class="yt-card__title">Add grade</span>
                        </div>
                        <form action="${pageContext.request.contextPath}/grades" method="post" class="row g-2">
                            <input type="hidden" name="studentId" value="${student.id}">
                            <input type="hidden" name="redirectTo" value="${pageContext.request.contextPath}/studentDetails?id=${student.id}">
                            <div class="col-12">
                                <label class="form-label small text-muted">Subject</label>
                                <select name="subjectId" class="form-select" required>
                                    <c:forEach var="subject" items="${subjects}">
                                        <option value="${subject.id}">${subject.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-12">
                                <label class="form-label small text-muted">Teacher</label>
                                <select name="teacherId" class="form-select">
                                    <option value="">-- Optional --</option>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <option value="${teacher.id}">${teacher.name} ${teacher.surname}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Mark</label>
                                <input type="number" name="mark" class="form-control" min="0" max="100" step="0.5" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Term</label>
                                <input type="text" name="term" class="form-control" placeholder="e.g. Term 1">
                            </div>
                            <div class="col-12 text-end">
                                <button class="yt-button">Save grade</button>
                            </div>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>

        <div class="yt-card">
            <div class="yt-card__header">
                <span class="yt-card__title">Remarks</span>
            </div>
            <c:if test="${empty student.remarks}">
                <p class="yt-empty-state">No remarks recorded.</p>
            </c:if>
            <c:if test="${not empty student.remarks}">
                <p class="mb-0">${student.remarks}</p>
            </c:if>
        </div>
    </main>
</div>
</body>
</html>

