package vn.rikkei.service;

import vn.rikkei.dao.CategoryDAO;
import vn.rikkei.model.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAll() {
        return categoryDAO.findAll();
    }

    public boolean add(String name) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Tên không được để trống!");
            return false;
        }

        Category old = categoryDAO.findByName(name);

        if (old != null) {
            if (old.isDeleted()) {
                // khôi phục
                categoryDAO.restore(old.getId());
                System.out.println("Danh mục đã được khôi phục!");
                return true;
            } else {
                System.out.println("Tên đã tồn tại!");
                return false;
            }
        }

        // chưa có thì sẽ tgheem mới
        return categoryDAO.add(new Category(name));
    }

    public boolean update(int id, String name) {
        Category c = categoryDAO.findById(id);
        if (c == null) {
            System.out.println("Không tìm thấy ID");
            return false;
        }

        c.setName(name);
        return categoryDAO.update(c);
    }

    public boolean delete(int id) {
        Category c = categoryDAO.findById(id);
        if (c == null) {
            System.out.println("Không tìm thấy ID");
            return false;
        }

        return categoryDAO.delete(id);
    }
}