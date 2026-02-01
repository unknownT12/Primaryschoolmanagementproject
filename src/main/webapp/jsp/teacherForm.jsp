<%@ page contentType="text/html;charset=UTF-8" errorPage="/jsp/error500.jsp" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Teacher Form | Primary School Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-4">
    <c:set var="isEdit" value="${not empty teacher}"/>
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h3 class="mb-0">
            <c:choose>
                <c:when test="${isEdit}">Edit Teacher</c:when>
                <c:otherwise>Add Teacher</c:otherwise>
            </c:choose>
        </h3>
        <a href="${pageContext.request.contextPath}/teachers" class="btn btn-outline-secondary">Back to teachers</a>
    </div>

    <c:set var="teacherId" value=""/>
    <c:set var="teacherName" value=""/>
    <c:set var="teacherSurname" value=""/>
    <c:set var="teacherOmang" value=""/>
    <c:set var="teacherGender" value=""/>
    <c:set var="teacherDob" value=""/>
    <c:set var="teacherAddress" value=""/>
    <c:set var="teacherContact" value=""/>
    <c:set var="teacherEmail" value=""/>
    <c:set var="teacherDateJoined" value=""/>
    <c:set var="teacherQualifications" value=""/>
    <c:set var="subjectList" value=""/>
    
    <c:if test="${teacher != null}">
        <c:set var="teacherId" value="${teacher.id}"/>
        <c:set var="teacherName" value="${teacher.name}"/>
        <c:set var="teacherSurname" value="${teacher.surname}"/>
        <c:set var="teacherOmang" value="${teacher.omangOrPassport}"/>
        <c:set var="teacherGender" value="${teacher.gender}"/>
        <c:set var="teacherAddress" value="${teacher.address}"/>
        <c:set var="teacherContact" value="${teacher.contactNo}"/>
        <c:set var="teacherEmail" value="${teacher.email}"/>
        <c:set var="teacherQualifications" value="${teacher.qualifications}"/>
        <c:if test="${teacher.dob != null}">
            <c:set var="teacherDob" value="${teacher.dob}"/>
        </c:if>
        <c:if test="${teacher.dateJoined != null}">
            <c:set var="teacherDateJoined" value="${teacher.dateJoined}"/>
        </c:if>
        <c:if test="${teacher.subjectsQualified != null && not empty teacher.subjectsQualified}">
            <c:set var="subjectList" value="${fn:join(teacher.subjectsQualified, ', ')}"/>
        </c:if>
    </c:if>

    <form action="${pageContext.request.contextPath}/teachers" method="post" class="card shadow-sm border-0 mt-3 p-4">
        <input type="hidden" name="action" value="save">
        <c:if test="${isEdit && not empty teacherId}">
            <input type="hidden" name="id" value="${teacherId}">
        </c:if>

        <div class="row g-3">
            <div class="col-md-4">
                <label class="form-label">First Name</label>
                <input type="text" name="name" class="form-control" value="${teacherName}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Surname</label>
                <input type="text" name="surname" class="form-control" value="${teacherSurname}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Omang / Passport</label>
                <input type="text" name="omangOrPassport" class="form-control" value="${teacherOmang}">
            </div>
            <div class="col-md-3">
                <label class="form-label">Gender</label>
                <select name="gender" class="form-select">
                    <option value="">Select...</option>
                    <option value="Male" <c:if test="${teacherGender == 'Male'}">selected</c:if>>Male</option>
                    <option value="Female" <c:if test="${teacherGender == 'Female'}">selected</c:if>>Female</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">Date of Birth</label>
                <input type="date" name="dob" class="form-control" value="${teacherDob}">
            </div>
            <div class="col-md-6">
                <label class="form-label">Address</label>
                <input type="text" name="address" class="form-control" value="${teacherAddress}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Contact No</label>
                <input type="text" name="contactNo" class="form-control" value="${teacherContact}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" value="${teacherEmail}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Date Joined</label>
                <input type="date" name="dateJoined" class="form-control" value="${teacherDateJoined}">
            </div>
            <div class="col-12">
                <label class="form-label">Qualifications</label>
                <input type="text" name="qualifications" class="form-control" value="${teacherQualifications}">
            </div>
            <div class="col-12">
                <label class="form-label">Subjects Qualified (comma separated)</label>
                <input type="text" name="subjectsQualified" class="form-control" value="${subjectList}">
            </div>
        </div>

        <div class="text-end mt-4">
            <button class="btn btn-primary">Save Teacher</button>
        </div>
    </form>
</div>
</body>
</html>

