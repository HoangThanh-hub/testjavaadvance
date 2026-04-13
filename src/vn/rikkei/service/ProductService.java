package vn.rikkei.service;

import vn.rikkei.dao.ProductDAO;
import vn.rikkei.model.Product;

import java.util.List;

public class ProductService {
    public static int page = 1;
    public static int pageSize = 5;

    private ProductDAO dao = new ProductDAO();

    public List<Product> getAll() {
        return dao.findAll(page, pageSize);
    }

    public boolean add(Product p) {
        return dao.save(p);
    }

    public boolean update(Product p) {
        return dao.update(p);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public Product findById(int id) {
        return dao.findById(id);
    }

    public List<Product> search(String key) {
        return dao.search(key);
    }

    public List<Product> sort(boolean asc) {
        return dao.sortByPrice(asc);
    }
}