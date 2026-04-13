package vn.rikkei.presentation;

import vn.rikkei.service.StatisticService;

import java.util.List;

public class StatisticMenu {

    private static StatisticService service = new StatisticService();

    public static void showTop5() {

        List<String> list = service.getTop5Products();

        if (list.isEmpty()) {
            System.out.println("Chưa có dữ liệu!");
            return;
        }

        System.out.println("\n==========TOP 5 SẢN PHẨM BÁN CHẠY==========");

        System.out.printf("| %-3s | %-25s | %-5s |\n", "TOP", "Tên sản phẩm", "SL");
        System.out.println("---------------------------------------------");

        list.forEach(System.out::println);

        System.out.println("=============================================");
    }
}