package dao;

import model.Course;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public CourseDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS courses (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, credits INT NOT NULL)";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[CourseDAO] createTable error: " + e.getMessage());
        }
    }

    public void save(Course course) {
        String sql = "INSERT INTO courses (name, credits) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getName());
            ps.setInt(2, course.getCredits());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    course.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] save error: " + e.getMessage());
        }
    }

    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, name, credits FROM courses";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("name"), rs.getInt("credits")));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] findAll error: " + e.getMessage());
        }
        return courses;
    }
}
