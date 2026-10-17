package com.catshop.servlet;

import com.catshop.javabean.CartItem;
import com.catshop.javabean.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车 Servlet
 * 当前仅实现 Session 购物车的获取与初始化，以及默认的「查看购物车」操作。
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 防御性登录校验：未登录跳转登录页（LoginFilter 已拦截，此处双保险）
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 获取购物车：Session 中不存在则初始化空集合并保存，已存在则直接复用，不覆盖
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<CartItem>();
            session.setAttribute("cart", cart);
        }

        // 默认操作：查看购物车，放入 request 后转发到 cart.jsp
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}
