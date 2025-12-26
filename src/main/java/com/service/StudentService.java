package com.service;

import org.slf4j.Logger;


import java.util.List;
import com.dao.StudentDAO;
import com.exception.DuplicateEmailException;
import com.exception.InvalidEmailException;
import com.model.Student;
import org.slf4j.LoggerFactory;




public class StudentService {
    private static final Logger log =
            LoggerFactory.getLogger(StudentService.class);
	
	

    StudentDAO dao = new StudentDAO();
//valid gmail
    public boolean isValidGmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$");
    }

    // CREATE
    public boolean addStudent(Student s) {
    	//   traceLogger.info("Adding student with email {}", s.getEmail());
    	
        if (dao.isEmailExists(s.getEmail())) {
        	 log.warn("Invalid email: {}", s.getEmail());
        	   throw new DuplicateEmailException(
                       "Email already exists. Please use another email.");
        }

        dao.insert(s);
        return true;
    }


    // READ
    public List<Student> getAllStudents() {
        return dao.getAll();
    }

    // DELETE
    public boolean deleteStudent(int id) {
        dao.delete(id);
		return false;
    }
    

    // UPDATE
    public void updateStudent(Student s) {

        if (!isValidGmail(s.getEmail())) {
        	log.warn("Invalid email: {}", s.getEmail());
        
            throw new InvalidEmailException("Invalid Gmail: " + s.getEmail());
        }

        if (dao.isEmailExistsForOtherStudent(s.getId(), s.getEmail())) {
        	log.warn("duplicate email: {}", s.getEmail());
            throw new DuplicateEmailException("Duplicate email: " + s.getEmail());
        }

        dao.update(s);
    }


    // READ BY ID 
    public Student getStudentById(int id) {
        return dao.getById(id);
    }
}
