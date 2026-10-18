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

/**后台订单管理
 * 支持 action: list / detail / ship（发货）/ complete（完成）
 * 仅管理员可访问（权限校验在 Servlet 内完成） */
@WebServlet("/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        // 管理员权限校验
        HttpSession session = request.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                List<OrderBean> orders = orderDAO.findAll();
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/admin/order_list.jsp").forward(request, response);
                break;
            case "detail":
                orderDetail(request, response);
                break;
            case "ship":
                changeStatus(request, response, 1, 2, "仅已付款订单可以发货");
                break;
            case "complete":
                changeStatus(request, response, 2, 3, "仅已发货订单可以标记完成");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/AdminOrderServlet?action=list");
        }
    }

    /** 订单详情：订单主信息 + 明细 */
    private void orderDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单id非法");
            return;
        }

        OrderBean order = orderDAO.findById(orderId);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
            return;
        }
        List<OrderItemBean> items = orderDAO.findItemsByOrderId(orderId);
        order.setItems(items);
        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/admin/order_detail.jsp").forward(request, response);
    }

    /** 状态流转：仅允许 fromStatus -> toStatus，避免非法跳变 */
    private void changeStatus(HttpServletRequest request, HttpServletResponse response,
                              int fromStatus, int toStatus, String tip) throws ServletException, IOException {
        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单id非法");
            return;
        }

        OrderBean order = orderDAO.findById(orderId);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
            return;
        }
        if (order.getStatus() != fromStatus || !orderDAO.updateStatus(orderId, toStatus)) {
            request.setAttribute("error", tip);
            request.setAttribute("orders", orderDAO.findAll());
            request.getRequestDispatcher("/admin/order_list.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/AdminOrderServlet?action=list");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
