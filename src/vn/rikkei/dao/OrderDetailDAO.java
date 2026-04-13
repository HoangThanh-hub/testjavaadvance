package vn.rikkei.dao;

import vn.rikkei.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDetailDAO {

    public void save(OrderDetail od, Connection conn) throws Exception {
        String sql = "INSERT INTO order_details(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, od.getOrderId());
        ps.setInt(2, od.getProductId());
        ps.setInt(3, od.getQuantity());
        ps.setDouble(4, od.getPrice());

        ps.executeUpdate();
    }
}