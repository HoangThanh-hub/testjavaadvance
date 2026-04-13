package vn.rikkei.dao;

import vn.rikkei.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // lưu order
    public int save(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders(user_id, total_price, status) VALUES (?, ?, 'PENDING')";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalPrice());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // thêm chi tiết đơn
    public void insertOrderDetail(Connection conn, int orderId, int productId, int qty, double price) throws SQLException {
        String sql = "INSERT INTO order_details(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            ps.setDouble(4, price);
            ps.executeUpdate();
        }
    }

    // trừ stock
    public boolean decreaseStock(Connection conn, int productId, int qty) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        }
    }

    // tăng stock (khi huỷ)
    public void increaseStock(Connection conn, int productId, int qty) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // lấy chi tiết đơn
    public List<int[]> getOrderDetails(Connection conn, int orderId) throws SQLException {
        List<int[]> list = new ArrayList<>();
        String sql = "SELECT product_id, quantity FROM order_details WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new int[]{
                            rs.getInt("product_id"),
                            rs.getInt("quantity")
                    });
                }
            }
        }
        return list;
    }

    // lấy đơn theo user
    public List<Order> findByUserId(Connection conn, int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    // lấy tất cả
    public List<Order> findAll(Connection conn) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    // update status
    public boolean updateStatus(Connection conn, int id, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // map
    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalPrice(rs.getDouble("total_price"));
        o.setStatus(rs.getString("status"));
        return o;
    }
}