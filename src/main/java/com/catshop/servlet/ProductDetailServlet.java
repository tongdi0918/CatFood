package com.catshop.servlet;

import com.catshop.javabean.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/ProductDetailServlet")
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取地址栏参数 pid
        String pidStr = request.getParameter("pid");

        // =========参数校验：解决Cannot parse null string报错=========
        if(pidStr == null || pidStr.trim().isEmpty()){
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("错误：缺少商品pid参数，请从商品列表点击商品进入详情！");
            return;
        }
        int pid;
        try{
            pid = Integer.parseInt(pidStr);
        }catch (NumberFormatException e){
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("错误：pid必须是数字！");
            return;
        }
        // ======================================================

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Product product = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 【重点！这里改成你真实数据库名、账号、密码】
            String url = "jdbc:mysql://localhost:3306/catshop?useSSL=false&serverTimezone=UTC";
            conn = DriverManager.getConnection(url,"root","mysql8");

            String sql = "select pid,pname,price,category,stock,image,description,status from product where pid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pid);
            rs = pstmt.executeQuery();
            if(rs.next()){
                product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{if(rs!=null) rs.close();}catch (Exception e){}
            try{if(pstmt!=null) pstmt.close();}catch (Exception e){}
            try{if(conn!=null) conn.close();}catch (Exception e){}
        }

        //把商品对象传到页面
        request.setAttribute("product",product);
        request.getRequestDispatcher("/product_detail.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}