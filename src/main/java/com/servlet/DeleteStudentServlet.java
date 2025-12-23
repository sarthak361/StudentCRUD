package com.servlet;

import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteStudent")
public class DeleteStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        service.deleteStudent(id);

        resp.sendRedirect(req.getContextPath() + "/students?status=deleted");
    }
}
