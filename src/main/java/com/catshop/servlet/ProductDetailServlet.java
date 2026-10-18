package com.catshop.servlet;

import com.catshop.dao.ProductDAO;
import com.catshop.javabean.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ProductDetailServlet")
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取地址栏参数 pid
        String pidStr = request.getParameter("pid");

        // 参数校验：避免 pid 为空或非数字导致报错
        if (pidStr == null || pidStr.trim().isEmpty()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("错误：缺少商品pid参数，请从商品列表点击商品进入详情！");
            return;
        }
        int pid;
        try {
            pid = Integer.parseInt(pidStr.trim());
        } catch (NumberFormatException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("错误：pid必须是数字！");
            return;
        }

        // 通过 DAO 查询商品（统一使用 DBUtil 的连接配置，避免硬编码数据库账号密码）
        Product product = new ProductDAO().findById(pid);
        if (product == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("错误：商品不存在或已下架！");
            return;
        }

        // 把商品对象传到页面
        request.setAttribute("product", product);
        request.getRequestDispatcher("/product_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
