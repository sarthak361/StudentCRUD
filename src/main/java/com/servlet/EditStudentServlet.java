//package com.servlet;
//
//import com.exception.InvalidRequestException;
//import com.exception.StudentNotFoundException;
//import com.model.Student;
//import com.service.StudentService;
//
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/editStudent")
//public class EditStudentServlet extends HttpServlet {
//
//    private StudentService service = new StudentService();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
//
//        // id parameter validation
//        String idParam = req.getParameter("id");
//        if (idParam == null || idParam.isEmpty()) {
//            throw new InvalidRequestException("Student id is missing");
//        }
//
//        int id;
//        try {
//            id = Integer.parseInt(idParam);
//        } catch (NumberFormatException e) {
//            throw new InvalidRequestException("Invalid student id format");
//        }
//
//        //  Student fetch
//        Student s = service.getStudentById(id);
//
//        if (s == null) {
//            throw new StudentNotFoundException(
//                "Student not found with id: " + id
//            );
//        }
//
//        //  Normal success flow
//        try {
//            req.setAttribute("student", s);
//            req.getRequestDispatcher("edit.jsp").forward(req, resp);
//        } catch (Exception e) {
//            throw new RuntimeException("Error while forwarding to edit page");
//        }
//    }
//}
