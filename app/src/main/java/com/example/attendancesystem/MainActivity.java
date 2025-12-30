package com.example.attendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * Main Activity - Entry point of the Attendance System application
 * Provides navigation to Class Management and Student Management sections
 */
public class MainActivity extends AppCompatActivity {
    
    /**
     * CardView for navigating to Class Management
     */
    private CardView cardClasses;
    
    /**
     * CardView for navigating to Student Management
     */
    private CardView cardStudents;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        initViews();
        
        // Set click listeners
        setupClickListeners();
    }
    
    /**
     * Initialize all views
     */
    private void initViews() {
        cardClasses = findViewById(R.id.cardClasses);
        cardStudents = findViewById(R.id.cardStudents);
    }
    
    /**
     * Setup click listeners for navigation
     */
    private void setupClickListeners() {
        // Navigate to Class List Activity
        cardClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassListActivity.class);
                startActivity(intent);
            }
        });
        
        // Navigate to Student List Activity
        cardStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
                startActivity(intent);
            }
        });
    }
}