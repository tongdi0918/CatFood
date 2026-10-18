package com.catshop.servlet;


import com.catshop.dao.OrderDAO;
import com.catshop.javabean.OrderBean;
import com.catshop.javabean.OrderItemBean;
import com.catshop.javabean.UserBean;

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
 * 支持 action: create / list / detail / pay / cancel1
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
        // 从 session 拿登录用户对象（与登录/购物车模块保持一致）
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");

        if (loginUser == null) {
            // 未登录，跳转到登录页
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<OrderBean> orders = orderDAO.findByUserId(loginUser.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }

    /**
     * 订单详情（校验订单归属：只能查看自己的订单）
     */
    private void orderDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = currentUserId(req);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        Integer orderId = parseOrderId(req);
        if (orderId == null) {
            resp.sendRedirect(req.getContextPath() + "/orders?action=list");
            return;
        }
        OrderBean order = orderDAO.findById(orderId);
        // 订单不存在或不属于当前用户，一律跳回列表（不泄露他人订单信息）
        if (order == null || order.getUserId() != userId) {
            resp.sendRedirect(req.getContextPath() + "/orders?action=list");
            return;
        }
        List<OrderItemBean> items = orderDAO.findItemsByOrderId(orderId);
        req.setAttribute("order", order);
        req.setAttribute("items", items);
        req.getRequestDispatcher("/order_detail.jsp").forward(req, resp);
    }

    /**
     * 模拟支付（校验订单归属）
     */
    private void payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!ownOrder(req)) {
            resp.sendRedirect(req.getContextPath() + "/orders?action=list");
            return;
        }
        int orderId = Integer.parseInt(req.getParameter("id").trim());
        orderDAO.updateStatus(orderId, 1); // 已付款
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }

    /**
     * 取消订单（校验订单归属）
     */
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!ownOrder(req)) {
            resp.sendRedirect(req.getContextPath() + "/orders?action=list");
            return;
        }
        int orderId = Integer.parseInt(req.getParameter("id").trim());
        orderDAO.updateStatus(orderId, 4); // 已取消
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }

    /** 解析并校验 id 参数，非法返回 null */
    private Integer parseOrderId(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** 当前登录用户 id，未登录返回 null */
    private Integer currentUserId(HttpServletRequest req) {
        UserBean loginUser = (UserBean) req.getSession().getAttribute("loginUser");
        return loginUser == null ? null : loginUser.getId();
    }

    /** 判断请求中的订单是否属于当前登录用户（防止越权操作他人订单） */
    private boolean ownOrder(HttpServletRequest req) {
        Integer userId = currentUserId(req);
        Integer orderId = parseOrderId(req);
        if (userId == null || orderId == null) {
            return false;
        }
        OrderBean order = orderDAO.findById(orderId);
        return order != null && order.getUserId() == userId;
    }
}
