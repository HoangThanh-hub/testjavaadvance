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
                    String control;

                    do {
                        System.out.println("\nTrang: " + ProductService.page);

                        System.out.println("------------------------------------------------------------------------------------------------------------");
                        System.out.printf("| %-3s | %-20s | %-10s | %-10s | %-10s | %-12s | %-8s | %-10s |\n",
                                "ID","Tên sản phẩm", "Hãng", "Dung lượng", "Màu sắc", "Giá", "Số lượng", "Danh mục");
                        System.out.println("------------------------------------------------------------------------------------------------------------");
                        service.getAll().forEach(System.out::println);

                        System.out.println("------------------------------------------------------------------------------------------------------------");

                        System.out.println("n - Trang tiếp | p - Trang trước | q - Thoát");
                        control = sc.nextLine();

                        if (control.equalsIgnoreCase("n")) {
                            ProductService.page++;
                        } else if (control.equalsIgnoreCase("p") && ProductService.page > 1) {
                            ProductService.page--;
                        }

                    } while (!control.equalsIgnoreCase("q"));

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

                        // Hiển thị thông tin cũ
                        System.out.println("===== Thông tin cũ =====");
                        System.out.println("ID: " + old.getId());
                        System.out.println("Name: " + old.getName());
                        System.out.printf("Price: %,.0f\n", old.getPrice());
                        System.out.println("Stock: " + old.getStock());
                        System.out.println("Category ID: " + old.getCategoryId());
                        System.out.println("Describe: " + old.getDescription());

                        System.out.println("===== Nhập thông tin mới =====");

                        // Nhập mới
                        Product newP = new Product();

                        System.out.print("Name: ");
                        newP.setName(sc.nextLine());

                        System.out.print("Price: ");
                        newP.setPrice(Double.parseDouble(sc.nextLine()));

                        System.out.print("Stock: ");
                        newP.setStock(Integer.parseInt(sc.nextLine()));

                        System.out.print("Category ID: ");
                        newP.setCategoryId(Integer.parseInt(sc.nextLine()));

                        System.out.print("Describe: ");
                        newP.setDescription(sc.nextLine());

                        //Giữ nguyên ID cũ
                        newP.setId(id);

                        service.update(newP);

                        System.out.println("Cập nhật thành công!");

                    } else {
                        System.out.println("Không tìm thấy sản phẩm!");
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