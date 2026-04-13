package vn.rikkei.model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private String storage;
    private String color;
    private double price;
    private int stock;
    private String description;
    private int categoryId;
    private String categoryName;
    private boolean isDeleted;

    public Product() {}

    public Product(int id, String name, String brand, String storage, String color,
                   double price, int stock, String description,
                   int categoryId, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.storage = storage;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }

    // GET SET
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    @Override
    public String toString() {
        return String.format(
                "| %-3d | %-20s | %-10s | %-10s | %-10s | %,12.0f | %-8d | %-10s |",
                id, name, brand, storage, color, price, stock, categoryName
        );
    }
}