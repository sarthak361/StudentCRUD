<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial;
            background-color: #f4f6f8;
        }
        .error-box {
            width: 60%;
            margin: 100px auto;
            padding: 25px;
            background: #fff;
            border-left: 6px solid #e74c3c;
        }
        h2 {
            color: #e74c3c;
        }
    </style>
</head>
<body>

<div class="error-box">

    <%
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String title = (String) request.getAttribute("errorTitle");
        String message = (String) request.getAttribute("errorMessage");
    %>

    <% if (statusCode != null) { %>

        <% if (statusCode == 404) { %>
            <h2>404 Page Not Found</h2>
            <p>The page you are trying to access does not exist.</p>

        <% } else if (statusCode == 405) { %>
            <h2>405 Method Not Allowed</h2>
            <p>This HTTP method is not supported for this URL.</p>

        <% } else if (statusCode == 500) { %>
            <h2>500 Internal Server Error</h2>
            <p>Server encountered an unexpected condition.</p>
        <% } %>

    <% } else { %>

        <h2><%= title %></h2>
        <p><%= message %></p>

    <% } %>

    <br>
    <a href="<%=request.getContextPath()%>/students">⬅ Go Back</a>

</div>

</body>
</html>
