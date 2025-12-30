package com.example.attendancesystem.model;

import com.example.attendancesystem.entity.Student;

public class StudentAttendanceInfo {
    public Student student;
    public int presentCount;
    public int absentCount;

    // سازنده با آرگومان‌ها
    public StudentAttendanceInfo(Student student, int presentCount, int absentCount) {
        this.student = student;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
    }

    // سازنده بدون آرگومان
    public StudentAttendanceInfo() {
    }
}
