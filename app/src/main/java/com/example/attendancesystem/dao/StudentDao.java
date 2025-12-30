package com.example.attendancesystem.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.attendancesystem.entity.Student;

import java.util.List;

/**
 * Data Access Object for Student entity
 * Provides methods for database operations on students table
 */
@Dao
public interface StudentDao {
    
    /**
     * Insert a new student into the database
     * @param student The student object to insert
     * @return The row ID of the newly inserted student
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Student student);
    
    /**
     * Insert multiple students into the database
     * @param students List of students to insert
     * @return Array of row IDs for the inserted students
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Student> students);
    
    /**
     * Update an existing student record
     * @param student The student object with updated values
     */
    @Update
    void update(Student student);
    
    /**
     * Delete a student from the database
     * @param student The student object to delete
     */
    @Delete
    void delete(Student student);
    
    /**
     * Get all students from the database
     * @return List of all students
     */
    @Query("SELECT * FROM students")
    List<Student> getAllStudents();
    
    /**
     * Get a student by their ID
     * @param id The student's unique identifier
     * @return The student object, or null if not found
     */
    @Query("SELECT * FROM students WHERE id = :id")
    Student getStudentById(int id);
    
    /**
     * Get a student by their student number
     * @param studentNumber The student's identification number
     * @return The student object, or null if not found
     */
    @Query("SELECT * FROM students WHERE studentNumber = :studentNumber")
    Student getStudentByNumber(String studentNumber);
    
    /**
     * Search for students by name (case-insensitive)
     * @param name The name or partial name to search for
     * @return List of matching students
     */
    @Query("SELECT * FROM students WHERE name LIKE '%' || :name || '%'")
    List<Student> searchStudentsByName(String name);
    
    /**
     * Get the total count of students
     * @return Number of students in the database
     */
    @Query("SELECT COUNT(*) FROM students")
    int getStudentCount();
}