package com.example.attendancesystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.adapter.TakeAttendanceAdapter;
import com.example.attendancesystem.database.AppDatabase;
import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity for taking attendance for a specific class on a specific date
 * Allows marking students as present or absent
 */
public class TakeAttendanceActivity extends AppCompatActivity {
    
    // UI Components
    private Toolbar toolbar;
    private RecyclerView rvStudents;
    private TextView tvCourseName;
    private TextView tvSelectedDate;
    private Button btnSelectDate;
    private Button btnSaveAttendance;
    private TextView tvEmptyState;
    
    // Database
    private AppDatabase database;
    
    // Adapter
    private TakeAttendanceAdapter attendanceAdapter;
    
    // Data
    private int classId;
    private String courseName;
    private List<Student> studentList;
    private List<Attendance> attendanceList;
    private long selectedDate;
    
    // Date format
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        
        // Initialize database
        database = AppDatabase.getInstance(this);
        
        // Get intent data
        classId = getIntent().getIntExtra("classId", -1);
        courseName = getIntent().getStringExtra("courseName");
        
        if (classId == -1) {
            Toast.makeText(this, "خطا: کلاس مشخص نشده است", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        
        // Set current date as default
        selectedDate = System.currentTimeMillis();
        
        // Initialize views
        initViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup date picker
        setupDatePicker();
        
        // Setup save button
        setupSaveButton();
        
        // Load data
        loadData();
    }
    
    /**
     * Initialize all views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvStudents = findViewById(R.id.rvStudents);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveAttendance = findViewById(R.id.btnSaveAttendance);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        
        // Set course name
        tvCourseName.setText(courseName);
        
        // Update selected date text
        updateSelectedDateText();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ثبت حضور و غیاب");
    }
    
    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        studentList = new ArrayList<>();
        attendanceList = new ArrayList<>();
        attendanceAdapter = new TakeAttendanceAdapter(studentList, attendanceList, selectedDate);
        
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(attendanceAdapter);
    }
    
    /**
     * Setup date picker button
     */
    private void setupDatePicker() {
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }
    
    /**
     * Setup save button
     */
    private void setupSaveButton() {
        btnSaveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance();
            }
        });
    }
    
    /**
     * Show date picker dialog
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate);
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newCalendar = Calendar.getInstance();
                    newCalendar.set(year, month, dayOfMonth, 0, 0, 0);
                    selectedDate = newCalendar.getTimeInMillis();
                    updateSelectedDateText();
                    loadData();
                }
            },
            year, month, day
        );
        
        datePickerDialog.show();
    }
    
    /**
     * Update selected date text display
     */
    private void updateSelectedDateText() {
        tvSelectedDate.setText("تاریخ: " + dateFormat.format(new Date(selectedDate)));
    }
    
    /**
     * Load students and attendance data
     */
    private void loadData() {
        // Load all students
        studentList.clear();
        studentList.addAll(database.studentDao().getAllStudents());
        
        // Load existing attendance for selected date
        attendanceList.clear();
        attendanceList.addAll(database.attendanceDao().getAttendanceByClassAndDate(classId, selectedDate));
        
        // Update adapter
        attendanceAdapter.setAttendanceList(attendanceList);
        attendanceAdapter.notifyDataSetChanged();
        
        // Show/hide empty state
        if (studentList.isEmpty()) {
            rvStudents.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvStudents.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }
    }
    
    /**
     * Save attendance data to database
     */
    private void saveAttendance() {
        List<Attendance> newAttendanceList = new ArrayList<>();
        
        // Get attendance status for each student
        for (Student student : studentList) {
            boolean isPresent = attendanceAdapter.isStudentPresent(student.getId());
            
            // Check if attendance already exists
            boolean found = false;
            for (Attendance existingAttendance : attendanceList) {
                if (existingAttendance.getStudentId() == student.getId() && 
                    existingAttendance.getDate() == selectedDate) {
                    // Update existing attendance
                    existingAttendance.setPresent(isPresent);
                    database.attendanceDao().update(existingAttendance);
                    found = true;
                    break;
                }
            }
            
            // If not found, create new attendance
            if (!found) {
                Attendance attendance = new Attendance(student.getId(), classId, selectedDate, isPresent);
                newAttendanceList.add(attendance);
            }
        }
        
        // Insert new attendance records
        if (!newAttendanceList.isEmpty()) {
            database.attendanceDao().insertAll(newAttendanceList);
        }
        
        Toast.makeText(this, "حضور و غیاب با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
        
        // Reload data
        loadData();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}