package com.example.attendancesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.adapter.ClassAdapter;
import com.example.attendancesystem.database.AppDatabase;
import com.example.attendancesystem.entity.ClassCourse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity for managing classes (courses)
 * Allows viewing, adding, and deleting classes
 */
public class ClassListActivity extends AppCompatActivity {
    
    // UI Components
    private Toolbar toolbar;
    private RecyclerView rvClasses;
    private FloatingActionButton fabAddClass;
    private TextView tvEmptyState;
    
    // Database
    private AppDatabase database;
    
    // Adapter
    private ClassAdapter classAdapter;
    
    // Data
    private List<ClassCourse> classList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        
        // Initialize database
        database = AppDatabase.getInstance(this);
        
        // Initialize views
        initViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup FAB
        setupFab();
        
        // Load classes
        loadClasses();
    }
    
    /**
     * Initialize all views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvClasses = findViewById(R.id.rvClasses);
        fabAddClass = findViewById(R.id.fabAddClass);
        tvEmptyState = findViewById(R.id.tvEmptyState);
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    
    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList, new ClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ClassCourse classCourse) {
                // Handle class click if needed
            }
            
            @Override
            public void onTakeAttendanceClick(ClassCourse classCourse) {
                openTakeAttendanceActivity(classCourse);
            }
            
            @Override
            public void onViewReportClick(ClassCourse classCourse) {
                openAttendanceReportActivity(classCourse);
            }
            
            @Override
            public void onDeleteClick(ClassCourse classCourse) {
                confirmDeleteClass(classCourse);
            }
        });
        
        rvClasses.setLayoutManager(new LinearLayoutManager(this));
        rvClasses.setAdapter(classAdapter);
    }
    
    /**
     * Setup Floating Action Button
     */
    private void setupFab() {
        fabAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddClassDialog();
            }
        });
    }
    
    /**
     * Load all classes from database
     */
    private void loadClasses() {
        classList.clear();
        classList.addAll(database.classCourseDao().getAllClasses());
        classAdapter.notifyDataSetChanged();
        
        // Show/hide empty state
        if (classList.isEmpty()) {
            rvClasses.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvClasses.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }
    }
    
    /**
     * Show dialog for adding a new class
     */
    private void showAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_class, null);
        
        EditText etCourseName = dialogView.findViewById(R.id.etCourseName);
        EditText etInstructorName = dialogView.findViewById(R.id.etInstructorName);
        Spinner spinnerDay = dialogView.findViewById(R.id.spinnerDay);
        EditText etTime = dialogView.findViewById(R.id.etTime);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        
        // Setup day spinner
        String[] days = getResources().getStringArray(R.array.days_of_week);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);
        
        AlertDialog dialog = builder.setView(dialogView).create();
        
        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = etCourseName.getText().toString().trim();
                String instructorName = etInstructorName.getText().toString().trim();
                String day = spinnerDay.getSelectedItem().toString();
                String time = etTime.getText().toString().trim();
                
                if (courseName.isEmpty() || instructorName.isEmpty() || time.isEmpty()) {
                    Toast.makeText(ClassListActivity.this, R.string.fill_all_fields, 
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Create new class
                ClassCourse newClass = new ClassCourse(courseName, instructorName, day, time);
                database.classCourseDao().insert(newClass);
                
                Toast.makeText(ClassListActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadClasses();
            }
        });
        
        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        dialog.show();
    }
    
    /**
     * Open Take Attendance Activity for a specific class
     */
    private void openTakeAttendanceActivity(ClassCourse classCourse) {
        Intent intent = new Intent(this, TakeAttendanceActivity.class);
        intent.putExtra("classId", classCourse.getId());
        intent.putExtra("courseName", classCourse.getCourseName());
        startActivity(intent);
    }
    
    /**
     * Open Attendance Report Activity for a specific class
     */
    private void openAttendanceReportActivity(ClassCourse classCourse) {
        Intent intent = new Intent(this, AttendanceReportActivity.class);
        intent.putExtra("classId", classCourse.getId());
        intent.putExtra("courseName", classCourse.getCourseName());
        intent.putExtra("instructorName", classCourse.getInstructorName());
        intent.putExtra("day", classCourse.getDay());
        intent.putExtra("time", classCourse.getTime());
        startActivity(intent);
    }
    
    /**
     * Show confirmation dialog before deleting a class
     */
    private void confirmDeleteClass(final ClassCourse classCourse) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete)
            .setMessage("آیا از حذف کلاس &quot;" + classCourse.getCourseName() + "&quot; مطمئن هستید؟")
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Delete attendance records first
                    database.attendanceDao().deleteAttendanceByClass(classCourse.getId());
                    
                    // Then delete the class
                    database.classCourseDao().delete(classCourse);
                    
                    Toast.makeText(ClassListActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                    loadClasses();
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}