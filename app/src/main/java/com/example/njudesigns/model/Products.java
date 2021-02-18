package com.example.njudesigns.model;

public class Products {

    String ProductId;
    String ProductName;
    Integer imageUrl;

    public Products(String productId, String productName, Integer imageUrl) {
        ProductId = productId;
        ProductName = productName;
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }
}
