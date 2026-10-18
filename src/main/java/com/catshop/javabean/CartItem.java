package com.catshop.javabean;

/* 购物车项实体类
   product  ：商品含 pid/pname/price/image/stock
   quantity ：购买数量
 */
public class CartItem {
    private Product product;   // 商品
    private int quantity;      // 购买数量

    public CartItem() {}   // 无参构造
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /* 小计 = 单价 × 数量 */
    public double getSubtotal() { return product.getPrice() * quantity; }
}
