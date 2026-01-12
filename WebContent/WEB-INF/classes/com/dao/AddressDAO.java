package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.exception.deletionnotfindexception;
import com.exception.insertaddressexception;
import com.exception.studentaddressfailedexception;
import com.model.Address;
import com.model.Student;

public class AddressDAO {

    public void insert(Address a) {

        String sql = """
            INSERT INTO address(city, state, pincode, student_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getCity());
            ps.setString(2, a.getState());
            ps.setString(3, a.getPincode());
            ps.setInt(4, a.getStudentId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new insertaddressexception("Failed to insert address");
        }
    }
    public void deleteByStudentId(int studentId) {

        String sql = "DELETE FROM address WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new deletionnotfindexception("Address delete failed");
        }
    }
    
    public List<Student> getAllWithAddresses() {

        String sql = """
            SELECT s.id sid, s.name, s.email,
                   a.id aid, a.city, a.state, a.pincode
            FROM students s
            LEFT JOIN address a ON s.id = a.student_id
        """;

        Map<Integer, Student> map = new LinkedHashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int sid = rs.getInt("sid");

                Student s = map.get(sid);
                if (s == null) {
                    s = new Student();
                    s.setId(sid);
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setAddresses(new ArrayList<>());
                    map.put(sid, s);
                }

                if (rs.getString("city") != null) {
                    Address a = new Address();
                    a.setId(rs.getInt("aid"));
                    a.setCity(rs.getString("city"));
                    a.setState(rs.getString("state"));
                    a.setPincode(rs.getString("pincode"));
                    s.getAddresses().add(a);
                }
            }

        } catch (Exception e) {
            throw new studentaddressfailedexception("Fetch failed");
        }

        return new ArrayList<>(map.values());
    }

}
