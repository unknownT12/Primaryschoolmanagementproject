<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Server Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="text-center mt-5">
<h1 class="display-4 text-danger">500</h1>
<p class="lead">Something went wrong on the server. Please try again later.</p>
<a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
    Return to Dashboard
</a>
</body>
</html>
