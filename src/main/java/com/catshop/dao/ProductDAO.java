package com.catshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAO {
    // 扣减库存，使用外部传入的conn（共享事务连接！）
    public int reduceStock(Connection conn, int productId, int quantity) throws SQLException{
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ? AND stock >= ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, quantity);
        pstmt.setInt(2, productId);
        pstmt.setInt(3, quantity);
        int rows = pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
}
