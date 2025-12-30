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
import com.example.attendancesystem.model.StudentAttendanceInfo;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for viewing attendance reports for a specific class
 */
public class AttendanceReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvAttendance;
    private TextView tvCourseName, tvClassDetails, tvTotalSessions, tvPresentCount, tvAbsentCount, tvEmptyState;

    private AppDatabase database;
    private AttendanceReportAdapter attendanceAdapter;

    private int classId;
    private String courseName, instructorName, day, time;
    private List<Student> studentList;
    private List<Attendance> attendanceList;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

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

        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvAttendance = findViewById(R.id.rvAttendance);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvClassDetails = findViewById(R.id.tvClassDetails);
        tvTotalSessions = findViewById(R.id.tvTotalSessions);
        tvPresentCount = findViewById(R.id.tvPresentCount);
        tvAbsentCount = findViewById(R.id.tvAbsentCount);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        tvCourseName.setText(courseName);
        tvClassDetails.setText("استاد: " + instructorName + " | " + day + " - " + time);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("گزارش حضور و غیاب");
    }

    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceReportAdapter();
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));
        rvAttendance.setAdapter(attendanceAdapter);
    }

    private void loadData() {
        studentList = database.studentDao().getAllStudents();
        attendanceList = database.attendanceDao().getAttendanceByClass(classId);

        if (attendanceList.isEmpty()) {
            rvAttendance.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            tvTotalSessions.setText("0");
            tvPresentCount.setText("0");
            tvAbsentCount.setText("0");
            return;
        }

        rvAttendance.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);

        Map<Integer, StudentAttendanceInfo> studentStats = new HashMap<>();

        // ⚡ استفاده از سازنده با آرگومان‌ها
        for (Student student : studentList) {
            StudentAttendanceInfo info = new StudentAttendanceInfo(student, 0, 0);
            studentStats.put(student.getId(), info);
        }

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

        int totalPresent = 0;
        int totalAbsent = 0;
        for (StudentAttendanceInfo info : studentStats.values()) {
            totalPresent += info.presentCount;
            totalAbsent += info.absentCount;
        }

        int totalSessions = totalPresent + totalAbsent;

        tvTotalSessions.setText(String.valueOf(totalSessions));
        tvPresentCount.setText(String.valueOf(totalPresent));
        tvAbsentCount.setText(String.valueOf(totalAbsent));

        attendanceAdapter.setData(studentStats, dateFormat);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
