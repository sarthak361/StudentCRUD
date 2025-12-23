package com.servlet;

import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {
	

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

    	String idStr = req.getParameter("id");
    	if (idStr == null || idStr.isEmpty()) {
    	    throw new RuntimeException("Student id missing for update");
    	}

    	int id = Integer.parseInt(idStr);

        Student s = new Student();
        s.setId(Integer.parseInt(req.getParameter("id")));
        s.setName(req.getParameter("name"));
        s.setEmail(req.getParameter("email"));
        boolean success = service.updateStudent(s);

        if (!success) {
            // ❌ duplicate email
            resp.sendRedirect(req.getContextPath()
                + "/students?status=duplicateUpdate");
            return;
        }

        // ✅ success
        resp.sendRedirect(req.getContextPath()
            + "/students?status=updated");


        service.updateStudent(s);
        resp.sendRedirect(req.getContextPath() + "/students?status=updated");
    }
}
