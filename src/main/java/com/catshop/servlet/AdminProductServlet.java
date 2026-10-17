package com.catshop.servlet;

import com.catshop.dao.ProductDAO;
import com.catshop.javabean.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AdminProductServlet")
public class AdminProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        if(action == null){
            action = "list";
        }
        switch (action){
            case "list":
                //后台查看全部商品
                List<Product> allProduct = productDAO.findAllAdmin();
                request.setAttribute("allProduct",allProduct);
                request.getRequestDispatcher("/admin/product_list.jsp").forward(request,response);
                break;
            case "delete":
                //删除商品
                Integer pidDel = Integer.parseInt(request.getParameter("pid"));
                productDAO.deleteProduct(pidDel);
                response.sendRedirect("AdminProductServlet?action=list");
                break;
            case "updateStatus":
                //切换上下架
                Integer pidSta = Integer.parseInt(request.getParameter("pid"));
                Integer status = Integer.parseInt(request.getParameter("status"));
                productDAO.updateStatus(pidSta,status);
                response.sendRedirect("AdminProductServlet?action=list");
                break;
            case "edit":
                //跳转编辑页面，回显数据
                Integer pidEdit = Integer.parseInt(request.getParameter("pid"));
                Product editProduct = productDAO.findById(pidEdit);
                request.setAttribute("editProduct",editProduct);
                request.getRequestDispatcher("/admin/product_edit.jsp").forward(request,response);
                break;
            case "add":
                //跳转新增表单页面
                request.getRequestDispatcher("/admin/product_edit.jsp").forward(request,response);
                break;
            default:
                response.sendRedirect("AdminProductServlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getParameter("action");
        if("saveAdd".equals(action)){
            //新增商品提交表单
            Product p = new Product();
            p.setPname(request.getParameter("pname"));
            p.setPrice(Double.parseDouble(request.getParameter("price")));
            p.setCategory(request.getParameter("category"));
            p.setStock(Integer.parseInt(request.getParameter("stock")));
            p.setImage(request.getParameter("image"));
            p.setDescription(request.getParameter("description"));
            p.setStatus(Integer.parseInt(request.getParameter("status")));
            productDAO.addProduct(p);
            response.sendRedirect("AdminProductServlet?action=list");
        }else if("saveEdit".equals(action)){
            //保存编辑修改
            Product p = new Product();
            p.setPid(Integer.parseInt(request.getParameter("pid")));
            p.setPname(request.getParameter("pname"));
            p.setPrice(Double.parseDouble(request.getParameter("price")));
            p.setCategory(request.getParameter("category"));
            p.setStock(Integer.parseInt(request.getParameter("stock")));
            p.setImage(request.getParameter("image"));
            p.setDescription(request.getParameter("description"));
            p.setStatus(Integer.parseInt(request.getParameter("status")));
            productDAO.updateProduct(p);
            response.sendRedirect("AdminProductServlet?action=list");
        }else{
            doGet(request,response);
        }
    }
}
