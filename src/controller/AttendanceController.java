package controller;

import dao.AttendanceDAO;
import model.Attendance;

import java.time.LocalDate;
import java.util.List;

public class AttendanceController {
    private final AttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
    }

    public void addAttendance(int enrollmentId, LocalDate date, String status) {
        Attendance attendance = new Attendance();
        attendance.setEnrollmentId(enrollmentId);
        attendance.setDate(date);
        attendance.setStatus(status);
        attendanceDAO.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceDAO.findAll();
    }
}
