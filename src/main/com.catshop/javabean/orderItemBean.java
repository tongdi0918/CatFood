package com.catfood.javabean;
import java.math.BigDecimal;

/* 订单明细实体类 */
public class OrderItem {
    private int id;         //订单明细主键
    private int orderId;        // 外键,这条明细属于哪个订单
    private int productId;          //商品 id
    private String productName;
    private BigDecimal price;
    private int quantity;       //购买数量


    public OrderItem() {}       //无参构造

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /* 总价 = 单价 × 数量  */
    public BigDecimal getSubtotal() {  return price.multiply(new BigDecimal(quantity));
    }
}
