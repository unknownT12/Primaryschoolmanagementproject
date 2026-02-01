<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Form | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <c:set var="isEdit" value="${not empty student}"/>
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">
            <c:choose>
                <c:when test="${isEdit}">Edit Student</c:when>
                <c:otherwise>Register Student</c:otherwise>
            </c:choose>
        </h3>
        <a href="${pageContext.request.contextPath}/students" class="btn btn-outline-secondary">Back to list</a>
    </div>

    <c:set var="formAction" value="${isEdit ? 'update' : 'insert'}"/>
    <form action="${pageContext.request.contextPath}/students" method="post" class="card shadow-sm border-0 mt-3 p-4">
        <input type="hidden" name="action" value="${formAction}">
        <c:if test="${isEdit}">
            <input type="hidden" name="id" value="${student.id}">
        </c:if>

        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">First Name</label>
                <input type="text" name="name" class="form-control" value="${student.name}" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Surname</label>
                <input type="text" name="surname" class="form-control" value="${student.surname}" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Birth Certificate No.</label>
                <input type="text" name="birthCertificateNo" class="form-control" value="${student.birthCertificateNo}">
            </div>
            <div class="col-md-3">
                <label class="form-label">Gender</label>
                <select name="gender" class="form-select">
                    <option value="">Select...</option>
                    <option value="Male" <c:if test="${student.gender == 'Male'}">selected</c:if>>Male</option>
                    <option value="Female" <c:if test="${student.gender == 'Female'}">selected</c:if>>Female</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">Date of Birth</label>
                <input type="date" name="dob" class="form-control" value="${student.dob}">
            </div>
            <div class="col-12">
                <label class="form-label">Address</label>
                <input type="text" name="address" class="form-control" value="${student.address}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Guardian Name</label>
                <input type="text" name="guardianName" class="form-control" value="${student.guardianName}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Guardian Contact</label>
                <input type="text" name="guardianContact" class="form-control" value="${student.guardianContact}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Guardian Email</label>
                <input type="email" name="guardianEmail" class="form-control" value="${student.guardianEmail}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Class</label>
                <select name="schoolClass" class="form-select">
                    <option value="">Select class</option>
                    <c:forEach var="className" items="${classOptions}">
                        <option value="${className}" <c:if test="${student.schoolClass == className}">selected</c:if>>
                            ${className}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Status</label>
                <select name="status" class="form-select">
                    <option value="Active" <c:if test="${student.status == 'Active'}">selected</c:if>>Active</option>
                    <option value="Inactive" <c:if test="${student.status == 'Inactive'}">selected</c:if>>Inactive</option>
                    <option value="Transferred" <c:if test="${student.status == 'Transferred'}">selected</c:if>>Transferred</option>
                    <option value="Graduated" <c:if test="${student.status == 'Graduated'}">selected</c:if>>Graduated</option>
                </select>
            </div>
            <div class="col-12">
                <label class="form-label">Remarks / Notes</label>
                <textarea name="remarks" class="form-control" rows="3">${student.remarks}</textarea>
            </div>
        </div>

        <div class="text-end mt-4">
            <button type="submit" class="btn btn-primary">Save Student</button>
        </div>
    </form>
</div>
</body>
</html>
