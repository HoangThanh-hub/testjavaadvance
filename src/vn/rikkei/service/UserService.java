package vn.rikkei.service;

import vn.rikkei.dao.UserDAO;
import vn.rikkei.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // đăng ký
    public boolean register(User user) {

        // validate
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            System.out.println("Tên không được để trống!");
            return false;
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Email không được để trống!");
            return false;
        }

        if (!user.getEmail().contains("@")) {
            System.out.println("Email không hợp lệ!");
            return false;
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Mật khẩu không được để trống!");
            return false;
        }

        if (user.getPhone() == null || !user.getPhone().matches("\\d{10}")) {
            System.out.println("SĐT phải 10 số!");
            return false;
        }

        if (userDAO.isEmailExist(user.getEmail())) {
            System.out.println("Email đã tồn tại!");
            return false;
        }

        // mã hoá pass
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        // mặc định role
        if (user.getRole() == null) {
            user.setRole("CUSTOMER");
        }

        return userDAO.register(user);
    }

    // đăng nhập
    public User login(String email, String password) {

        User user = userDAO.findByEmail(email);

        if (user == null) return null;

        // so sánh mk BCrypt
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public boolean isEmailExist(String email) {
        return userDAO.isEmailExist(email);
    }
}