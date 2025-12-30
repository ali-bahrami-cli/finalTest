package com.example.attendancesystem;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.adapter.AttendanceReportAdapter;
import com.example.attendancesystem.database.AppDatabase;
import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for viewing attendance reports for a specific class
 * Shows summary statistics and detailed attendance records
 */
public class AttendanceReportActivity extends AppCompatActivity {
    
    // UI Components
    private Toolbar toolbar;
    private RecyclerView rvAttendance;
    private TextView tvCourseName;
    private TextView tvClassDetails;
    private TextView tvTotalSessions;
    private TextView tvPresentCount;
    private TextView tvAbsentCount;
    private TextView tvEmptyState;
    
    // Database
    private AppDatabase database;
    
    // Adapter
    private AttendanceReportAdapter attendanceAdapter;
    
    // Data
    private int classId;
    private String courseName;
    private String instructorName;
    private String day;
    private String time;
    private List<Student> studentList;
    private List<Attendance> attendanceList;
    
    // Date format
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        
        // Initialize database
        database = AppDatabase.getInstance(this);
        
        // Get intent data
        classId = getIntent().getIntExtra("classId", -1);
        courseName = getIntent().getStringExtra("courseName");
        instructorName = getIntent().getStringExtra("instructorName");
        day = getIntent().getStringExtra("day");
        time = getIntent().getStringExtra("time");
        
        if (classId == -1) {
            Toast.makeText(this, "خطا: کلاس مشخص نشده است", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        
        // Initialize views
        initViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load data
        loadData();
    }
    
    /**
     * Initialize all views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvAttendance = findViewById(R.id.rvAttendance);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvClassDetails = findViewById(R.id.tvClassDetails);
        tvTotalSessions = findViewById(R.id.tvTotalSessions);
        tvPresentCount = findViewById(R.id.tvPresentCount);
        tvAbsentCount = findViewById(R.id.tvAbsentCount);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        
        // Set course info
        tvCourseName.setText(courseName);
        tvClassDetails.setText("استاد: " + instructorName + " | " + day + " - " + time);
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("گزارش حضور و غیاب");
    }
    
    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceReportAdapter();
        
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));
        rvAttendance.setAdapter(attendanceAdapter);
    }
    
    /**
     * Load attendance data and calculate statistics
     */
    private void loadData() {
        // Load all students
        studentList = database.studentDao().getAllStudents();
        
        // Load all attendance records for this class
        attendanceList = database.attendanceDao().getAttendanceByClass(classId);
        
        if (attendanceList.isEmpty()) {
            rvAttendance.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            
            // Reset statistics
            tvTotalSessions.setText("0");
            tvPresentCount.setText("0");
            tvAbsentCount.setText("0");
            
            return;
        }
        
        rvAttendance.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        
        // Calculate statistics per student
        Map<Integer, StudentAttendanceInfo> studentStats = new HashMap<>();
        
        for (Student student : studentList) {
            StudentAttendanceInfo info = new StudentAttendanceInfo();
            info.student = student;
            info.presentCount = 0;
            info.absentCount = 0;
            studentStats.put(student.getId(), info);
        }
        
        // Count attendance
        for (Attendance attendance : attendanceList) {
            StudentAttendanceInfo info = studentStats.get(attendance.getStudentId());
            if (info != null) {
                if (attendance.isPresent()) {
                    info.presentCount++;
                } else {
                    info.absentCount++;
                }
            }
        }
        
        // Calculate total statistics
        int totalPresent = 0;
        int totalAbsent = 0;
        
        for (StudentAttendanceInfo info : studentStats.values()) {
            totalPresent += info.presentCount;
            totalAbsent += info.absentCount;
        }
        
        int totalSessions = totalPresent + totalAbsent;
        
        // Update summary
        tvTotalSessions.setText(String.valueOf(totalSessions));
        tvPresentCount.setText(String.valueOf(totalPresent));
        tvAbsentCount.setText(String.valueOf(totalAbsent));
        
        // Update adapter with data
        attendanceAdapter.setData(studentStats, dateFormat);
    }
    
    /**
     * Inner class to hold student attendance information
     */
    private static class StudentAttendanceInfo {
        Student student;
        int presentCount;
        int absentCount;
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}