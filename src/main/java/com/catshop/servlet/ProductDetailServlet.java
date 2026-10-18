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


        Product product = new ProductDAO().findById(pid);

        // 查无此商品：转发回商品列表并提示，而不是渲染空详情页
        if (product == null) {
            request.setAttribute("msg", "商品不存在或已删除，请重新选择商品");
            request.getRequestDispatcher("/ProductListServlet").forward(request, response);
            return;
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
