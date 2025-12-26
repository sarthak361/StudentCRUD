<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
</head>
<body>

<h2>Something went wrong</h2>

<p>
    Error Message:
    <b><%= request.getAttribute("jakarta.servlet.error.message") %></b>
</p>

<p>
    Status Code:
    <b><%= request.getAttribute("jakarta.servlet.error.status_code") %></b>
</p>

<a href="<%=request.getContextPath()%>/students">Go Back</a>

</body>
</html>
