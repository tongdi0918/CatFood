package com.catfood.model;

public class Product {
    private Integer pid;
    private String pname;
    private Double price;
    private String category;
    private Integer stock;
    private String image;
    private String description;
    // 1上架 0下架
    private Integer status;

    //无参构造，jsp反射必须要有！不能删
    public Product() {
    }

    //全参构造
    public Product(Integer pid, String pname, Double price, String category, Integer stock, String image, String description, Integer status) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.image = image;
        this.description = description;
        this.status = status;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", pname='" + pname + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
