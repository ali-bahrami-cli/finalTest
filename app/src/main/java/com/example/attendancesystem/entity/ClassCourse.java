package com.example.attendancesystem.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity representing a course/class in the university
 * Each class has a schedule and assigned instructor
 */
@Entity(tableName = "classes")
public class ClassCourse {
    
    /**
     * Unique identifier for the class
     * Auto-generated when a new class is created
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    /**
     * Name of the course (e.g., "Advanced Programming", "Data Structures")
     */
    private String courseName;
    
    /**
     * Name of the instructor/professor teaching this class
     */
    private String instructorName;
    
    /**
     * Day of the week when the class is held (e.g., "Saturday", "Sunday")
     */
    private String day;
    
    /**
     * Time when the class is held (e.g., "08:00 - 10:00")
     */
    private String time;
    
    /**
     * Default constructor required by Room
     */
    public ClassCourse() {
    }
    
    /**
     * Constructor for creating a new class
     * @param courseName Name of the course
     * @param instructorName Name of the instructor
     * @param day Day of the week
     * @param time Time of the class
     */
    public ClassCourse(String courseName, String instructorName, String day, String time) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.day = day;
        this.time = time;
    }
    
    // Getter and Setter methods
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public String getDay() {
        return day;
    }
    
    public void setDay(String day) {
        this.day = day;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
}