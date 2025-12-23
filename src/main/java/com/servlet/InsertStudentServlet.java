package com.servlet;

import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/insertStudent")
public class InsertStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	String email = req.getParameter("email");

    	if (!service.isValidGmail(email)) {
    	    resp.sendRedirect(req.getContextPath()
    	        + "/students?status=invalidEmail");
    	    return;
    	}

    	
        Student s = new Student();
        s.setName(req.getParameter("name"));
        s.setEmail(req.getParameter("email"));
        

        // 👇 YAHAN DUPLICATE CHECK HANDLE HOTA HAI
        boolean success = service.addStudent(s);

        if (!success) {
            resp.sendRedirect(req.getContextPath()
                + "/students?status=duplicate");
            return;
        }

        resp.sendRedirect(req.getContextPath()
            + "/students?status=inserted");
    }
}
