package vn.rikkei.service;

import vn.rikkei.dao.OrderDAO;
import vn.rikkei.model.CartItem;
import vn.rikkei.model.Order;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    // đặt hàng
    public void placeOrder(int userId, List<CartItem> cart) {

        if (cart == null || cart.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        double total = cart.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);

        boolean success = orderDAO.placeOrder(order, cart);

        if (!success) {
            throw new RuntimeException("Đặt hàng thất bại!");
        }
    }

    // lịch sử đơn hàng
    public List<Order> getMyOrders(int userId) {
        return orderDAO.findByUserId(userId);
    }

    // admin lấy tất cả
    public List<Order> getAll() {
        return orderDAO.findAll();
    }

    // cập nhật trạng thái
    public void updateStatus(int orderId, String status) {

        if ("CANCELLED".equals(status)) {
            cancelOrder(orderId);
            return;
        }

        if (!orderDAO.updateStatus(orderId, status)) {
            throw new RuntimeException("Cập nhật trạng thái thất bại!");
        }
    }

    // huỷ đơn
    public void cancelOrder(int orderId) {

        if (!orderDAO.cancelOrder(orderId)) {
            throw new RuntimeException("Huỷ đơn thất bại!");
        }
    }
}