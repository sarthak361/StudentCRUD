// com.dao.DepartmentDAO.java
package com.dao;

import java.sql.*;

import com.exception.DataFetchException;
import com.model.Department;

public class DepartmentDAO {

    public int insertDepartment(Department dept) {
        String sql = "INSERT INTO departments (name, head, location) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dept.getName());
            ps.setString(2, dept.getHead());
            ps.setString(3, dept.getLocation());
            int rows = ps.executeUpdate();

            if (rows == 0) throw new RuntimeException("Department insert failed");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    dept.setId(id);
                    return id;
                }
                throw new RuntimeException("No ID returned");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Department insert error: " + e.getMessage(), e);
        }
    }
    
    public Department findByName(String name) throws DataFetchException {
        String sql = "SELECT id, name, head FROM departments WHERE name = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Department d = new Department();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("name"));
                    d.setHead(rs.getString("head"));
                    return d;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataFetchException("Failed to find department by name: " + e.getMessage(), e);
        }
    }
}