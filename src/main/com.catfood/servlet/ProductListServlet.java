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

        //获取页面传过来参数
        String category = request.getParameter("category");
        String keyword = request.getParameter("keyword");

        List<Product> productList;

        if(keyword != null && !keyword.trim().equals("")){
            //执行搜索
            productList = productDAO.searchProduct(keyword.trim());
        }else if(category != null && !category.trim().equals("")){
            //按分类筛选
            productList = productDAO.findByCategory(category.trim());
        }else{
            //全部上架商品
            productList = productDAO.findAllOnSale();
        }

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
