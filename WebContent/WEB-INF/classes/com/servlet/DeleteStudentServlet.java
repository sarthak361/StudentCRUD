package com.servlet;

import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/deleteStudent")
public class DeleteStudentServlet extends HttpServlet {
	
    private static final Logger logger =
            LoggerFactory.getLogger("trace");

    private StudentService service = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	
        int id = Integer.parseInt(req.getParameter("id"));
        service.deleteStudent(id);
        logger.info("succesfully deleted:");

        resp.sendRedirect(req.getContextPath() + "/students?status=deleted");
        
    }
}
