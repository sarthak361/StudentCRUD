package com.servlet;

import com.exception.InvalidEmailException;
import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/insertStudent")
public class InsertStudentServlet extends HttpServlet {
    private static final Logger logger =
            LoggerFactory.getLogger(InsertStudentServlet.class);
    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
    	logger.info("InsertStudentServlet: Request received");
    	 String name = req.getParameter("name");
    	    String email = req.getParameter("email");

   	    logger.debug("Received data - name: {}, email: {}", name, email);
    	
//    	    //  Name validation
//            if (name == null || name.trim().isEmpty()) {
//                throw new ("Name is required");
//            }

            //  Email validation (gmail only)
            if (!service.isValidGmail(email)) {
                throw new InvalidEmailException(
                    "Invalid Gmail address: " + email
                );
            }

        Student s = new Student();
        s.setName(req.getParameter("name"));
        s.setEmail(req.getParameter("email"));

        service.addStudent(s);
    logger.info("Student inserted successfully with email: {}", email);
        
        resp.sendRedirect(req.getContextPath() + "/students?status=inserted");
    }
}
