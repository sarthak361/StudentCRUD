<%@ page import="java.util.*, com.dao.StudentDAO, com.model.Student" %>

<form action="student" method="post">
    <input type="hidden" name="action" value="insert">
    Name: <input type="text" name="name" required>
    Email: <input type="email" name="email" required>
    <button type="submit">Save</button>
</form>

<hr>

<table border="1">
<tr>
    <th>ID</th><th>Name</th><th>Email</th><th>Action</th>
</tr>

<%
    StudentDAO dao = new StudentDAO();
    List<Student> list = dao.getAllStudents();
    for(Student s : list){
%>
<tr>
    <td><%= s.getId() %></td>
    <td><%= s.getName() %></td>
    <td><%= s.getEmail() %></td>
    <td>
        <a href="student?action=delete&id=<%=s.getId()%>">Delete</a>
    </td>
    <td>
    <a href="student?action=edit&id=<%=s.getId()%>">Edit</a> |
    <a href="student?action=delete&id=<%=s.getId()%>"
       onclick="return confirm('Are you sure?');">Delete</a>
</td>
    
</tr>
<% } %>
</table>
