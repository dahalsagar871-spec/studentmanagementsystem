package model;

import java.time.LocalDate;

public class Attendance {
    private int id;
    private int enrollmentId;
    private LocalDate date;
    private String status;

    public Attendance() {}

    public Attendance(int id, int enrollmentId, LocalDate date, String status) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
