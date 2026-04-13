package vn.rikkei.presentation;

import vn.rikkei.service.StatisticService;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private static Scanner sc = new Scanner(System.in);
    private static StatisticService statisticService = new StatisticService();

    public static void menu() {

        while (true) {
            System.out.println("\n======== ADMIN MENU ========");
            System.out.println("1. Quản lý danh mục");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Quản lý đơn hàng");
            System.out.println("4. Top 5 sản phẩm bán chạy");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    CategoryMenu.menu();
                    break;

                case 2:
                    ProductMenu.menu();
                    break;

                case 3:
                    AdminOrderMenu.menu();
                    break;

                case 4:
                    StatisticMenu.showTop5();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Sai lựa chọn!");
            }
        }

    }
}