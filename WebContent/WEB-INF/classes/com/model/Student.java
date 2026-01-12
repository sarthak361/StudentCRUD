package com.model;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int id;
    private String name;
    private String email;
    private List<College> colleges = new ArrayList<>();
    
    private Department department;  

    
    
 public Department getDepartment() { return department; }
 public void setDepartment(Department department) { this.department = department; }
    
    
    public List<College> getColleges() {
        return colleges;
    }

    // ===== Setter for colleges =====
    public void setColleges(List<College> colleges) {
        this.colleges = colleges;
    }
   
// this is for one to one relationship
    private Benches bench;
    
    // ONE-TO-MANY
    private List<Address> addresses = new ArrayList<>();
    public Student() {
        // default constructor
    }

    // getters & setters
    
    public Benches getBench() {
        return bench;
    }

    public void setBench(Benches bench) {
        this.bench = bench;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
