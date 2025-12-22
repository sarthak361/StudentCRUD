<%@ page import="com.model.Student" %>

<%
    Student s = (Student) request.getAttribute("student");
%>

<h2>Update Student</h2>

<form action="student" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="<%=s.getId()%>">

    Name:
    <input type="text" name="name" value="<%=s.getName()%>" required><br><br>

    Email:
    <input type="email" name="email" value="<%=s.getEmail()%>" required><br><br>

    <button type="submit">Update</button>
</form>
