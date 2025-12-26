package com.servlet;

import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {

    private static final Logger log =
        LoggerFactory.getLogger(UpdateStudentServlet.class);

    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        log.info("UpdateStudent request received");

        Student s = new Student();
        s.setId(Integer.parseInt(req.getParameter("id")));
        s.setName(req.getParameter("name"));
        s.setEmail(req.getParameter("email"));

        log.debug("Updating student id={}", s.getId());

        // exception yahin se aayegi
        service.updateStudent(s);

        log.info("Student updated successfully, id={}", s.getId());

        try {
            resp.sendRedirect(req.getContextPath() + "/students");
        } catch (Exception e) {
            log.error("Redirect failed after update", e);
            throw new RuntimeException("Redirect failed", e);
        }
    }
}
