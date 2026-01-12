package com.servlet;

import java.io.IOException;

import com.model.Address;
import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/updateStudent")
public class UpdateStudentServlet extends HttpServlet {

    private StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Student s = new Student();
        s.setId(Integer.parseInt(req.getParameter("id")));
        s.setName(req.getParameter("name"));
        s.setEmail(req.getParameter("email")); // hidden se aa raha

        String[] cities = req.getParameterValues("city");
        String[] states = req.getParameterValues("state");
        String[] pins   = req.getParameterValues("pincode");

        for (int i = 0; i < cities.length; i++) {
            Address a = new Address();
            a.setCity(cities[i]);
            a.setState(states[i]);
            a.setPincode(pins[i]);
            s.getAddresses().add(a);
        }

//        service.updateStudentWithAddresses(s);
        resp.sendRedirect("students?status=updated");
    }

}
