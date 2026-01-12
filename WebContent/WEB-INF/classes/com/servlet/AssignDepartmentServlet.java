package com.servlet;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.service.StudentService;

@WebServlet("/assignDepartment")
public class AssignDepartmentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int studentId = Integer.parseInt(req.getParameter("studentId"));
        String deptName = req.getParameter("departmentName");

        if (deptName == null || deptName.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/students?status=error");
            return;
        }

        // Call service to handle department assignment
        service.assignDepartmentToStudent(studentId, deptName.trim());

        resp.sendRedirect(req.getContextPath() + "/students?status=departmentAssigned");
    }
}