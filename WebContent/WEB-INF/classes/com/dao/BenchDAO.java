package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.exception.DatabaseOperationException;
import com.exception.DuplicateBenchNumberException;
import com.exception.InsertingBenchException;
import com.model.Benches;
import com.model.Student;  

public class BenchDAO {


    // 1. INSERT BENCH + return generated ID
    public static int insertBench(Benches bench) {
    	
    	if (bench.getBenchNumber() == null || bench.getBenchNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Bench number cannot be empty");
        }

   //bench number exist or not  
        if (isBenchNumberExists(bench.getBenchNumber())) {
            throw new DuplicateBenchNumberException(
                "Bench number '" + bench.getBenchNumber() + "' already exists!"
            );
        }
        String sql = "INSERT INTO benches (bench_number, student_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bench.getBenchNumber());
            ps.setInt(2, bench.getStudentId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DatabaseOperationException("Bench insertion failed — no rows affected");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    bench.setId(generatedId);  // update object with new ID
                    return generatedId;
                } else {
                    throw new DatabaseOperationException("Failed to retrieve generated bench ID");
                }
            }

        } catch (SQLException e) {
            throw new InsertingBenchException("Error inserting bench: " + e.getMessage());
        }
    }
    
    //if bench number exist
    public static boolean isBenchNumberExists(String benchNumber) {
        String sql = "SELECT COUNT(*) FROM benches WHERE bench_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, benchNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }

        } catch (SQLException e) {
            throw new InsertingBenchException("Error checking bench number existence");
        }
    }
    


    // 2. insert bench  for a specific student 
 
    public int insertBenchForStudent(int studentId, String benchNumber) {
        Benches bench = new Benches();
        bench.setBenchNumber(benchNumber);
        bench.setStudentId(studentId);

        return insertBench(bench); 
    }


    // find bench by id(One-to-One)
  
    public Benches findByStudentId(int studentId) {
        String sql = "SELECT id, bench_number, student_id FROM benches WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Benches b = new Benches();
                    b.setId(rs.getInt("id"));
                    b.setBenchNumber(rs.getString("bench_number"));
                    b.setStudentId(rs.getInt("student_id"));
                    return b;
                }
                return null; 
            }

        } catch (SQLException e) {
            throw new InsertingBenchException("Error finding bench for student " + studentId, e);
        }
    }

   
    //  get all benches
 
    public List<Benches> findAll() {
        List<Benches> list = new ArrayList<>();
        String sql = "SELECT id, bench_number, student_id FROM benches ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Benches b = new Benches();
                b.setId(rs.getInt("id"));
                b.setBenchNumber(rs.getString("bench_number"));
                b.setStudentId(rs.getInt("student_id"));
                list.add(b);
            }
            return list;

        } catch (SQLException e) {
            throw new InsertingBenchException("Error fetching all benches", e);
        }
    }

 
    // UPDATE BENCH NUMBER 

    public void updateBenchNumber(int benchId, String newBenchNumber) {
        String sql = "UPDATE benches SET bench_number = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newBenchNumber);
            ps.setInt(2, benchId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No bench found with ID: " + benchId);
            }

        } catch (SQLException e) {
            throw new InsertingBenchException("Error updating bench", e);
        }
    }


    // DELETE BENCH 
    
    public void deleteById(int benchId) {
        String sql = "DELETE FROM benches WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, benchId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InsertingBenchException("Error deleting bench", e);
        }
    }

    
    //  DELETE BENCH BY STUDENT ID 
   
    public void deleteByStudentId(int studentId) {
        String sql = "DELETE FROM benches WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InsertingBenchException("Error deleting bench for student " + studentId, e);
        }
    }
}