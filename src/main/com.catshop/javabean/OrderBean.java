
/*订单表
id,orderNo,userId,totalAmount,receiver,phone,address,status,createTime,payTime
 */

//package com.catfood.model;

import java.math.BigDecimal;    //高精度小数,避免精度丢失
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 */
public class Order {
    private int id;     //主键 id
    private String orderNo;     //业务唯一单号
    private int userId;     //下单用户 id
    private BigDecimal totalAmount;     //金额
    private String receiver;        //收货人姓名
    private String phone;
    private String address;
    private int status;   // 0-待付款，1-已付款，2-已发货，3-已完成，4-已取消
    private Date createTime;
    private Date payTime;

    // 关联查询字段
    private String username;    //把联查出来的 username 赋值给 order实体
    private List<OrderItem> items;      //订单集合

    public Order() {}   //无参构造

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getStatusText() {
        switch (status) {
            case 0: return "待付款";
            case 1: return "已付款";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }
}
