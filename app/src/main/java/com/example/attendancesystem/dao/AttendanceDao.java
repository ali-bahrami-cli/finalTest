package com.example.attendancesystem.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.model.AttendanceSummary;


import java.util.List;

/**
 * Data Access Object for Attendance entity
 * Provides methods for database operations on attendance table
 */
@Dao
public interface AttendanceDao {
    
    /**
     * Insert a new attendance record into the database
     * @param attendance The attendance object to insert
     * @return The row ID of the newly inserted record
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Attendance attendance);
    
    /**
     * Insert multiple attendance records
     * @param attendanceList List of attendance records to insert
     * @return Array of row IDs for the inserted records
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Attendance> attendanceList);
    
    /**
     * Update an existing attendance record
     * @param attendance The attendance object with updated values
     */
    @Update
    void update(Attendance attendance);
    
    /**
     * Delete an attendance record
     * @param attendance The attendance object to delete
     */
    @Delete
    void delete(Attendance attendance);
    
    /**
     * Get all attendance records
     * @return List of all attendance records
     */
    @Query("SELECT * FROM attendance")
    List<Attendance> getAllAttendance();
    
    /**
     * Get attendance records for a specific class
     * @param classId The class ID
     * @return List of attendance records for that class
     */
    @Query("SELECT * FROM attendance WHERE classId = :classId")
    List<Attendance> getAttendanceByClass(int classId);
    
    /**
     * Get attendance records for a specific student
     * @param studentId The student ID
     * @return List of attendance records for that student
     */
    @Query("SELECT * FROM attendance WHERE studentId = :studentId")
    List<Attendance> getAttendanceByStudent(int studentId);
    
    /**
     * Get attendance records for a specific class on a specific date
     * @param classId The class ID
     * @param date The date (timestamp)
     * @return List of attendance records for that class on that date
     */
    @Query("SELECT * FROM attendance WHERE classId = :classId AND date = :date")
    List<Attendance> getAttendanceByClassAndDate(int classId, long date);
    
    /**
     * Get unique dates for a class (all session dates)
     * @param classId The class ID
     * @return List of unique date timestamps
     */
    @Query("SELECT DISTINCT date FROM attendance WHERE classId = :classId ORDER BY date DESC")
    List<Long> getClassSessionDates(int classId);
    
    /**
     * Get attendance records for a specific student in a specific class
     * @param studentId The student ID
     * @param classId The class ID
     * @return List of attendance records
     */
    @Query("SELECT * FROM attendance WHERE studentId = :studentId AND classId = :classId")
    List<Attendance> getAttendanceByStudentAndClass(int studentId, int classId);
    
    /**
     * Get attendance summary for a student in a class
     * Returns the count of present and absent sessions
     * @param studentId The student ID
     * @param classId The class ID
     * @return Array where index 0 = present count, index 1 = absent count
     */
    @Query("SELECT isPresent, COUNT(*) as count FROM attendance WHERE studentId = :studentId AND classId = :classId GROUP BY isPresent")
    List<AttendanceSummary> getAttendanceSummary(int studentId, int classId);
    /**
     * Count present sessions for a student in a class
     * @param studentId The student ID
     * @param classId The class ID
     * @return Number of present sessions
     */
    @Query("SELECT COUNT(*) FROM attendance WHERE studentId = :studentId AND classId = :classId AND isPresent = 1")
    int getPresentCount(int studentId, int classId);
    
    /**
     * Count absent sessions for a student in a class
     * @param studentId The student ID
     * @param classId The class ID
     * @return Number of absent sessions
     */
    @Query("SELECT COUNT(*) FROM attendance WHERE studentId = :studentId AND classId = :classId AND isPresent = 0")
    int getAbsentCount(int studentId, int classId);
    
    /**
     * Delete all attendance records for a specific class
     * @param classId The class ID
     */
    @Query("DELETE FROM attendance WHERE classId = :classId")
    void deleteAttendanceByClass(int classId);
    
    /**
     * Delete all attendance records for a specific student
     * @param studentId The student ID
     */
    @Query("DELETE FROM attendance WHERE studentId = :studentId")
    void deleteAttendanceByStudent(int studentId);
}