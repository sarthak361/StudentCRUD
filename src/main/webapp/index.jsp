<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.model.Student" %>

<!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<!-- ================= POPUP STATUS ================= -->
<%
    String status = request.getParameter("status");

    if ("inserted".equals(status)) {
%>
<script>
    alert("✅ Student inserted successfully!");
</script>
<%
    } else if ("updated".equals(status)) {
%>
<script>
    alert("✏️ Student updated successfully!");
</script>
<%
    } else if ("deleted".equals(status)) {
%>
<script>
    alert("🗑️ Student deleted successfully!");
</script>
<%
    } else if ("duplicate".equals(status) || "duplicateUpdate".equals(status)) {
%>
<script>
    alert("❌ Email already exists. Please use another email.");
</script>
<%
    } else if ("invalidEmail".equals(status)) {
%>
<script>
    alert("❌ Please enter a valid Gmail address (example@gmail.com)");
</script>
<%
    }
%>


<div class="container">

    <h2>Student Management System</h2>

    <!-- ================= INSERT FORM ================= -->
    <form action="<%=request.getContextPath()%>/insertStudent"
          method="post"
          class="student-form">

        <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" required>
        </div>

        <input type="email"
       name="email"
       required
       pattern="^[a-zA-Z0-9._%+-]+@gmail\.com$"
       title="Please enter a valid Gmail address">


        <button type="submit" class="btn-save">Save</button>
    </form>

    <hr>

    <!-- ================= STUDENT TABLE ================= -->
    <table class="student-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
        </thead>

        <tbody>
        <%
            List<Student> students =
                (List<Student>) request.getAttribute("students");

            if (students != null && !students.isEmpty()) {
                for (Student s : students) {
        %>
            <tr>
                <td><%= s.getId() %></td>
                <td><%= s.getName() %></td>
                <td><%= s.getEmail() %></td>
                <td>
                    <a class="btn-edit"
                       href="<%=request.getContextPath()%>/editStudent?id=<%=s.getId()%>">
                        Edit
                    </a>

                    <a class="btn-delete"
                       href="<%=request.getContextPath()%>/deleteStudent?id=<%=s.getId()%>"
                       onclick="return confirm('Are you sure you want to delete?');">
                        Delete
                    </a>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="4">No students found</td>
            </tr>
        <%
        
        
            }
        %>
        </tbody>
    </table>

</div>

</body>
</html>
