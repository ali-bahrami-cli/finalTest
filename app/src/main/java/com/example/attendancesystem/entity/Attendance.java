package com.example.attendancesystem.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Entity representing an attendance record for a specific student in a specific class session
 * Links students to their attendance status in each class session
 */
@Entity(
    tableName = "attendance",
    foreignKeys = {
        @ForeignKey(
            entity = Student.class,
            parentColumns = "id",
            childColumns = "studentId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = ClassCourse.class,
            parentColumns = "id",
            childColumns = "classId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("studentId"),
        @Index("classId")
    }
)
public class Attendance {
    
    /**
     * Unique identifier for the attendance record
     * Auto-generated when a new record is created
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    /**
     * Foreign key referencing the student
     */
    private int studentId;
    
    /**
     * Foreign key referencing the class
     */
    private int classId;
    
    /**
     * Date when the attendance was taken (stored as timestamp)
     * Used to group attendance records by session
     */
    private long date;
    
    /**
     * Attendance status: true = present, false = absent
     */
    private boolean isPresent;
    
    /**
     * Default constructor required by Room
     */
    public Attendance() {
    }
    
    /**
     * Constructor for creating a new attendance record
     * @param studentId ID of the student
     * @param classId ID of the class
     * @param date Date of the session (timestamp)
     * @param isPresent Attendance status
     */
    public Attendance(int studentId, int classId, long date, boolean isPresent) {
        this.studentId = studentId;
        this.classId = classId;
        this.date = date;
        this.isPresent = isPresent;
    }
    
    // Getter and Setter methods
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getClassId() {
        return classId;
    }
    
    public void setClassId(int classId) {
        this.classId = classId;
    }
    
    public long getDate() {
        return date;
    }
    
    public void setDate(long date) {
        this.date = date;
    }
    
    public boolean isPresent() {
        return isPresent;
    }
    
    public void setPresent(boolean present) {
        isPresent = present;
    }
}