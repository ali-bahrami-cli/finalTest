package com.example.attendancesystem.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.attendancesystem.entity.ClassCourse;

import java.util.List;

/**
 * Data Access Object for ClassCourse entity
 * Provides methods for database operations on classes table
 */
@Dao
public interface ClassCourseDao {
    
    /**
     * Insert a new class into the database
     * @param classCourse The class object to insert
     * @return The row ID of the newly inserted class
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ClassCourse classCourse);
    
    /**
     * Insert multiple classes into the database
     * @param classes List of classes to insert
     * @return Array of row IDs for the inserted classes
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ClassCourse> classes);
    
    /**
     * Update an existing class record
     * @param classCourse The class object with updated values
     */
    @Update
    void update(ClassCourse classCourse);
    
    /**
     * Delete a class from the database
     * @param classCourse The class object to delete
     */
    @Delete
    void delete(ClassCourse classCourse);
    
    /**
     * Get all classes from the database
     * @return List of all classes
     */
    @Query("SELECT * FROM classes")
    List<ClassCourse> getAllClasses();
    
    /**
     * Get a class by its ID
     * @param id The class's unique identifier
     * @return The class object, or null if not found
     */
    @Query("SELECT * FROM classes WHERE id = :id")
    ClassCourse getClassById(int id);
    
    /**
     * Search for classes by course name (case-insensitive)
     * @param courseName The course name or partial name to search for
     * @return List of matching classes
     */
    @Query("SELECT * FROM classes WHERE courseName LIKE '%' || :courseName || '%'")
    List<ClassCourse> searchClassesByCourseName(String courseName);
    
    /**
     * Get classes by instructor name
     * @param instructorName The name of the instructor
     * @return List of classes taught by the instructor
     */
    @Query("SELECT * FROM classes WHERE instructorName = :instructorName")
    List<ClassCourse> getClassesByInstructor(String instructorName);
    
    /**
     * Get classes scheduled on a specific day
     * @param day The day of the week (e.g., "Saturday")
     * @return List of classes on that day
     */
    @Query("SELECT * FROM classes WHERE day = :day")
    List<ClassCourse> getClassesByDay(String day);
    
    /**
     * Get the total count of classes
     * @return Number of classes in the database
     */
    @Query("SELECT COUNT(*) FROM classes")
    int getClassCount();
}