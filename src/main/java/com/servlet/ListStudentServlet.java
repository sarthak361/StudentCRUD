package com.servlet;

import java.io.IOException;

import com.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class ListStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("students", service.getAllStudents());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
