package vn.rikkei.presentation;

import vn.rikkei.service.OrderService;

import java.util.Scanner;

public class AdminOrderMenu {

    private static OrderService orderService = new OrderService();
    private static Scanner sc = new Scanner(System.in);

    public static void menu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ ĐƠN HÀNG =====");
            System.out.println("1. Xem tất cả đơn");
            System.out.println("2. Cập nhật trạng thái");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n================ DANH SÁCH ĐƠN HÀNG ================");

                    System.out.printf("| %-5s | %-7s | %-15s | %-12s |\n",
                            "ID", "UserID", "Tổng tiền", "Trạng thái");

                    System.out.println("----------------------------------------------------");

                    orderService.getAll().forEach(o ->
                            System.out.printf("| %-5d | %-7d | %,15.0f | %-12s |\n",
                                    o.getId(),
                                    o.getUserId(),
                                    o.getTotalPrice(),
                                    o.getStatus()
                            )
                    );

                    System.out.println("----------------------------------------------------");
                    break;

                case "2":
                    System.out.print("Nhập ID đơn: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.println("1. SHIPPING");
                    System.out.println("2. DELIVERED");
                    System.out.println("3. CANCELLED");

                    String status = switch (sc.nextLine()) {
                        case "1" -> "SHIPPING";
                        case "2" -> "DELIVERED";
                        default -> "CANCELLED";
                    };

                    try {
                        orderService.updateStatus(id, status);
                        System.out.println("Cập nhật thành công!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "0":
                    return;
            }
        }
    }
}