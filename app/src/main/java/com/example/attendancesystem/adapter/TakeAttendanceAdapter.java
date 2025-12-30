package com.example.attendancesystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.entity.Attendance;
import com.example.attendancesystem.entity.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for taking attendance in RecyclerView
 * Allows toggling attendance status for each student
 */
public class TakeAttendanceAdapter extends RecyclerView.Adapter<TakeAttendanceAdapter.AttendanceViewHolder> {
    
    private List<Student> studentList;
    private List<Attendance> attendanceList;
    private long currentDate;
    private Map<Integer, Boolean> attendanceMap;
    
    /**
     * Constructor
     * @param studentList List of students
     * @param attendanceList List of existing attendance records
     * @param currentDate Current selected date
     */
    public TakeAttendanceAdapter(List<Student> studentList, List<Attendance> attendanceList, long currentDate) {
        this.studentList = studentList;
        this.attendanceList = attendanceList;
        this.currentDate = currentDate;
        this.attendanceMap = new HashMap<>();
        
        // Build attendance map for quick lookup
        buildAttendanceMap();
    }
    
    /**
     * Build a map of student ID to attendance status
     */
    private void buildAttendanceMap() {
        attendanceMap.clear();
        for (Attendance attendance : attendanceList) {
            if (attendance.getDate() == currentDate) {
                attendanceMap.put(attendance.getStudentId(), attendance.isPresent());
            }
        }
    }
    
    /**
     * Update attendance list and rebuild map
     * @param attendanceList New attendance list
     */
    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
        buildAttendanceMap();
    }
    
    /**
     * Check if a student is marked as present
     * @param studentId Student ID
     * @return true if present, false otherwise
     */
    public boolean isStudentPresent(int studentId) {
        return attendanceMap.containsKey(studentId) && attendanceMap.get(studentId);
    }
    
    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student, currentDate);
    }
    
    @Override
    public int getItemCount() {
        return studentList.size();
    }
    
    /**
     * ViewHolder for attendance items
     */
    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName;
        private TextView tvStudentNumber;
        private TextView tvStatus;
        private ImageView ivStatusIcon;
        
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
        }
        
        /**
         * Bind data to the view holder
         * @param student The student to display
         * @param date The date for attendance
         */
        public void bind(Student student, long date) {
            tvStudentName.setText(student.getName());
            tvStudentNumber.setText("شماره دانشجویی: " + student.getStudentNumber());
            
            // Get attendance status
            boolean isPresent = attendanceMap.containsKey(student.getId()) ? 
                attendanceMap.get(student.getId()) : true; // Default to present
            
            updateAttendanceDisplay(isPresent);
            
            // Click to toggle attendance status
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newStatus = !isStudentPresent(student.getId());
                    attendanceMap.put(student.getId(), newStatus);
                    updateAttendanceDisplay(newStatus);
                }
            });
        }
        
        /**
         * Update display based on attendance status
         * @param isPresent true if present, false if absent
         */
        private void updateAttendanceDisplay(boolean isPresent) {
            if (isPresent) {
                tvStatus.setText("حاضر");
                tvStatus.setTextColor(itemView.getContext().getResources()
                    .getColor(R.color.present_green));
                ivStatusIcon.setImageResource(android.R.drawable.presence_online);
                ivStatusIcon.setImageTintList(itemView.getContext().getResources()
                    .getColorStateList(R.color.present_green));
            } else {
                tvStatus.setText("غایب");
                tvStatus.setTextColor(itemView.getContext().getResources()
                    .getColor(R.color.absent_red));
                ivStatusIcon.setImageResource(android.R.drawable.presence_offline);
                ivStatusIcon.setImageTintList(itemView.getContext().getResources()
                    .getColorStateList(R.color.absent_red));
            }
        }
    }
}