package vn.rikkei.presentation;

import vn.rikkei.model.Category;
import vn.rikkei.service.CategoryService;

import java.util.List;
import java.util.Scanner;

public class CategoryMenu {
    private static CategoryService service = new CategoryService();
    private static Scanner sc = new Scanner(System.in);

    public static void menu() {
        while (true) {
            System.out.println("\n======== CATEGORY ========");
            System.out.println("1. Hiển thị");
            System.out.println("2. Thêm");
            System.out.println("3. Sửa");
            System.out.println("4. Xóa");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("\n------------------------------");
                    System.out.println("| ID    | TÊN DANH MỤC       |");
                    System.out.println("------------------------------");
                    List<Category> list = service.getAll();
                    list.forEach(System.out::println);
                    System.out.println("------------------------------");
                    break;

                case 2:
                    System.out.print("\nNhập tên: ");
                    String name = sc.nextLine();
                    service.add(name);
                    System.out.println("Thêm thành công");
                    break;

                case 3:
                    System.out.print("\nNhập ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("Tên mới: ");
                    String newName = sc.nextLine();
                    service.update(id, newName);
                    System.out.println("Cập nhật thành công");
                    break;

                case 4:
                    System.out.print("\nNhập ID cần xoá: ");
                    int idcate = Integer.parseInt(sc.nextLine());

                    System.out.print("Bạn có chắc muốn xoá? (Y/N): ");
                    String confirm = sc.nextLine();

                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("Đã huỷ xoá");
                        break;
                    }

                    if (service.delete(idcate)) {
                        System.out.println("Xoá thành công");
                    } else {
                        System.out.println("Xoá thất bại");
                    }
                    break;

                case 0:
                    return;
            }
        }
    }
}