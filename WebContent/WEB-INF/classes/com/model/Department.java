// com.model.Department.java
package com.model;

public class Department {
    private int id;
    private String name;
    private String head;
    private String location;

    // Constructors
    public Department() {}
    public Department(String name, String head) {
        this.name = name;
        this.head = head;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHead() { return head; }
    public void setHead(String head) { this.head = head; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}