package vn.rikkei.dao;

import vn.rikkei.model.Category;
import vn.rikkei.util.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Lấy danh sách
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE is_deleted = false";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDeleted(rs.getBoolean("is_deleted"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm mới
    public boolean add(Category category) {
        String sql = "INSERT INTO categories(name, is_deleted) VALUES (?, false)";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Cập nhật
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Xóa mềm
    public boolean delete(int id) {
        String sql = "UPDATE categories SET is_deleted = true WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Khôi phục (restore)
    public boolean restore(int id) {
        String sql = "UPDATE categories SET is_deleted = false WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Tìm theo ID
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("is_deleted")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 7. Tìm theo tên (QUAN TRỌNG - dùng cho thêm)
    public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("is_deleted")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 8. Check tên tồn tại (chỉ check chưa xóa)
    public boolean isNameExist(String name) {
        String sql = "SELECT * FROM categories WHERE name = ? AND is_deleted = false";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}