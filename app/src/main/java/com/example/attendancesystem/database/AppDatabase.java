package com.example.attendancesystem.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.attendancesystem.dao.AttendanceDao;
import com.example.attendancesystem.dao.ClassCourseDao;
import com.example.attendancesystem.dao.StudentDao;
import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.ClassCourse;
import com.example.attendancesystem.entity.Student;

/**
 * Main database class for the Attendance System application
 * Uses Room Database to manage local data persistence
 * 
 * This is a singleton database instance that provides access to all DAOs
 * for performing database operations on Student, ClassCourse, and Attendance entities
 */
@Database(
    entities = {Student.class, ClassCourse.class, Attendance.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    /**
     * Singleton instance of the database
     * Ensures only one database instance exists throughout the application lifecycle
     */
    private static AppDatabase instance;
    
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "attendance_system_db";
    
    /**
     * Provides access to Student DAO for student-related operations
     * @return StudentDao interface instance
     */
    public abstract StudentDao studentDao();
    
    /**
     * Provides access to ClassCourse DAO for class-related operations
     * @return ClassCourseDao interface instance
     */
    public abstract ClassCourseDao classCourseDao();
    
    /**
     * Provides access to Attendance DAO for attendance-related operations
     * @return AttendanceDao interface instance
     */
    public abstract AttendanceDao attendanceDao();
    
    /**
     * Gets the singleton instance of the database
     * Creates the database if it doesn't exist, otherwise returns existing instance
     * 
     * @param context Application context
     * @return Singleton instance of AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        }
        return instance;
    }
    
    /**
     * Destroys the database instance
     * Useful for testing or when you want to reset the database
     */
    public static void destroyInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}