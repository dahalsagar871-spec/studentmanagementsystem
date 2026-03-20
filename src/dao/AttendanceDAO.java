package dao;

import model.Attendance;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public AttendanceDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS attendance (id INT AUTO_INCREMENT PRIMARY KEY, enrollment_id INT NOT NULL, attendance_date DATE NOT NULL, status VARCHAR(20) NOT NULL)";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[AttendanceDAO] createTable error: " + e.getMessage());
        }
    }

    public void save(Attendance attendance) {
        String sql = "INSERT INTO attendance (enrollment_id, attendance_date, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, attendance.getEnrollmentId());
            ps.setDate(2, Date.valueOf(attendance.getDate()));
            ps.setString(3, attendance.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    attendance.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AttendanceDAO] save error: " + e.getMessage());
        }
    }

    public List<Attendance> findAll() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT id, enrollment_id, attendance_date, status FROM attendance ORDER BY attendance_date DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("enrollment_id"),
                        rs.getDate("attendance_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[AttendanceDAO] findAll error: " + e.getMessage());
        }
        return list;
    }
}
