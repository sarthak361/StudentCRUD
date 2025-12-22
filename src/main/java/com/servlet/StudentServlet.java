package com.servlet;

import com.dao.StudentDAO;
import com.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    StudentDAO dao = new StudentDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
  

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            dao.deleteStudent(id);
            resp.sendRedirect("index.jsp");
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Student s = dao.getStudentById(id);

            req.setAttribute("student", s);
            RequestDispatcher rd = req.getRequestDispatcher("edit.jsp");
            rd.forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("insert".equals(action)) {
            Student s = new Student();
            s.setName(req.getParameter("name"));
            s.setEmail(req.getParameter("email"));
            dao.insertStudent(s);
        }

        if ("update".equals(action)) {
            Student s = new Student();
            s.setId(Integer.parseInt(req.getParameter("id")));
            s.setName(req.getParameter("name"));
            s.setEmail(req.getParameter("email"));
            dao.updateStudent(s);
        }

        resp.sendRedirect("index.jsp");
    }
}
