package dao;

import model.Enrollment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    public EnrollmentDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS enrollments (id INT AUTO_INCREMENT PRIMARY KEY, student_id INT NOT NULL, course_id INT NOT NULL, semester VARCHAR(50) NOT NULL)";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] createTable error: " + e.getMessage());
        }
    }

    public void save(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, semester) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getCourseId());
            ps.setString(3, enrollment.getSemester());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    enrollment.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] save error: " + e.getMessage());
        }
    }

    public List<Enrollment> findAll() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, semester FROM enrollments";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Enrollment(rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"), rs.getString("semester")));
            }
        } catch (SQLException e) {
            System.err.println("[EnrollmentDAO] findAll error: " + e.getMessage());
        }
        return list;
    }
}
