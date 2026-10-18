package com.catshop.servlet;

import com.catshop.dao.ProductDAO;
import com.catshop.javabean.Product;

import com.catshop.javabean.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        HttpSession session = request.getSession();
        UserBean u = (UserBean) session.getAttribute("loginUser");
        if (u == null || !"admin".equals(u.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

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
                if (!isPositiveInt(request.getParameter("pid"))) {
                    response.getWriter().write("商品ID格式错误！");
                    return;
                }
                Integer pidDel = Integer.parseInt(request.getParameter("pid"));
                productDAO.deleteProduct(pidDel);
                response.sendRedirect("AdminProductServlet?action=list");
                break;
            case "updateStatus":
                //切换上下架
                if (!isPositiveInt(request.getParameter("pid")) || !isStatusValid(request.getParameter("status"))) {
                    response.getWriter().write("参数格式错误！");
                    return;
                }
                Integer pidSta = Integer.parseInt(request.getParameter("pid"));
                Integer status = Integer.parseInt(request.getParameter("status"));
                productDAO.updateStatus(pidSta,status);
                response.sendRedirect("AdminProductServlet?action=list");
                break;
            case "edit":
                //跳转编辑页面，回显数据
                if (!isPositiveInt(request.getParameter("pid"))) {
                    response.getWriter().write("商品ID格式错误！");
                    return;
                }
                Integer pidEdit = Integer.parseInt(request.getParameter("pid"));
                Product editProduct = productDAO.findById(pidEdit);
                if (editProduct == null) {
                    response.getWriter().write("商品不存在！");
                    return;
                }
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
        response.setCharacterEncoding("UTF-8");   // getWriter 输出中文必须显式 UTF-8，否则按 ISO-8859-1 乱码
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        UserBean u = (UserBean) session.getAttribute("loginUser");
        if (u == null || !"admin".equals(u.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        if("saveAdd".equals(action)){
            //新增商品提交表单
            Product p = new Product();
            p.setPname(request.getParameter("pname"));
            p.setCategory(request.getParameter("category"));
            p.setImage(request.getParameter("image"));
            p.setDescription(request.getParameter("description"));
            // 数字字段安全解析：非法/为空时给出提示，不再抛 500
            try {
                p.setPrice(Double.parseDouble(request.getParameter("price")));
                p.setStock(Integer.parseInt(request.getParameter("stock")));
                p.setStatus(Integer.parseInt(request.getParameter("status")));
            } catch (NumberFormatException | NullPointerException e) {
                response.getWriter().write("参数错误：价格、库存、状态必须是有效数字！");
                return;
            }
            if (p.getPname() == null || p.getPname().trim().isEmpty()) {
                response.getWriter().write("参数错误：商品名称不能为空！");
                return;
            }
            productDAO.addProduct(p);
            response.sendRedirect("AdminProductServlet?action=list");
        }else if("saveEdit".equals(action)){
            //保存编辑修改
            Product p = new Product();
            p.setCategory(request.getParameter("category"));
            p.setImage(request.getParameter("image"));
            p.setDescription(request.getParameter("description"));
            try {
                p.setPid(Integer.parseInt(request.getParameter("pid")));
                p.setPrice(Double.parseDouble(request.getParameter("price")));
                p.setStock(Integer.parseInt(request.getParameter("stock")));
                p.setStatus(Integer.parseInt(request.getParameter("status")));
            } catch (NumberFormatException | NullPointerException e) {
                response.getWriter().write("参数错误：价格、库存、状态必须是有效数字！");
                return;
            }
            p.setPname(request.getParameter("pname"));
            if (p.getPname() == null || p.getPname().trim().isEmpty()) {
                response.getWriter().write("参数错误：商品名称不能为空！");
                return;
            }
            productDAO.updateProduct(p);
            response.sendRedirect("AdminProductServlet?action=list");
        }else{
            doGet(request,response);
        }
    }

    /** 校验字符串是否为合法正整数（防止 Integer.parseInt 抛异常导致 500） */
    private boolean isPositiveInt(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** 校验上下架状态是否为 0 或 1 */
    private boolean isStatusValid(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        try {
            int v = Integer.parseInt(s.trim());
            return v == 0 || v == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
