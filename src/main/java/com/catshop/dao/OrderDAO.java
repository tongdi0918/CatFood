package com.catshop.dao;
import com.catshop.javabean.OrderBean;      //导入订单Bean类
import com.catshop.javabean.OrderItemBean;      //导入订单明细Bean类

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;      // 预编译SQL
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class OrderDAO {
    /** 创建订单（含订单明细，事务） */


    public boolean createOrder(OrderBean order, List<OrderItemBean> items) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);      //关闭自动提交，开启手动事务:多条 SQL 必须手动 commit，任意失败可以 rollback 回滚

            // 1. 插入订单主表
            String orderSql = "INSERT INTO orders (order_no, user_id, total_amount, receiver, phone, address, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);         //拿到主键id
            pstmt.setString(1, order.getOrderNo() );
            pstmt.setInt(2, order.getUserId() );
            pstmt.setBigDecimal(3, order.getTotalAmount() );
            pstmt.setString(4, order.getReceiver() );
            pstmt.setString(5, order.getPhone() );
            pstmt.setString(6, order.getAddress() );
            pstmt.setInt(7, order.getStatus() );
            pstmt.executeUpdate();

            // 拿取刚才插入订单  自动生成的主键 orderId
            ResultSet rs = pstmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

            // 2. 插入订单明细
            String itemSql = "INSERT INTO order_item (order_id, product_id, product_name, price, quantity) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(itemSql);
            for (OrderItemBean item : items) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, item.getProductId());
                pstmt.setString(3, item.getProductName());
                pstmt.setBigDecimal(4, item.getPrice());
                pstmt.setInt(5, item.getQuantity());
                pstmt.addBatch();       //所有 订单项addBatch批量添加
            }
            pstmt.executeBatch();       //批量执行所有明细插入
            pstmt.close();

            // 3. 扣减库存
            ProductDAO productDAO = new ProductDAO();
            for (OrderItemBean item : items) {
                int affected = productDAO.reduceStock(conn, item.getProductId(), item.getQuantity());
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
            }


            conn.commit();      //有步骤全部正常：提交事务，数据永久写入数据库
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);   //恢复自动提交
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** 查询用户的所有订单  */
    public List<OrderBean> findByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY create_time DESC";
        List<OrderBean> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));     //把数据库行转为 OrderBean 实体对象，放入 list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /** 查询单条订单（用于订单归属校验） */
    public OrderBean findById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    /** 查询所有订单（后台管理）  */
    public List<OrderBean> findAll() {
        //订单表o  左连接 用户表u。取出username，即使用户被删除，订单记录依然查得到
        String sql = "SELECT o.*, u.username FROM orders o LEFT JOIN user u ON o.user_id = u.id ORDER BY o.create_time DESC";
        List<OrderBean> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderBean order = mapOrder(rs);     //每行结果映射 Order实体
                order.setUsername(rs.getString("username"));    //把联查出来的username 赋值给 order实体的username
                list.add(order);        //Order实体存入集合
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /** 查询orderId=?的订单明细（联查商品图片，展示订单详情用） */
    public List<OrderItemBean> findItemsByOrderId(int orderId) {
        String sql = "SELECT i.*, p.image AS product_image FROM order_item i LEFT JOIN product p ON i.product_id = p.pid WHERE i.order_id = ?";
        List<OrderItemBean> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderItemBean item = new OrderItemBean();       //实例化一个OrderItem实体
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setQuantity(rs.getInt("quantity"));    //封装成 OrderItem实体
                item.setProductImage(rs.getString("product_image"));
                list.add(item);            //OrderItem实体存入集合
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;        //返回集合
    }

    /** 查询 orderId=? 的单个订单（后台详情、前台详情校验归属用） */
    public OrderBean findById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    /** 模拟支付：仅待付款(status=0)订单可支付，支付后记录支付时间 */
    public boolean pay(int orderId) {
        String sql = "UPDATE orders SET status = 1, pay_time = NOW() WHERE id = ? AND status = 0";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /** 取消订单：仅待付款(status=0)订单可取消，事务内恢复商品库存 */
    public boolean cancel(int orderId) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 先锁定该订单行，防止并发重复取消
            PreparedStatement lock = conn.prepareStatement("SELECT status FROM orders WHERE id = ? FOR UPDATE");
            lock.setInt(1, orderId);
            ResultSet rs = lock.executeQuery();
            if (!rs.next()) {
                rs.close(); lock.close();
                conn.rollback();
                return false;   // 订单不存在
            }
            int currentStatus = rs.getInt("status");
            rs.close();
            lock.close();
            if (currentStatus != 0) {
                conn.rollback();
                return false;   // 仅待付款订单可取消
            }

            // 2. 读取订单明细，恢复库存
            ProductDAO productDAO = new ProductDAO();
            List<OrderItemBean> items = findItemsByOrderIdInTx(conn, orderId);
            for (OrderItemBean item : items) {
                productDAO.increaseStock(conn, item.getProductId(), item.getQuantity());
            }

            // 3. 更新订单状态为已取消
            PreparedStatement upd = conn.prepareStatement("UPDATE orders SET status = 4 WHERE id = ? AND status = 0");
            upd.setInt(1, orderId);
            int rows = upd.executeUpdate();
            upd.close();
            if (rows == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** 在指定事务连接内查询订单明细（供 cancel 使用） */
    private List<OrderItemBean> findItemsByOrderIdInTx(Connection conn, int orderId) throws SQLException {
        List<OrderItemBean> list = new ArrayList<>();
        String sql = "SELECT i.*, p.image AS product_image FROM order_item i LEFT JOIN product p ON i.product_id = p.pid WHERE i.order_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            OrderItemBean item = new OrderItemBean();
            item.setId(rs.getInt("id"));
            item.setOrderId(rs.getInt("order_id"));
            item.setProductId(rs.getInt("product_id"));
            item.setProductName(rs.getString("product_name"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setQuantity(rs.getInt("quantity"));
            item.setProductImage(rs.getString("product_image"));
            list.add(item);
        }
        rs.close();
        pstmt.close();
        return list;
    }

    /** 更新订单状态（后台发货/完成等通用操作） */
    public boolean updateStatus(int orderId, int status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;       //>0更新成功；没找到订单返回false
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**  生成唯一订单号，"CF"+ 当前毫秒时间戳 + 4位随机数（降低并发下碰撞概率）  */
    public static String generateOrderNo() {
        return "CF" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }

    private OrderBean mapOrder(ResultSet rs) throws SQLException {  //把 ResultSet每行数据封装成 Order实体
        OrderBean o = new OrderBean();
        o.setId(rs.getInt("id"));
        o.setOrderNo(rs.getString("order_no"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setReceiver(rs.getString("receiver"));
        o.setPhone(rs.getString("phone"));
        o.setAddress(rs.getString("address"));
        o.setStatus(rs.getInt("status"));
        o.setCreateTime(rs.getTimestamp("create_time"));
        o.setPayTime(rs.getTimestamp("pay_time"));
        return o;       //返回Order实体
    }
}
