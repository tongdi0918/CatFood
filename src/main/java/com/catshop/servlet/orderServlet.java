package com.catshop.servlet;


import com.catshop.dao.OrderDAO;
import com.catshop.javabean.OrderBean;
import com.catshop.javabean.OrderItemBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 订单 Servlet
 * 支持 action: create / list / detail / pay / cancel
 */
@WebServlet("/orders")
public class orderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            //case "create":   createOrder(req, resp);   break;
            case "list":     listOrders(req, resp);    break;
            case "detail":   orderDetail(req, resp);   break;
            case "pay":      payOrder(req, resp);     break;
            case "cancel":   cancelOrder(req, resp);   break;
            default:         listOrders(req, resp);
        }
    }

    /**
     * 我的订单列表
     */
    private void listOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // 从session拿登录用户id，不再需要User对象
        Integer userId = (Integer) session.getAttribute("loginUserId");

        if (userId == null) {
            // 未登录，跳转到登录页
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<OrderBean> orders = orderDAO.findByUserId(userId);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }

    /**
     * 订单详情
     */
    private void orderDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("id"));
        List<OrderItemBean> items = orderDAO.findItemsByOrderId(orderId);
        req.setAttribute("items", items);
        // 注意：这里你后面需要新建 order_detail.jsp 详情页面
        req.getRequestDispatcher("/order_detail.jsp").forward(req, resp);
    }

    /**
     * 模拟支付
     */
    private void payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = Integer.parseInt(req.getParameter("id"));
        orderDAO.updateStatus(orderId, 1); // 已付款
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }

    /**
     * 取消订单
     */
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = Integer.parseInt(req.getParameter("id"));
        orderDAO.updateStatus(orderId, 4); // 已取消
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }
}
