<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Assign Classes | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h3>Assign classes to ${teacher.name} ${teacher.surname}</h3>
    <a href="${pageContext.request.contextPath}/teachers" class="btn btn-link">&larr; Back to teachers</a>

    <form action="${pageContext.request.contextPath}/teachers" method="post" class="card shadow-sm border-0 mt-3 p-4" id="assignForm">
        <input type="hidden" name="action" value="assign">
        <input type="hidden" name="id" value="${teacher.id}">

        <select id="classOptionsSeed" class="d-none">
            <option value="">Select class</option>
            <c:forEach var="className" items="${classOptions}">
                <option value="${className}">${className}</option>
            </c:forEach>
        </select>

        <div id="assignRows">
            <c:forEach var="entry" items="${teacher.classToSubject}">
                <div class="row g-2 align-items-end mb-2 assign-row">
                    <div class="col-md-5">
                        <label class="form-label small text-muted">Class</label>
                        <select name="className" class="form-select">
                            <option value="">Select class</option>
                            <c:forEach var="className" items="${classOptions}">
                                <option value="${className}" <c:if test="${className == entry.key}">selected</c:if>>${className}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-5">
                        <label class="form-label small text-muted">Subject</label>
                        <input type="text" name="subjectName" class="form-control" value="${entry.value}" placeholder="Subject taught">
                    </div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-outline-danger w-100 remove-row">Remove</button>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty teacher.classToSubject}">
                <div class="row g-2 align-items-end mb-2 assign-row">
                    <div class="col-md-5">
                        <label class="form-label small text-muted">Class</label>
                        <select name="className" class="form-select">
                            <option value="">Select class</option>
                            <c:forEach var="className" items="${classOptions}">
                                <option value="${className}">${className}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-5">
                        <label class="form-label small text-muted">Subject</label>
                        <input type="text" name="subjectName" class="form-control" placeholder="Subject taught">
                    </div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-outline-danger w-100 remove-row">Remove</button>
                    </div>
                </div>
            </c:if>
        </div>

        <button type="button" class="btn btn-outline-primary mt-2" id="addRowBtn">+ Add another class</button>

        <div class="text-end mt-4">
            <button class="btn btn-success">Save assignments</button>
        </div>
    </form>
</div>

<script>
    const optionsHTML = document.getElementById('classOptionsSeed').innerHTML;
    const buildRow = () => `
        <div class="row g-2 align-items-end mb-2 assign-row">
            <div class="col-md-5">
                <label class="form-label small text-muted">Class</label>
                <select name="className" class="form-select">${optionsHTML}</select>
            </div>
            <div class="col-md-5">
                <label class="form-label small text-muted">Subject</label>
                <input type="text" name="subjectName" class="form-control" placeholder="Subject taught">
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-outline-danger w-100 remove-row">Remove</button>
            </div>
        </div>`;

    document.getElementById('addRowBtn').addEventListener('click', () => {
        document.getElementById('assignRows').insertAdjacentHTML('beforeend', buildRow());
    });

    document.getElementById('assignRows').addEventListener('click', (e) => {
        if (e.target.classList.contains('remove-row')) {
            const row = e.target.closest('.assign-row');
            row.parentNode.removeChild(row);
        }
    });
</script>
</body>
</html>

