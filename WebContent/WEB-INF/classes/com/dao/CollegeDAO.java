package com.dao;

import java.sql.*;

import com.exception.InvalidRequestException;
import com.model.College;

public class CollegeDAO {

    // Insert college and return ID
    public int insertCollege(College college) {
        String sql = "INSERT INTO colleges (name, location) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, college.getName());
            ps.setString(2, college.getLocation());
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new InvalidRequestException("College insert failed");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    college.setId(id);
                    return id;
                }
                throw new RuntimeException("No ID returned");
            }

        } catch (SQLException e) {
            throw new RuntimeException("College insert error: " + e.getMessage(), e);
        }
    }

    // Link student to college
    public void linkStudentToCollege(int studentId, int collegeId) {
        String sql = "INSERT INTO student_college (student_id, college_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, collegeId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Link error: " + e.getMessage(), e);
        }
    }
}