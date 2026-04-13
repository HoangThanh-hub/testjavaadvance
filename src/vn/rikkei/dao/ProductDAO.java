package vn.rikkei.dao;

import vn.rikkei.model.Product;
import vn.rikkei.util.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // lấy tất cả
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE p.is_deleted = false AND p.stock > 0";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // tìm theo id
    public Product findById(int id) {
        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE p.id = ? AND p.is_deleted = false";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return map(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // lưu
    public boolean save(Product p) {
        String sql = "INSERT INTO products(name, brand, storage, color, price, stock, description, category_id, is_deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, false)";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParams(ps, p);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // cập nhật
    public boolean update(Product p) {
        String sql = "UPDATE products SET name=?, brand=?, storage=?, color=?, price=?, stock=?, description=?, category_id=? WHERE id=?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParams(ps, p);
            ps.setInt(9, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // xoá
    public boolean delete(int id) {
        String sql = "UPDATE products SET is_deleted = true WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // tìm kiếm
    public List<Product> search(String keyword) {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE (LOWER(p.name) LIKE LOWER(?) OR LOWER(p.brand) LIKE LOWER(?)) " +
                "AND p.is_deleted = false";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword.trim() + "%");
            ps.setString(2, "%" + keyword.trim() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //sắp xếp
    public List<Product> sortByPrice(boolean asc) {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "WHERE p.is_deleted = false " +
                "ORDER BY p.price " + (asc ? "ASC" : "DESC");

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // cập nhật số lượng
    public void updateStock(int productId, int qty) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    giảm số lượng
    public void increaseStock(int productId, int qty) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // map
    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("storage"),
                rs.getString("color"),
                rs.getDouble("price"),
                rs.getInt("stock"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getBoolean("is_deleted")
        );

        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (Exception ignored) {}

        return p;
    }

    private void setParams(PreparedStatement ps, Product p) throws SQLException {
        ps.setString(1, p.getName());
        ps.setString(2, p.getBrand());
        ps.setString(3, p.getStorage());
        ps.setString(4, p.getColor());
        ps.setDouble(5, p.getPrice());
        ps.setInt(6, p.getStock());
        ps.setString(7, p.getDescription());
        ps.setInt(8, p.getCategoryId());
    }
}