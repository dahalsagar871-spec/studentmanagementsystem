package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UserDAO() {
        createTable();
        ensureAdminUser();
        ensureTeacherUser();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(100) NOT NULL, role VARCHAR(20) NOT NULL)";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[UserDAO] createTable error: " + e.getMessage());
        }
    }

    private void ensureAdminUser() {
        if (findByUsername("admin") == null) {
            save(new User(0, "admin", "admin", "admin"));
        }
    }

    private void ensureTeacherUser() {
        if (findByUsername("teacher") == null) {
            save(new User(0, "teacher", "teacher", "teacher"));
        }
    }

    public boolean save(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[UserDAO] save error: " + e.getMessage());
            return false;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findByUsername error: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role FROM users";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] findAll error: " + e.getMessage());
        }
        return users;
    }
}
