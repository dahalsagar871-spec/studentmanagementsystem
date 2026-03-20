package dao;

import model.Result;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {
    public ResultDAO() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS results (id INT AUTO_INCREMENT PRIMARY KEY, enrollment_id INT NOT NULL, grade VARCHAR(10) NOT NULL)";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[ResultDAO] createTable error: " + e.getMessage());
        }
    }

    public void save(Result result) {
        String sql = "INSERT INTO results (enrollment_id, grade) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, result.getEnrollmentId());
            ps.setString(2, result.getGrade());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    result.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ResultDAO] save error: " + e.getMessage());
        }
    }

    public List<Result> findAll() {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT id, enrollment_id, grade FROM results";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Result(rs.getInt("id"), rs.getInt("enrollment_id"), rs.getString("grade")));
            }
        } catch (SQLException e) {
            System.err.println("[ResultDAO] findAll error: " + e.getMessage());
        }
        return list;
    }
}
