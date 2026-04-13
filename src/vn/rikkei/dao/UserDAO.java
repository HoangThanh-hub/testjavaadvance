package vn.rikkei.dao;

import vn.rikkei.model.User;
import vn.rikkei.util.ConnectionDB;

import java.sql.*;

public class UserDAO {

    // đăng kí
    public boolean register(User user) {

        String sql = "INSERT INTO users(name, email, password, phone, address, role, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // đã hash ở Service
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // kiểm tra email
    public boolean isEmailExist(String email) {

        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // tìm kiếm email
    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // tìm kiếm theo id
    public User findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // map kết quả
    private User map(ResultSet rs) throws SQLException {

        Timestamp ts = rs.getTimestamp("created_at");

        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("role"),
                ts != null ? ts.toLocalDateTime() : null
        );
    }
}