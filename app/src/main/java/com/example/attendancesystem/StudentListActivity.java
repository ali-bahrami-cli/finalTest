package com.example.attendancesystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.adapter.StudentAdapter;
import com.example.attendancesystem.database.AppDatabase;
import com.example.attendancesystem.entity.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for managing students
 * Allows viewing, adding, editing, and deleting students
 */
public class StudentListActivity extends AppCompatActivity {
    
    // UI Components
    private Toolbar toolbar;
    private RecyclerView rvStudents;
    private FloatingActionButton fabAddStudent;
    private EditText etSearch;
    private TextView tvEmptyState;
    
    // Database
    private AppDatabase database;
    
    // Adapter
    private StudentAdapter studentAdapter;
    
    // Data
    private List<Student> studentList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        
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
        
        // Setup search
        setupSearch();
        
        // Load students
        loadStudents();
    }
    
    /**
     * Initialize all views
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvStudents = findViewById(R.id.rvStudents);
        fabAddStudent = findViewById(R.id.fabAddStudent);
        etSearch = findViewById(R.id.etSearch);
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
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentList, new StudentAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Student student) {
                showEditStudentDialog(student);
            }
            
            @Override
            public void onDeleteClick(Student student) {
                confirmDeleteStudent(student);
            }
        });
        
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(studentAdapter);
    }
    
    /**
     * Setup Floating Action Button
     */
    private void setupFab() {
        fabAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentDialog();
            }
        });
    }
    
    /**
     * Setup search functionality
     */
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                searchStudents(s.toString());
            }
        });
    }
    
    /**
     * Load all students from database
     */
    private void loadStudents() {
        studentList.clear();
        studentList.addAll(database.studentDao().getAllStudents());
        studentAdapter.notifyDataSetChanged();
        
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
     * Search students by name
     */
    private void searchStudents(String query) {
        if (query.isEmpty()) {
            loadStudents();
        } else {
            studentList.clear();
            studentList.addAll(database.studentDao().searchStudentsByName(query));
            studentAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * Show dialog for adding a new student
     */
    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_student, null);
        
        EditText etStudentName = dialogView.findViewById(R.id.etStudentName);
        EditText etStudentNumber = dialogView.findViewById(R.id.etStudentNumber);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText(R.string.add_student);
        
        AlertDialog dialog = builder.setView(dialogView).create();
        
        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etStudentName.getText().toString().trim();
                String studentNumber = etStudentNumber.getText().toString().trim();
                
                if (name.isEmpty() || studentNumber.isEmpty()) {
                    Toast.makeText(StudentListActivity.this, R.string.fill_all_fields, 
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Check if student number already exists
                Student existingStudent = database.studentDao().getStudentByNumber(studentNumber);
                if (existingStudent != null) {
                    Toast.makeText(StudentListActivity.this, "شماره دانشجویی تکراری است", 
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Create new student
                Student newStudent = new Student(name, studentNumber);
                database.studentDao().insert(newStudent);
                
                Toast.makeText(StudentListActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadStudents();
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
     * Show dialog for editing an existing student
     */
    private void showEditStudentDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_student, null);
        
        EditText etStudentName = dialogView.findViewById(R.id.etStudentName);
        EditText etStudentNumber = dialogView.findViewById(R.id.etStudentNumber);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText(R.string.edit_student);
        
        // Pre-fill with existing data
        etStudentName.setText(student.getName());
        etStudentNumber.setText(student.getStudentNumber());
        
        AlertDialog dialog = builder.setView(dialogView).create();
        
        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etStudentName.getText().toString().trim();
                String studentNumber = etStudentNumber.getText().toString().trim();
                
                if (name.isEmpty() || studentNumber.isEmpty()) {
                    Toast.makeText(StudentListActivity.this, R.string.fill_all_fields, 
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Check if student number already exists (excluding current student)
                Student existingStudent = database.studentDao().getStudentByNumber(studentNumber);
                if (existingStudent != null && existingStudent.getId() != student.getId()) {
                    Toast.makeText(StudentListActivity.this, "شماره دانشجویی تکراری است", 
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Update student
                student.setName(name);
                student.setStudentNumber(studentNumber);
                database.studentDao().update(student);
                
                Toast.makeText(StudentListActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadStudents();
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
     * Show confirmation dialog before deleting a student
     */
    private void confirmDeleteStudent(final Student student) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.confirm_delete)
            .setMessage("آیا از حذف دانشجو &quot;" + student.getName() + "&quot; مطمئن هستید؟")
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Delete attendance records first
                    database.attendanceDao().deleteAttendanceByStudent(student.getId());
                    
                    // Then delete the student
                    database.studentDao().delete(student);
                    
                    Toast.makeText(StudentListActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                    loadStudents();
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}