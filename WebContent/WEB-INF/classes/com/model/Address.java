package com.model;

public class Address {

    private int id;
    private String city;
    private String state;
    private String pincode;
    private int studentId;   // Foreign key (student.id)

    // ===== Getter & Setter for id =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ===== Getter & Setter for city =====
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // ===== Getter & Setter for state =====
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // ===== Getter & Setter for pincode =====
    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    // ===== Getter & Setter for studentId =====
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
