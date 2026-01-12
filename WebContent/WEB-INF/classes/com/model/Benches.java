package com.model;

public class Benches {
    private int id;
    private String benchNumber;
    private int studentId;

    // Default constructor
    public Benches() {
    }

    // All-args constructor (optional but very useful)
    public Benches(int id, String benchNumber, int studentId) {
        this.id = id;
        this.benchNumber = benchNumber;
        this.studentId = studentId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getBenchNumber() {
        return benchNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBenchNumber(String benchNumber) {
        this.benchNumber = benchNumber;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}