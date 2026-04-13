package vn.rikkei.service;

import vn.rikkei.dao.OrderDAO;
import vn.rikkei.model.CartItem;
import vn.rikkei.model.Order;
import vn.rikkei.util.ConnectionDB;

import java.sql.Connection;
import java.util.List;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    // đặt hàng
    public boolean placeOrder(int userId, List<CartItem> cart) {

        if (cart == null || cart.isEmpty()) {
            System.out.println("Giỏ hàng trống!");
            return false;
        }

        Connection conn = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            double total = cart.stream()
                    .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                    .sum();

            Order order = new Order();
            order.setUserId(userId);
            order.setTotalPrice(total);

            int orderId = orderDAO.save(conn, order);

            for (CartItem item : cart) {

                orderDAO.insertOrderDetail(
                        conn,
                        orderId,
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                );

                boolean ok = orderDAO.decreaseStock(
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
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    // lịch sử đơn hàng
    public List<Order> getMyOrders(int userId) {
        try (Connection conn = ConnectionDB.openConnection()) {
            return orderDAO.findByUserId(conn, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // admin lấy tất cả
    public List<Order> getAll() {
        try (Connection conn = ConnectionDB.openConnection()) {
            return orderDAO.findAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // cập nhật trạng thái
    public boolean updateStatus(int orderId, String status) {

        if ("CANCELLED".equals(status)) {
            return cancelOrder(orderId);
        }

        try (Connection conn = ConnectionDB.openConnection()) {
            return orderDAO.updateStatus(conn, orderId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // huỷ đơn
    public boolean cancelOrder(int orderId) {

        Connection conn = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            List<int[]> details = orderDAO.getOrderDetails(conn, orderId);

            for (int[] d : details) {
                orderDAO.increaseStock(conn, d[0], d[1]);
            }

            orderDAO.updateStatus(conn, orderId, "CANCELLED");

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}