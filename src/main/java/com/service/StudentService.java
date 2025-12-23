package com.service;

import java.util.List;
import com.dao.StudentDAO;
import com.model.Student;

public class StudentService {
	
	

    StudentDAO dao = new StudentDAO();
//valid gmail
    public boolean isValidGmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$");
    }

    // CREATE
    public boolean addStudent(Student s) {

        if (dao.isEmailExists(s.getEmail())) {
            return false; // duplicate
        }

        dao.insert(s);
        return true;
    }


    // READ
    public List<Student> getAllStudents() {
        return dao.getAll();
    }

    // DELETE
    public void deleteStudent(int id) {
        dao.delete(id);
    }
    

    // UPDATE
    public boolean updateStudent(Student s) {

        // ❌ duplicate email check
        if (dao.isEmailExistsForOtherStudent(s.getId(), s.getEmail())) {
            return false;
        }

        // ✅ safe update
        dao.update(s);
        return true;
    }


    // READ BY ID (for edit)
    public Student getStudentById(int id) {
        return dao.getById(id);
    }
}
