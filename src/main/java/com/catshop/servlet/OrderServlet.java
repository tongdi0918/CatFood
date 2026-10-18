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

/** 订单 Servlet（前台用户）
 * 支持 action: list / detail / pay / cancel */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

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
            case "list":     listOrders(req, resp);    break;
            case "detail":   orderDetail(req, resp);   break;
            case "pay":      payOrder(req, resp);     break;
            case "cancel":   cancelOrder(req, resp);   break;
            default:         listOrders(req, resp);
        }
    }

    /**我的订单列表  */
    private void listOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // 从session拿登录用户id，不再需要User对象
        Integer userId = (Integer) session.getAttribute("loginUserId");

        if (userId == null) {
            // 未登录，跳转到登录页
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<OrderBean> orders = orderDAO.findByUserId(userId);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }

    /** 订单详情 */
    private void orderDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 安全解析 id，非法参数不再直接抛 500
        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单id非法");
            return;
        }

        OrderBean order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
            return;
        }

        // 归属校验：只能查看自己的订单（管理员可查看全部）
        HttpSession session = req.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"admin".equals(loginUser.getRole()) && order.getUserId() != loginUser.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权查看该订单");
            return;
        }

        List<OrderItemBean> items = orderDAO.findItemsByOrderId(orderId);
        order.setItems(items);
        req.setAttribute("order", order);
        req.setAttribute("items", items);
        req.getRequestDispatcher("/order_detail.jsp").forward(req, resp);
    }

    /** 模拟支付 */
    private void payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单id非法");
            return;
        }

        OrderBean order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
            return;
        }

        // 归属校验：只能支付自己的待付款订单
        HttpSession session = req.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (order.getUserId() != loginUser.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权操作该订单");
            return;
        }

        if (!orderDAO.pay(orderId)) {
            req.setAttribute("error", "订单状态已变化，无法支付（仅待付款订单可支付）");
            forwardListWithError(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }

    /** 取消订单（仅待付款订单，取消后自动恢复库存）  */
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单id非法");
            return;
        }

        OrderBean order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
            return;
        }

        // 归属校验：只能取消自己的订单
        HttpSession session = req.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (order.getUserId() != loginUser.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权操作该订单");
            return;
        }

        if (!orderDAO.cancel(orderId)) {
            req.setAttribute("error", "订单状态已变化，无法取消（仅待付款订单可取消）");
            forwardListWithError(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/orders?action=list");
    }

    /** 取消/支付失败时回到订单列表并显示提示 */
    private void forwardListWithError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId != null) {
            req.setAttribute("orders", orderDAO.findByUserId(userId));
        }
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }
}
