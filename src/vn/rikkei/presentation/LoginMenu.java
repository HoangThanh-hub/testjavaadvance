package vn.rikkei.presentation;

import vn.rikkei.model.User;
import vn.rikkei.service.UserService;

import java.util.Scanner;

public class LoginMenu {

    private static Scanner sc = new Scanner(System.in);
    private static UserService userService = new UserService();

    public static void menu() {

        while (true) {
            System.out.println("\n========== PHONE STORE ==========");
            System.out.println("1. Đăng ký");
            System.out.println("2. Đăng nhập");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            int choice;

            // chống crash
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Vui lòng nhập số!");
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.println("\n----------Đăng ký----------");
                    register();
                    break;

                case 2:
                    System.out.println("\n----------Đăng nhập----------");
                    login();
                    break;

                case 0:
                    System.out.println("Thoát chương trình");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Chọn sai, hãy chọn lại");
            }
        }
    }

    // register
    private static void register() {

        System.out.print("Nhập tên: ");
        String name = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Mật khẩu: ");
        String password = sc.nextLine().trim();

        System.out.print("SĐT: ");
        String phone = sc.nextLine().trim();

        System.out.print("Địa chỉ: ");
        String address = sc.nextLine().trim();

        // validate
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("\nKhông được để trống");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("\nEmail không hợp lệ!");
            return;
        }

        if (password.length() < 6) {
            System.out.println("\nMật khẩu phải >= 6 ký tự!");
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            System.out.println("\nSĐT phải bắt đầu từ 0 và đủ 10 số");
            return;
        }

        if (userService.isEmailExist(email)) {
            System.out.println("\nEmail đã tồn tại");
            return;
        }

        User newUser = new User(name, email, password, phone, address);

        if (userService.register(newUser)) {
            System.out.println("\nĐăng ký thành công");
        } else {
            System.out.println("\nĐăng ký thất bại");
        }
    }

    // login
    private static void login() {

        System.out.print("Email: ");
        String loginEmail = sc.nextLine().trim();

        System.out.print("Mật khẩu: ");
        String loginPass = sc.nextLine().trim();

        if (loginEmail.isEmpty() || loginPass.isEmpty()) {
            System.out.println("\nKhông được để trống\n");
            return;
        }

        User user = userService.login(loginEmail, loginPass);

        if (user != null) {
            System.out.println("\nĐăng nhập thành công\n");

            // phân quyền
            if ("ADMIN".equals(user.getRole())) {
                System.out.println("Xin chào Admin");
                AdminMenu.menu();

            } else {
                String name = user.getName();
                System.out.println("Xin chào " +name);

                CustomerMenu.setCurrentUserId(user.getId());
                CustomerMenu.menu();
            }

        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
        }
    }
}