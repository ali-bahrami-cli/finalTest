package com.example.attendancesystem.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

/**
 * Entity representing a student in the system
 * Each student can be enrolled in multiple classes
 */
@Entity(tableName = "students")
public class Student {
    
    /**
     * Unique identifier for the student
     * Auto-generated when a new student is created
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    /**
     * Full name of the student
     */
    private String name;
    
    /**
     * Student's unique identification number
     * Used for official university records
     */
    private String studentNumber;
    
    /**
     * Default constructor required by Room
     */
    public Student() {
    }
    
    /**
     * Constructor for creating a new student
     * @param name Student's full name
     * @param studentNumber Student's unique identification number
     */
    public Student(String name, String studentNumber) {
        this.name = name;
        this.studentNumber = studentNumber;
    }
    
    // Getter and Setter methods
    
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
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}