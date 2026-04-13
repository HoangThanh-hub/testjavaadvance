package vn.rikkei.presentation;

import vn.rikkei.model.CartItem;
import vn.rikkei.model.Order;
import vn.rikkei.model.Product;
import vn.rikkei.model.User;
import vn.rikkei.service.OrderService;
import vn.rikkei.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private static Scanner sc = new Scanner(System.in);
    private static List<CartItem> cart = new ArrayList<>();
    private static OrderService orderService = new OrderService();
    private static ProductService productService = new ProductService();

    private static User currentUser; // 🔥 lưu user login

    // 👉 gọi từ login
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void menu() {
        while (true) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. Xem sản phẩm");
            System.out.println("2. Thêm vào giỏ");
            System.out.println("3. Xem giỏ");
            System.out.println("4. Đặt hàng");
            System.out.println("5. Lịch sử đơn");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    showProducts();
                    break;

                case 2:
                    addToCart();
                    break;

                case 3:
                    showCart();
                    break;

                case 4:
                    placeOrder();
                    break;

                case 5:
                    showHistory();
                    break;

                case 0:
                    currentUser = null; // logout
                    return;

                default:
                    System.out.println("Chọn sai!");
            }
        }
    }

    // hiển thị sản phẩm
    private static void showProducts() {
        System.out.println("\n================================= DANH SÁCH SẢN PHẨM =================================");

        System.out.printf("| %-3s | %-15s | %-10s | %-10s | %-6s | %-12s | %-5s |\n",
                "ID", "Tên", "Hãng", "Dung lượng", "Màu", "Giá", "Số lượng");
        System.out.println("--------------------------------------------------------------------------------------");

        for (Product p : productService.getAll()) {
            System.out.printf("| %-3d | %-15s | %-10s | %-10s | %-6s | %,12.0f | %-8d |\n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getStorage(),
                    p.getColor(),
                    p.getPrice(),
                    p.getStock());
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }

    // thêm vào giỏ
    private static void addToCart() {
        System.out.print("\nNhập ID sản phẩm bạn muốn thêm: ");
        int id = Integer.parseInt(sc.nextLine());

        Product p = productService.findById(id);

        if (p == null || p.getStock() <= 0) {
            System.out.println("\nKhông có sản phẩm");
            return;
        }

        System.out.print("Số lượng: ");
        int qty = Integer.parseInt(sc.nextLine());

        if (qty > p.getStock()) {
            System.out.println("\nKhông đủ hàng");
            return;
        }

        for (CartItem item : cart) {
            if (item.getProduct().getId() == id) {
                item.setQuantity(item.getQuantity() + qty);
                System.out.println("\nĐã thêm vào giỏ");
                return;
            }
        }

        cart.add(new CartItem(p, qty));
        System.out.println("\nĐã thêm");
    }

    // hiển thị giỏ
    private static void showCart() {

        if (cart.isEmpty()) {
            System.out.println("\nGiỏ hàng của bạn trống");
            return;
        }

        double total = 0;

        System.out.println("\n========================== GIỎ HÀNG ==========================");

        System.out.printf("| %-20s | %-5s | %-12s | %-12s |\n",
                "Tên", "SL", "Giá", "Thành tiền");
        System.out.println("--------------------------------------------------------------");

        for (CartItem i : cart) {
            double money = i.getProduct().getPrice() * i.getQuantity();
            total += money;

            System.out.printf("| %-20s | %-5d | %,12.0f | %,12.0f |\n",
                    i.getProduct().getName(),
                    i.getQuantity(),
                    i.getProduct().getPrice(),
                    money);

            System.out.println("--------------------------------------------------------------");
        }

        System.out.println("-------------------------");
        System.out.printf("|   TỔNG: %,12.0f  |\n", total);
        System.out.println("-------------------------");
    }

    // đặt hàng
    private static void placeOrder() {

        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập!");
            return;
        }

        if (cart.isEmpty()) {
            System.out.println("Giỏ trống");
            return;
        }

        try {
            orderService.placeOrder(currentUser.getId(), cart);
            System.out.println("Đặt hàng thành công!");
            cart.clear(); // clear giỏ sau khi đặt
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // lịch sử đơn
    private static void showHistory() {

        if (currentUser == null) {
            System.out.println("Bạn cần đăng nhập!");
            return;
        }

        List<Order> list = orderService.getMyOrders(currentUser.getId());

        if (list == null || list.isEmpty()) {
            System.out.println("Chưa có đơn!");
            return;
        }

        System.out.println("\n============== LỊCH SỬ ĐƠN =============");

        System.out.printf("| %-5s | %-15s | %-10s |\n",
                "ID", "Tổng tiền", "Trạng thái");
        System.out.println("----------------------------------------");

        for (Order o : list) {
            System.out.printf("| %-5d | %,15.0f | %-10s |\n",
                    o.getId(),
                    o.getTotalPrice(),
                    o.getStatus());
            System.out.println("----------------------------------------");
        }
    }
}