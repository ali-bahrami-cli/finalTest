package com.example.attendancesystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.entity.Student;

import java.util.List;

/**
 * Adapter for displaying list of students in RecyclerView
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    
    private List<Student> studentList;
    private OnItemClickListener listener;
    
    /**
     * Interface for handling item clicks
     */
    public interface OnItemClickListener {
        void onEditClick(Student student);
        void onDeleteClick(Student student);
    }
    
    /**
     * Constructor
     * @param studentList List of students to display
     * @param listener Click listener for items
     */
    public StudentAdapter(List<Student> studentList, OnItemClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student, listener);
    }
    
    @Override
    public int getItemCount() {
        return studentList.size();
    }
    
    /**
     * ViewHolder for student items
     */
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName;
        private TextView tvStudentNumber;
        private Button btnEdit;
        private Button btnDelete;
        
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
        
        /**
         * Bind data to the view holder
         * @param student The student to display
         * @param listener Click listener
         */
        public void bind(Student student, OnItemClickListener listener) {
            tvStudentName.setText(student.getName());
            tvStudentNumber.setText("شماره دانشجویی: " + student.getStudentNumber());
            
            // Edit button
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEditClick(student);
                    }
                }
            });
            
            // Delete button
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(student);
                    }
                }
            });
        }
    }
}