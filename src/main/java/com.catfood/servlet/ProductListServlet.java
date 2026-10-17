package com.catfood.servlet;

import com.catfood.dao.ProductDAO;
import com.catfood.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //1. 获取表单两个参数
        String category = request.getParameter("category");
        String keyword = request.getParameter("keyword");

//2. 调用组合查询
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.findByCondition(category,keyword);

//3. 传到页面
        //把分类和关键词传到request，用于页面表单回显
        request.setAttribute("category", category);
        request.setAttribute("keyword", keyword);
        //把list存到request域，传给jsp页面
        request.setAttribute("productList",productList);
        //转发到商品列表jsp页面
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
