package com.example.attendancesystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.entity.ClassCourse;

import java.util.List;

/**
 * Adapter for displaying list of classes in RecyclerView
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    
    private List<ClassCourse> classList;
    private OnItemClickListener listener;
    
    /**
     * Interface for handling item clicks
     */
    public interface OnItemClickListener {
        void onItemClick(ClassCourse classCourse);
        void onTakeAttendanceClick(ClassCourse classCourse);
        void onViewReportClick(ClassCourse classCourse);
        void onDeleteClick(ClassCourse classCourse);
    }
    
    /**
     * Constructor
     * @param classList List of classes to display
     * @param listener Click listener for items
     */
    public ClassAdapter(List<ClassCourse> classList, OnItemClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassCourse classCourse = classList.get(position);
        holder.bind(classCourse, listener);
    }
    
    @Override
    public int getItemCount() {
        return classList.size();
    }
    
    /**
     * ViewHolder for class items
     */
    static class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCourseName;
        private TextView tvInstructor;
        private TextView tvDay;
        private TextView tvTime;
        private Button btnTakeAttendance;
        private Button btnViewReport;
        
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnTakeAttendance = itemView.findViewById(R.id.btnTakeAttendance);
            btnViewReport = itemView.findViewById(R.id.btnViewReport);
        }
        
        /**
         * Bind data to the view holder
         * @param classCourse The class to display
         * @param listener Click listener
         */
        public void bind(ClassCourse classCourse, OnItemClickListener listener) {
            tvCourseName.setText(classCourse.getCourseName());
            tvInstructor.setText("استاد: " + classCourse.getInstructorName());
            tvDay.setText("روز: " + classCourse.getDay());
            tvTime.setText("ساعت: " + classCourse.getTime());
            
            // Take Attendance button
            btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTakeAttendanceClick(classCourse);
                    }
                }
            });
            
            // View Report button
            btnViewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onViewReportClick(classCourse);
                    }
                }
            });
            
            // Long press to delete
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(classCourse);
                    }
                    return true;
                }
            });
        }
    }
}