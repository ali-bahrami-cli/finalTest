package com.example.attendancesystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.model.StudentAttendanceInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter for displaying attendance reports in RecyclerView
 * Shows each student's attendance statistics
 */
public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.ReportViewHolder> {

    private List<StudentAttendanceInfo> studentList = new ArrayList<>();
    private SimpleDateFormat dateFormat;

    public AttendanceReportAdapter() {
    }

    /**
     * Set data for the adapter
     *
     * @param studentStats Map of student ID to attendance statistics
     * @param dateFormat   Date formatter for displaying dates
     */
    public void setData(Map<Integer, StudentAttendanceInfo> studentStats, SimpleDateFormat dateFormat) {
        this.studentList = new ArrayList<>(studentStats.values());
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
        if (position < studentList.size()) {
            holder.bind(studentList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    /**
     * ViewHolder for attendance report items
     */
    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentName;
        private final TextView tvStudentNumber;
        private final TextView tvPresentCount;
        private final TextView tvAbsentCount;
        private final TextView tvTotalSessions;
        private final TextView tvPercentage;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvPresentCount = itemView.findViewById(R.id.tvPresentCount);
            tvAbsentCount = itemView.findViewById(R.id.tvAbsentCount);
            tvTotalSessions = itemView.findViewById(R.id.tvTotalSessions);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
        }

        public void bind(StudentAttendanceInfo info) {
            tvStudentName.setText(info.student.getName());
            tvStudentNumber.setText("شماره دانشجویی: " + info.student.getStudentNumber());

            int totalSessions = info.presentCount + info.absentCount;
            double percentage = totalSessions > 0 ? (info.presentCount * 100.0 / totalSessions) : 0.0;

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
