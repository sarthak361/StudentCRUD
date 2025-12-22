package com.dao;

import java.sql.*;
import java.util.*;
import com.model.Student;

public class StudentDAO {

    // CREATE
    public void insertStudent(Student s) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
              con.prepareStatement(
                "INSERT INTO students(name,email) VALUES (?,?)")) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
              con.prepareStatement("SELECT * FROM students");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updateStudent(Student s) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
              con.prepareStatement(
                "UPDATE students SET name=?, email=? WHERE id=?")) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setInt(3, s.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteStudent(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement("DELETE FROM students WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // GET BY ID (for edit)
    public Student getStudentById(int id) {
        Student s = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
              con.prepareStatement("SELECT * FROM students WHERE id=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
