package com.example.attendancesystem.model;

public class AttendanceSummary {
    public boolean isPresent;
    public int count;

    public AttendanceSummary(boolean isPresent, int count) {
        this.isPresent = isPresent;
        this.count = count;
    }
}
