package vn.rikkei.dao;

import vn.rikkei.util.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public List<String> getTop5Products() {
        List<String> list = new ArrayList<>();

        String sql = "SELECT p.name, SUM(od.quantity) AS total_sold " +
                "FROM order_details od " +
                "JOIN products p ON od.product_id = p.id " +
                "JOIN orders o ON od.order_id = o.id " +
                "WHERE o.status = 'DELIVERED' " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sold DESC " +
                "LIMIT 5";

        try (Connection conn = ConnectionDB.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int rank = 1;

            while (rs.next()) {
                String line = String.format(
                        "| %-3d | %-25s | %5d |",
                        rank++,
                        rs.getString("name"),
                        rs.getInt("total_sold")
                );
                list.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}