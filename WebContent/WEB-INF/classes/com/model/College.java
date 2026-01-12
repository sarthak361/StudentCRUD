package com.model;

import java.util.ArrayList;
import java.util.List;

public class College {
    private int id;
    private String name;
  
    private String location;

    private List<Student> students = new ArrayList<>();
    
    public College(String name2, String location2) {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ===== Getter & Setter for name =====
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 

    // ===== Getter & Setter for students =====
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
	
}
