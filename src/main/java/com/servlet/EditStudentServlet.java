package com.servlet;

import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/editStudent")
public class EditStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        Student s = service.getStudentById(id);

        req.setAttribute("student", s);
        RequestDispatcher rd = req.getRequestDispatcher("edit.jsp");
        rd.forward(req, resp);
    }
}
