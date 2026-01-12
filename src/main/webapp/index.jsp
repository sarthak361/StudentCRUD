<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.model.Student, com.model.Address, com.model.Benches, com.model.College, com.model.Department" %>

<!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
    <link rel="stylesheet" href="style.css">
</head>

<script>
function addAddress() {
    const box = document.getElementById("addrBox");0
    box.insertAdjacentHTML("beforeend",
        `<div class="addr-row">
            <input name="city" placeholder="City">
            <input name="state" placeholder="State">
            <input name="pincode" placeholder="Pincode">
        </div>`
    );
}

function enableEdit(id) {
    document.querySelectorAll(".text_"+id).forEach(e => e.style.display="none");
    document.querySelectorAll(".input_"+id).forEach(e => e.style.display="inline");
    document.getElementById("editBtn_"+id).style.display="none";
    document.getElementById("saveBtn_"+id).style.display="inline";
}

function prepareSave(id) {
    document.getElementById("hiddenName_"+id).value =
        document.getElementById("nameInput_"+id).value;
    document.getElementById("hiddenEmail_"+id).value =
        document.getElementById("emailInput_"+id).value;
}
</script>

<body>

<%
String status = request.getParameter("status");
if ("inserted".equals(status)) {
%><script>alert("✅ Student inserted successfully");</script><%
} else if ("updated".equals(status)) {
%><script>alert("✏️ Student updated successfully");</script><%
} else if ("deleted".equals(status)) {
%><script>alert("🗑️ Student deleted successfully");</script><%
} else if ("duplicate".equals(status)) {
%><script>alert("❌ Email already exists");</script><%
} else if ("invalidEmail".equals(status)) {
%><script>alert("❌ Invalid Gmail address");</script><%
} else if ("duplicateBench".equals(status)) {
%><script>alert("❌ Duplicate bench number found!\nPlease choose a different bench number.");</script><%
} else if ("collegeAdded".equals(status)) {
%><script>alert("✅ College enrolled successfully!");</script><%
} else if ("departmentAssigned".equals(status)) {
%><script>alert("✅ Department assigned successfully!");</script><%
} else if ("error".equals(status)) {
%><script>alert("⚠️ Something went wrong. Please try again.");</script><%
}
%>

<div class="container">
<h2>Student Management (Address 1:N + Bench 1:1 + College N:N + Department N:1)</h2>

<!-- ===== INSERT STUDENT FORM ===== -->
<form action="<%=request.getContextPath()%>/insertStudent" method="post" class="student-form">

    <input name="name" placeholder="Name" required>
    <input name="email" placeholder="Gmail" pattern="^[A-Za-z0-9._%+-]+@gmail\\.com$" required>

    <input name="benchNumber" placeholder="Bench Number" style="margin-top:10px;">

    <div id="addrBox" style="margin-top:15px;">
        <div class="addr-row">
            <input name="city" placeholder="City">
            <input name="state" placeholder="State">
            <input name="pincode" placeholder="Pincode">
        </div>
    </div>

    <button type="button" onclick="addAddress()" style="margin-top:10px;">+ Add Address</button>
    <button class="btn-save" style="margin-top:10px;">Save Student</button>
</form>

<hr>

<table class="student-table">
<thead>
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Email</th>
    <th>Addresses</th>
    <th>Bench</th>
    <th>Colleges</th>
    <th>Department</th>   
    <th>Action</th>
</tr>
</thead>

<tbody>
<%
List<Student> students = (List<Student>) request.getAttribute("students");
if (students != null && !students.isEmpty()) {
    for (Student s : students) {
%>
<tr>
    <td><%= s.getId() %></td>

    <td>
        <span class="text_<%=s.getId()%>"><%= s.getName() %></span>
        <input class="input_<%=s.getId()%>" id="nameInput_<%=s.getId()%>"
               value="<%= s.getName() %>" style="display:none">
    </td>

    <td>
        <span class="text_<%=s.getId()%>"><%= s.getEmail() %></span>
        <input class="input_<%=s.getId()%>" id="emailInput_<%=s.getId()%>"
               value="<%= s.getEmail() %>" style="display:none">
    </td>

    <td>
    <%
    List<Address> addresses = s.getAddresses();
    if (addresses != null && !addresses.isEmpty()) {
        for (Address a : addresses) {
    %>
        <div><%= a.getCity() %>, <%= a.getState() %> - <%= a.getPincode() %></div>
    <%
        }
    } else {
    %>
        <i>No addresses</i>
    <%
    }
    %>
    </td>

    <td>
    <%
    Benches bench = s.getBench();
    if (bench != null && bench.getBenchNumber() != null && !bench.getBenchNumber().trim().isEmpty()) {
    %>
        <strong><%= bench.getBenchNumber() %></strong>
    <% } else { %>
        <i>No Bench</i>
    <% } %>
    </td>

    <!-- Colleges Column -->
    <td>
    <%
    List<College> colleges = s.getColleges();
    if (colleges != null && !colleges.isEmpty()) {
        for (College c : colleges) {
    %>
        <div><%= c.getName() %> (<%= c.getLocation() %>)</div>
    <%
        }
    } else {
    %>
        <i>No colleges enrolled</i>
    <%
    }
    %>
    <form action="<%=request.getContextPath()%>/addCollegeToStudent" method="post" style="margin-top:10px;">
        <input type="hidden" name="studentId" value="<%= s.getId() %>">
        <input name="collegeName" placeholder="College Name" required style="width:140px;">
        <input name="location" placeholder="Location" style="width:100px;">
        <button type="submit" style="padding:4px 8px; font-size:12px;">Add</button>
    </form>
    </td>

<!-- Department Column -->
<td>
<%
Department dept = s.getDepartment();
if (dept != null && dept.getName() != null) {
%>
    <strong><%= dept.getName() %></strong>
    <% if (dept.getHead() != null && !dept.getHead().trim().isEmpty()) { %>
        <br><small>Head: <%= dept.getHead() %></small>
    <% } %>
<% } else { %>
    <i>No Department</i>
<% } %>

<!-- Assign Department Form (text input instead of select) -->
<form action="<%=request.getContextPath()%>/assignDepartment" method="post" style="margin-top:8px;">
    <input type="hidden" name="studentId" value="<%= s.getId() %>">
    <input type="text" name="departmentName" placeholder="Enter Department Name" required 
           style="width:180px; font-size:12px;" 
           value="<%= dept != null && dept.getName() != null ? dept.getName() : "" %>">
    <button type="submit" style="padding:4px 8px; font-size:12px;">Assign</button>
</form>
</td>

    <td>
        <button id="editBtn_<%=s.getId()%>" onclick="enableEdit(<%=s.getId()%>)">Edit</button>

        <form action="<%=request.getContextPath()%>/updateStudent" method="post" style="display:inline">
            <input type="hidden" name="id" value="<%=s.getId()%>">
            <input type="hidden" name="name" id="hiddenName_<%=s.getId()%>">
            <input type="hidden" name="email" id="hiddenEmail_<%=s.getId()%>">
            <button id="saveBtn_<%=s.getId()%>" style="display:none" onclick="prepareSave(<%=s.getId()%>)">Save</button>
        </form>

        <a class="btn-delete" href="<%=request.getContextPath()%>/deleteStudent?id=<%=s.getId()%>"
           onclick="return confirm('Delete this student? All related data will also be removed.')">
            Delete
        </a>
    </td>
</tr>
<%
    }
} else {
%>
<tr><td colspan="8" style="text-align:center; padding:20px; color:#888;">No students found</td></tr>
<% } %>
</tbody>
</table>
</div>

</body>
</html>