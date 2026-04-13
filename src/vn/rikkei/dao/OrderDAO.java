package vn.rikkei.dao;

import vn.rikkei.model.CartItem;
import vn.rikkei.model.Order;
import vn.rikkei.util.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // ================== SAVE ORDER ==================
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

        throw new SQLException("Không tạo được order!");
    }

    // ================== ORDER DETAIL ==================
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

    // ================== STOCK ==================
    public boolean decreaseStock(Connection conn, int productId, int qty) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        }
    }

    public void increaseStock(Connection conn, int productId, int qty) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // ================== GET ORDER DETAILS ==================
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

    // ================== FIND ==================
    public List<Order> findByUserId(Connection conn, int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY id DESC";

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

    // OVERLOAD (fix lỗi cho Service)
    public List<Order> findByUserId(int userId) {
        try (Connection conn = ConnectionDB.openConnection()) {
            return findByUserId(conn, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Order> findAll(Connection conn) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    // OVERLOAD
    public List<Order> findAll() {
        try (Connection conn = ConnectionDB.openConnection()) {
            return findAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // ================== UPDATE STATUS ==================
    public boolean updateStatus(Connection conn, int id, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // OVERLOAD
    public boolean updateStatus(int id, String status) {
        try (Connection conn = ConnectionDB.openConnection()) {
            return updateStatus(conn, id, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================== MAP ==================
    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalPrice(rs.getDouble("total_price"));
        o.setStatus(rs.getString("status"));
        return o;
    }

    // ================== PLACE ORDER ==================
    public boolean placeOrder(Order order, List<CartItem> cart) {
        Connection conn = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            int orderId = save(conn, order);

            for (CartItem item : cart) {

                insertOrderDetail(
                        conn,
                        orderId,
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                );

                boolean ok = decreaseStock(
                        conn,
                        item.getProduct().getId(),
                        item.getQuantity()
                );

                if (!ok) {
                    throw new RuntimeException("Không đủ hàng!");
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    // ================== CANCEL ORDER ==================
    public boolean cancelOrder(int orderId) {
        Connection conn = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            List<int[]> details = getOrderDetails(conn, orderId);

            if (details.isEmpty()) {
                throw new RuntimeException("Order không tồn tại!");
            }

            for (int[] d : details) {
                increaseStock(conn, d[0], d[1]);
            }

            updateStatus(conn, orderId, "CANCELLED");

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}