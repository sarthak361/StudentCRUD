package com.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exception.DuplicateBenchNumberException;
import com.exception.InvalidEmailException;
import com.model.Address;
import com.model.Benches;
import com.model.Student;
import com.service.StudentService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/insertStudent")
public class InsertStudentServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(InsertStudentServlet.class);
    private final StudentService service = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("InsertStudentServlet: Request received");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String benchNumber = req.getParameter("benchNumber");

        logger.info("Received data - name: {}, email: {}, benchNumber: {}", name, email, benchNumber);

        if (!service.isValidGmail(email)) {
            logger.warn("Invalid Gmail attempt: {}", email);
            resp.sendRedirect(req.getContextPath() + "/students?status=invalidEmail");
            return;
        }

        Student s = new Student();
        s.setName(name);
        s.setEmail(email);

        // Bench handling
        if (benchNumber != null && !benchNumber.trim().isEmpty()) {
            Benches bench = new Benches();
            bench.setBenchNumber(benchNumber.trim());
            s.setBench(bench);
        }

        // Addresses
        String[] cities = req.getParameterValues("city");
        String[] states = req.getParameterValues("state");
        String[] pincodes = req.getParameterValues("pincode");

        if (cities != null && states != null && pincodes != null) {
            for (int i = 0; i < cities.length; i++) {
                String city = (cities[i] != null) ? cities[i].trim() : "";
                String state = (states[i] != null) ? states[i].trim() : "";
                String pincode = (pincodes[i] != null) ? pincodes[i].trim() : "";

                if (city.isEmpty() && state.isEmpty() && pincode.isEmpty()) {
                    continue;
                }

                Address a = new Address();
                a.setCity(city);
                a.setState(state);
                a.setPincode(pincode);
                s.getAddresses().add(a);
            }
        }

        // Save everything
        try {
            int studentId = service.addStudentWithAddressAndBench(s);

            logger.info("Student inserted successfully → ID: {}, email: {}, bench: {}", 
                        studentId, email, (s.getBench() != null ? s.getBench().getBenchNumber() : "none"));

            resp.sendRedirect(req.getContextPath() + "/students?status=inserted");

        } catch (DuplicateBenchNumberException e) {
            logger.warn("Duplicate bench number rejected: {}", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/students?status=duplicateBench");

        } catch (InvalidEmailException e) {
            logger.warn("Invalid email caught: {}", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/students?status=invalidEmail");

        } catch (Exception e) {
            logger.error("Failed to insert student", e);
            resp.sendRedirect(req.getContextPath() + "/students?status=error");
        }
    }
}