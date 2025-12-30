package com.example.attendancesystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.AttendanceReportActivity.StudentAttendanceInfo;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Adapter for displaying attendance reports in RecyclerView
 * Shows each student's attendance statistics
 */
public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.ReportViewHolder> {
    
    private Map<Integer, StudentAttendanceInfo> studentStats;
    private SimpleDateFormat dateFormat;
    
    /**
     * Default constructor
     */
    public AttendanceReportAdapter() {
    }
    
    /**
     * Set data for the adapter
     * @param studentStats Map of student ID to attendance statistics
     * @param dateFormat Date formatter for displaying dates
     */
    public void setData(Map<Integer, StudentAttendanceInfo> studentStats, SimpleDateFormat dateFormat) {
        this.studentStats = studentStats;
        this.dateFormat = dateFormat;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_attendance_report, parent, false);
        return new ReportViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        // Get all students from the map
        StudentAttendanceInfo[] students = studentStats.values().toArray(new StudentAttendanceInfo[0]);
        
        if (position < students.length) {
            StudentAttendanceInfo info = students[position];
            holder.bind(info);
        }
    }
    
    @Override
    public int getItemCount() {
        return studentStats != null ? studentStats.size() : 0;
    }
    
    /**
     * ViewHolder for attendance report items
     */
    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName;
        private TextView tvStudentNumber;
        private TextView tvPresentCount;
        private TextView tvAbsentCount;
        private TextView tvTotalSessions;
        private TextView tvPercentage;
        
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvPresentCount = itemView.findViewById(R.id.tvPresentCount);
            tvAbsentCount = itemView.findViewById(R.id.tvAbsentCount);
            tvTotalSessions = findViewById(R.id.tvTotalSessions);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
        }
        
        /**
         * Bind data to the view holder
         * @param info Student attendance information
         */
        public void bind(StudentAttendanceInfo info) {
            tvStudentName.setText(info.student.getName());
            tvStudentNumber.setText("شماره دانشجویی: " + info.student.getStudentNumber());
            
            int totalSessions = info.presentCount + info.absentCount;
            double percentage = totalSessions > 0 ? 
                (info.presentCount * 100.0 / totalSessions) : 0.0;
            
            tvPresentCount.setText(String.valueOf(info.presentCount));
            tvAbsentCount.setText(String.valueOf(info.absentCount));
            tvTotalSessions.setText(String.valueOf(totalSessions));
            
            String percentageText = String.format("%.1f%%", percentage);
            tvPercentage.setText(percentageText);
            
            // Color code the percentage
            if (percentage >= 75) {
                tvPercentage.setTextColor(itemView.getContext().getResources()
                    .getColor(R.color.present_green));
            } else if (percentage >= 50) {
                tvPercentage.setTextColor(itemView.getContext().getResources()
                    .getColor(android.R.color.holo_orange_dark));
            } else {
                tvPercentage.setTextColor(itemView.getContext().getResources()
                    .getColor(R.color.absent_red));
            }
        }
    }
}