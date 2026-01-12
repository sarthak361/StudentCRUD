package com.servlet;

import java.io.IOException;
import java.util.List;

import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class ListStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        // used to fetch data 
        List<Student> students = service.getAllStudents(); 

       
        req.setAttribute("students", students);
//
//     
//        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        resp.setHeader("Pragma", "no-cache");
//        resp.setDateHeader("Expires", 0);

        // Forward to JSP
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}