package com.catfood.servlet;

import com.catfood.dao.ProductDAO;
import com.catfood.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ProductDetailServlet")
public class ProductDetailServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String pidStr = request.getParameter("pid");
        Product product = null;
        if(pidStr != null){
            Integer pid = Integer.parseInt(pidStr);
            product = productDAO.findById(pid);
        }
        request.setAttribute("product",product);
        request.getRequestDispatcher("/product_detail.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
