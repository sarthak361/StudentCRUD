package com.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.model.College;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addCollegeToStudent")
public class AddCollegeToStudentServlet extends HttpServlet {
	   private static final Logger logger = LoggerFactory.getLogger(InsertStudentServlet.class);
    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int studentId = Integer.parseInt(req.getParameter("studentId"));
        String name = req.getParameter("collegeName");
        String location = req.getParameter("location");

        College college = new College(location, location);
        college.setName(name);
        college.setLocation(location);
logger.info("collage succesfully inserted:{} , student id is:{}",name,studentId);
        service.addCollegeToStudent(studentId, college);
        
        

        resp.sendRedirect(req.getContextPath() + "/students?status=collegeAdded");
    }
}