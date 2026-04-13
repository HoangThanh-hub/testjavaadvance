package vn.rikkei.presentation;

import vn.rikkei.model.Product;
import vn.rikkei.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class CustomerProductMenu {
    private static ProductService service = new ProductService();
    private static Scanner sc = new Scanner(System.in);

    public static void menu() {
        while (true) {
            System.out.println("===== PRODUCT (CUSTOMER) =====");
            System.out.println("1. Xem sản phẩm còn hàng");
            System.out.println("2. Tìm kiếm sản phẩm");
            System.out.println("0. Quay lại");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    showAvailableProducts();
                    break;
                case 2:
                    searchProduct();
                    break;
                case 0:
                    return;
            }
        }
    }

    // hiển thị sản phẩm còn hàng
    private static void showAvailableProducts() {
        List<Product> list = service.getAll();

        for (Product p : list) {
            if (p.getStock() > 0) {
                System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice() + " | SL: " + p.getStock());
            }
        }
    }

    // tìm kiếm
    private static void searchProduct() {
        System.out.print("Nhập tên: ");
        String key = sc.nextLine();

        List<Product> list = service.search(key);

        for (Product p : list) {
            if (p.getStock() > 0) {
                System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice());
            }
        }
    }
}