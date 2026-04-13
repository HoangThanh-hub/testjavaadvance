package vn.rikkei.model;

public class Category {
    private int id;
    private String name;
    private boolean isDeleted;

    public Category() {
    }

    public Category(int id, String name, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public Category(String name) {
        this.name = name;
        this.isDeleted = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-18s |", id, name);

    }
}