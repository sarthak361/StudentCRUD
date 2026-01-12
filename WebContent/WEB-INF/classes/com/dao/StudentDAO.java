package com.dao;

import java.sql.*;
import java.util.*;

import com.model.Student;
import com.model.Address;
import com.model.Benches;
import com.model.College;
import com.model.Department;
import com.exception.InvalidEmailException;
import com.exception.StudentNotFoundException;
import com.exception.DatabaseOperationException;      
import com.exception.DuplicateLinkException;          
import com.exception.DataFetchException;               

public class StudentDAO {

    //  INSERT STUDENT AND RETURN GENERATED ID 
    public int insertAndReturnId(Student s) throws DatabaseOperationException {
        String sql = "INSERT INTO students (name, email, department_id) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setObject(3, s.getDepartment() != null ? s.getDepartment().getId() : null);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Student insert failed - no rows affected");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new DatabaseOperationException("No generated ID returned for student");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error during student insert: " + e.getMessage());
        }
    }

    //  CHECK IF EMAIL ALREADY EXISTS
    public boolean isEmailExists(String email) throws InvalidEmailException {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new InvalidEmailException("Failed to check email existence: " + e.getMessage());
        }
    }

    // ── INSERT BENCH FOR STUDENT (One-to-One) 
    public void insertBenchForStudent(int studentId, Benches bench) throws DatabaseOperationException {
        String sql = "INSERT INTO benches (bench_number, student_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bench.getBenchNumber());
            ps.setInt(2, studentId);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new DatabaseOperationException("Bench insert failed - no rows affected");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bench.setId(rs.getInt(1));
                } else {
                    throw new DatabaseOperationException("No generated ID returned for bench");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to insert bench: " + e.getMessage(), e);
        }
    }

    //update department id (many to one) 
    public void updateDepartmentId(int studentId, int departmentId) throws DatabaseOperationException {
        String sql = "UPDATE students SET department_id = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, departmentId); // can be null to remove department
            ps.setInt(2, studentId);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new StudentNotFoundException("Student with ID " + studentId + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update department: " + e.getMessage(), e);
        }
    }
    // ── LINK STUDENT TO COLLEGE (Many-to-Many) ───────────────────────────────
    public void insertStudentCollege(int studentId, int collegeId) throws DuplicateLinkException, DatabaseOperationException {
        String sql = "INSERT INTO student_college (student_id, college_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, collegeId);
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate key violation
                throw new DuplicateLinkException("Student is already enrolled in this college");
            }
            throw new DatabaseOperationException("Failed to link student to college: " + e.getMessage(), e);
        }
    }

    // ── GET ALL STUDENTS WITH ALL DETAILS ────────────────────────────────────
    public List<Student> getAllStudentsWithDetails() throws DataFetchException {
      Map<Integer, Student> map = new LinkedHashMap<>();

        String sql = """
            SELECT 
                s.id, s.name, s.email,
                a.id AS aid, a.city, a.state, a.pincode,
                b.id AS bid, b.bench_number,
                c.id AS cid, c.name AS cname, c.location AS clocation,
                d.id AS did, d.name AS dname, d.head AS dhead
            FROM students s
            LEFT JOIN address a ON s.id = a.student_id
            LEFT JOIN benches b ON s.id = b.student_id
            LEFT JOIN student_college sc ON s.id = sc.student_id
            LEFT JOIN colleges c ON sc.college_id = c.id
            LEFT JOIN departments d ON s.department_id = d.id
            ORDER BY s.id
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int sid = rs.getInt("id");

                Student s = map.computeIfAbsent(sid, k -> {
                    Student student = new Student();
                    student.setId(sid);
                    try {
						student.setName(rs.getString("name"));
					} catch (SQLException e) {

						throw new DataFetchException("name not found");
					}
                    try {
						student.setEmail(rs.getString("email"));
					} catch (SQLException e) {
						
						throw new InvalidEmailException("mail is not found");
					}
                    student.setAddresses(new ArrayList<>());
                    student.setColleges(new ArrayList<>());
                    return student;
                });

                // Address
                if (rs.getObject("aid") != null) {
                    Address a = new Address();
                    a.setId(rs.getInt("aid"));
                    a.setCity(rs.getString("city"));
                    a.setState(rs.getString("state"));
                    a.setPincode(rs.getString("pincode"));
                    if (!s.getAddresses().contains(a)) {
                        s.getAddresses().add(a);
                    }
                }

                // Bench
                if (rs.getObject("bid") != null && s.getBench() == null) {
                    Benches b = new Benches();
                    b.setId(rs.getInt("bid"));
                    b.setBenchNumber(rs.getString("bench_number"));
                    b.setStudentId(sid);
                    s.setBench(b);
                }

                // College
                if (rs.getObject("cid") != null && rs.getString("cname") != null) {
                    College c = new College(sql, sql);
                    c.setId(rs.getInt("cid"));
                    c.setName(rs.getString("cname"));
                    c.setLocation(rs.getString("clocation"));
                    if (s.getColleges().stream().noneMatch(col -> col.getId() == c.getId())) {
                        s.getColleges().add(c);
                    }
                }

                // Department (Many-to-One)
                if (rs.getObject("did") != null) {
                    Department d = new Department();
                    d.setId(rs.getInt("did"));
                    d.setName(rs.getString("dname"));
                    d.setHead(rs.getString("dhead"));
                    s.setDepartment(d);
                }
            }

            return new ArrayList<>(map.values());

        } catch (SQLException e) {
            throw new DataFetchException("Failed to fetch students with all details: " + e.getMessage(), e);
        }
    }

    // ── DELETE STUDENT ───────────────────────────────────────────────────────
    public void delete(int id) throws StudentNotFoundException, DatabaseOperationException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new StudentNotFoundException("Student with ID " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    // ── UPDATE STUDENT ───────────────────────────────────────────────────────
    public void update(Student s) throws StudentNotFoundException, DatabaseOperationException {
        String sql = "UPDATE students SET name = ?, email = ?, department_id = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setObject(3, s.getDepartment() != null ? s.getDepartment().getId() : null);
            ps.setInt(4, s.getId());

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new StudentNotFoundException("Student with ID " + s.getId() + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update student: " + e.getMessage(), e);
        }
    }


}