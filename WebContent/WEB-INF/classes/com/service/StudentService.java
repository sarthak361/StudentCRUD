package com.service;

import java.util.List;

import com.dao.StudentDAO;
import com.dao.AddressDAO;
import com.dao.BenchDAO;
import com.dao.CollegeDAO;
import com.dao.DepartmentDAO;
import com.exception.DatabaseOperationException;
import com.exception.DuplicateBenchNumberException;
import com.exception.DuplicateEmailException;
import com.exception.InvalidEmailException;
import com.exception.StudentNotFoundException;
import com.model.Address;
import com.model.Benches;
import com.model.College;
import com.model.Department;
import com.model.Student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private StudentDAO studentDAO = new StudentDAO();
    private AddressDAO addressDAO = new AddressDAO();
    private BenchDAO benchDAO = new BenchDAO();
    private CollegeDAO collegeDAO = new CollegeDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    // ================= EMAIL VALIDATION =================
    public boolean isValidGmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$");
    }

    // CREATE STUDENT + ADDRESS + BENCH
    public int addStudentWithAddressAndBench(Student student) {
        // Validate email
        if (!isValidGmail(student.getEmail())) {
            throw new InvalidEmailException("Invalid Gmail address");
        }

        // Check duplicate email
        if (studentDAO.isEmailExists(student.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        // 1. Insert student and get ID
        int studentId = studentDAO.insertAndReturnId(student);

        // 2. Insert addresses
        for (Address addr : student.getAddresses()) {
            addr.setStudentId(studentId);
            addressDAO.insert(addr);
        }

        // 3. Insert bench if present (with duplicate check in BenchDAO)
        if (student.getBench() != null && student.getBench().getBenchNumber() != null) {
            Benches bench = student.getBench();
            bench.setStudentId(studentId);
            benchDAO.insertBench(bench);  // This will throw DuplicateBenchNumberException if duplicate
        }

        return studentId;
    }

    // ADD COLLEGE TO STUDENT (Many-to-Many)
    public void addCollegeToStudent(int studentId, College college) {
        // Insert college and get ID
        int collegeId = collegeDAO.insertCollege(college);

        // Link student to college
        studentDAO.insertStudentCollege(studentId, collegeId);
    }

    // GET ALL STUDENTS WITH ALL DETAILS
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudentsWithDetails();
    }
    
    
    

    // DELETE STUDENT
    public void deleteStudent(int id) {
        // Delete addresses, bench, college links (use CASCADE in DB or manual deletes)
        addressDAO.deleteByStudentId(id);
        benchDAO.deleteByStudentId(id);
//        collegeDAO.deleteLinksByStudentId(id);  // Add this method in CollegeDAO if needed
        studentDAO.delete(id);
        log.info("Student deleted, id={}", id);
    }

    // UPDATE STUDENT + ADDRESSES
    public void updateStudent(Student s) {
        if (!isValidGmail(s.getEmail())) {
            throw new InvalidEmailException("Invalid Gmail");
        }

        if (studentDAO.isEmailExists(s.getEmail())) {
            throw new DuplicateEmailException("Duplicate email");
        }

        studentDAO.update(s);

        // Update addresses: delete old, insert new
        addressDAO.deleteByStudentId(s.getId());
        for (Address a : s.getAddresses()) {
            a.setStudentId(s.getId());
            addressDAO.insert(a);
        }
    }

    public void assignDepartmentToStudent(int studentId, String departmentName) 
            throws DatabaseOperationException {

        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be empty");
        }

        String trimmedName = departmentName.trim();

        try {
            Department existingDept = departmentDAO.findByName(trimmedName);

            int deptId;
            if (existingDept != null) {
                deptId = existingDept.getId();
            } else {
                Department newDept = new Department();
                newDept.setName(trimmedName);
                deptId = departmentDAO.insertDepartment(newDept);
            }

            studentDAO.updateDepartmentId(studentId, deptId);

            log.info("Assigned department ID {} to student {}", deptId, studentId);

        } catch (Exception e) {
            log.error("Failed to assign department: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to assign department", e);
        }
    }
}