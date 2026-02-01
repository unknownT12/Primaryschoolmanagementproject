<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Capture Grade | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4" style="max-width: 700px;">
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">Capture Grade</h3>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">Dashboard</a>
    </div>

    <form action="${pageContext.request.contextPath}/grades" method="post" class="card shadow-sm border-0 mt-3 p-4">
        <div class="mb-3">
            <label class="form-label">Student</label>
            <select name="studentId" class="form-select" required>
                <c:forEach var="student" items="${students}">
                    <option value="${student.id}">${student.name} ${student.surname}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Subject</label>
            <select name="subjectId" class="form-select" required>
                <c:forEach var="subject" items="${subjects}">
                    <option value="${subject.id}">${subject.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Teacher</label>
            <select name="teacherId" class="form-select">
                <option value="">-- Optional --</option>
                <c:forEach var="teacher" items="${teachers}">
                    <option value="${teacher.id}">${teacher.name} ${teacher.surname}</option>
                </c:forEach>
            </select>
        </div>
        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Mark (%)</label>
                <input type="number" name="mark" class="form-control" min="0" max="100" step="0.5" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Term</label>
                <input type="text" name="term" class="form-control" placeholder="e.g. Term 2">
            </div>
        </div>
        <div class="text-end mt-4">
            <button class="btn btn-primary">Save Grade</button>
        </div>
    </form>
</div>
</body>
</html>

