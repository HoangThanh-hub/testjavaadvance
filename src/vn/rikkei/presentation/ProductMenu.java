package vn.rikkei.presentation;

import vn.rikkei.model.Product;
import vn.rikkei.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class ProductMenu {
    private static ProductService service = new ProductService();
    private static Scanner sc = new Scanner(System.in);

    public static void menu() {
        while (true) {
            System.out.println("\n======== PRODUCT ========");
            System.out.println("1. Hiển thị");
            System.out.println("2. Thêm");
            System.out.println("3. Sửa");
            System.out.println("4. Xóa");
            System.out.println("5. Tìm kiếm");
            System.out.println("6. Sắp xếp giá");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.println("\n------------------------------------------------------------------------------------------------------------");
                    System.out.printf("| %-3s | %-20s | %-10s | %-10s | %-10s | %-12s | %-8s | %-10s |\n",
                            "ID","Tên sản phẩm", "Hãng", "Dung lượng", "Màu sắc", "Giá", "Số lượng", "Danh mục");
                    System.out.println("------------------------------------------------------------------------------------------------------------");


                    service.getAll().forEach(System.out::println);
                    System.out.println("------------------------------------------------------------------------------------------------------------");


                    break;

                case 2:
                    Product p = input();
                    if (p != null) service.add(p);
                    System.out.println("Thêm thành công");
                    break;

                case 3:
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Product old = service.findById(id);

                    if (old != null) {
                        Product newP = input();
                        if (newP != null) {
                            newP.setId(id);
                            service.update(newP);
                        }
                    }
                    break;

                case 4:
                    System.out.print("Nhập ID: ");
                    int idDelete = Integer.parseInt(sc.nextLine());

                    System.out.print("Bạn có chắc muốn xoá? (Y/N): ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        if (service.delete(idDelete)) {
                            System.out.println("Xoá thành công");
                        } else {
                            System.out.println("Xoá thất bại");
                        }
                    } else {
                        System.out.println("Đã huỷ xoá");
                    }
                    break;

                case 5:
                    System.out.print("\nBạn hãy nhập từ khóa muốn tìm: ");
                    List<Product> list = service.search(sc.nextLine());

                    if (list.isEmpty()) {
                        System.out.println("Không tìm thấy");
                    } else {
                        System.out.println("\n------------------------------------------------------------------------------------------------------------");
                        System.out.printf("| %-3s | %-20s | %-10s | %-10s | %-10s | %-12s | %-8s | %-10s |\n",
                                "ID","Tên sản phẩm", "Hãng", "Dung lượng", "Màu sắc", "Giá", "Số lượng", "Danh mục");
                        System.out.println("------------------------------------------------------------------------------------------------------------");
                        list.forEach(System.out::println);
                        System.out.println("------------------------------------------------------------------------------------------------------------");
                    }
                    break;

                case 6:
                    System.out.println("\n1. Tăng | 2. Giảm");
                    System.out.print("Nhập lựa chọn của bạn: ");
                    String a = sc.nextLine();
                    System.out.println("\n------------------------------------------------------------------------------------------------------------");
                    System.out.printf("| %-3s | %-20s | %-10s | %-10s | %-10s | %-12s | %-8s | %-10s |\n",
                            "ID","Tên sản phẩm", "Hãng", "Dung lượng", "Màu sắc", "Giá", "Số lượng", "Danh mục");
                    System.out.println("------------------------------------------------------------------------------------------------------------");
                    service.sort(a.equals("1"))
                            .forEach(System.out::println);
                    System.out.println("------------------------------------------------------------------------------------------------------------");
                    break;

                case 0:
                    return;
            }
        }
    }

    private static Product input() {
        Product p = new Product();

        System.out.print("Tên: ");
        p.setName(sc.nextLine());

        System.out.print("Hãng: ");
        p.setBrand(sc.nextLine());

        System.out.print("Dung lượng: ");
        p.setStorage(sc.nextLine());

        System.out.print("Màu: ");
        p.setColor(sc.nextLine());

        System.out.print("Giá: ");
        p.setPrice(Double.parseDouble(sc.nextLine()));

        System.out.print("Số lượng: ");
        p.setStock(Integer.parseInt(sc.nextLine()));

        if (p.getPrice() <= 0 || p.getStock() < 0) {
            System.out.println("Giá và số lượng phải > 0");
            return null;
        }

        System.out.print("Mô tả: ");
        p.setDescription(sc.nextLine());

        System.out.print("Category ID: ");
        p.setCategoryId(Integer.parseInt(sc.nextLine()));

        return p;
    }
}