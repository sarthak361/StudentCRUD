<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.model.Student" %>

<!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
    <link rel="stylesheet" href="style.css">
</head>
<script>
function enableEdit(id) {
    document.getElementById("nameText_" + id).style.display = "none";
    document.getElementById("emailText_" + id).style.display = "none";

    document.getElementById("nameInput_" + id).style.display = "inline";
    document.getElementById("emailInput_" + id).style.display = "inline";

    document.getElementById("editBtn_" + id).style.display = "none";
    document.getElementById("saveBtn_" + id).style.display = "inline";
}

function prepareSave(id) {
    document.getElementById("hiddenName_" + id).value =
        document.getElementById("nameInput_" + id).value;

    document.getElementById("hiddenEmail_" + id).value =
        document.getElementById("emailInput_" + id).value;
}
</script>

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
                <td>
    <span id="nameText_<%=s.getId()%>"><%= s.getName() %></span>
    <input type="text"
           id="nameInput_<%=s.getId()%>"
           value="<%= s.getName() %>"
           style="display:none;">
</td>

<td>
    <span id="emailText_<%=s.getId()%>"><%= s.getEmail() %></span>
    <input type="email"
           id="emailInput_<%=s.getId()%>"
           value="<%= s.getEmail() %>"
           style="display:none;">
</td>

                <td>
<td>
    <button id="editBtn_<%=s.getId()%>"
            onclick="enableEdit(<%=s.getId()%>)">
        Edit
    </button>

    <form action="<%=request.getContextPath()%>/updateStudent"
          method="post"
          style="display:inline;">

        <input type="hidden" name="id" value="<%=s.getId()%>">

        <input type="hidden" name="name"
               id="hiddenName_<%=s.getId()%>">

        <input type="hidden" name="email"
               id="hiddenEmail_<%=s.getId()%>">

        <button type="submit"
                id="saveBtn_<%=s.getId()%>"
                style="display:none;"
                onclick="prepareSave(<%=s.getId()%>)">
            Save
        </button>
    </form>
   


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
