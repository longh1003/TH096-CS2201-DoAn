package com.doan.pharcity.Fragment;

public class Orders {
    private String id; // Mã đơn hàng
    private String productId; // Mã sản phẩm
    private String customerName; // Tên khách hàng
    private String nameProduct;
    private String orderDate; // Ngày đặt hàng
    private double totalPrice; // Tổng tiền
    private Integer status; // Trạng thái đơn hàng
    private int quantity; // Số lượng sản phẩm trong đơn hàng
    private int image;

    public Orders(String id, String productId, String customerName, String nameProduct
            ,int image, String orderDate, double totalPrice, Integer status, int quantity) {
        this.id = id;
        this.productId = productId;
        this.customerName = customerName;
        this.nameProduct = nameProduct;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.quantity = quantity;
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
