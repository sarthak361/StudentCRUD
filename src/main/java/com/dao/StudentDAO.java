package com.dao;

import java.sql.*;
import java.util.*;
import com.model.Student;

public class StudentDAO {

    public void insert(Student s) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(
                		 
                		 //for insert data into table
                     "INSERT INTO students(name,email) VALUES (?,?)")) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isEmailExists(String email) {

        String sql = "SELECT COUNT(*) FROM students WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
            		 //to show data on screen
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

    public void delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(
                		 //this is for deleting the data
                     "DELETE FROM students WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isEmailExistsForOtherStudent(int id, String email) {

        String sql = "SELECT COUNT(*) FROM students WHERE email = ? AND id != ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            throw new RuntimeException("Database error while checking email");
        }

        return false;
    }


    public void update(Student s) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(
                		 
                		 //update data 
                     "UPDATE students SET name=?, email=? WHERE id=?")) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setInt(3, s.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Student getById(int id) {
        Student s = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(
                		 
                     "SELECT * FROM students WHERE id=?")) {

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
