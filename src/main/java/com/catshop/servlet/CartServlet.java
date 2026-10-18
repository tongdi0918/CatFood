package com.catshop.servlet;

import com.catshop.dao.OrderDAO;
import com.catshop.dao.ProductDAO;
import com.catshop.javabean.CartItem;
import com.catshop.javabean.OrderBean;
import com.catshop.javabean.OrderItemBean;
import com.catshop.javabean.Product;
import com.catshop.javabean.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车 Servlet
 * 支持 action: view（默认）/ update / remove / clear / submit
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

        String action = request.getParameter("action");

        // 加入购物车（AJAX）
        if ("add".equals(action)) {
            addToCart(request, response, cart);
            return;
        }

        // 修改数量（AJAX）
        if ("update".equals(action)) {
            updateQuantity(request, response, cart);
            return;
        }

        // 移除商品（AJAX）
        if ("remove".equals(action)) {
            removeItem(request, response, cart);
            return;
        }

        // 清空购物车（AJAX）
        if ("clear".equals(action)) {
            clearCart(session, response);
            return;
        }

        // 提交订单（表单提交，成功后重定向）
        if ("submit".equals(action)) {
            submitOrder(request, response, cart, loginUser);
            return;
        }

        // 默认操作：查看购物车前刷新最新库存与上下架状态，放入 request 后转发到 cart.jsp
        refreshCartInfo(cart);
        request.setAttribute("cart", cart);
        request.setAttribute("totalAmount", calcTotal(cart));
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    /**
     * action=add：加入购物车
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response, List<CartItem> cart) throws IOException {
        String pidStr = request.getParameter("pid");
        if (pidStr == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数缺失\"}");
            return;
        }

        int pid;
        int quantity = 1; // 未传数量时默认为 1
        try {
            pid = Integer.parseInt(pidStr.trim());
            String qtyStr = request.getParameter("quantity");
            if (qtyStr != null) {
                quantity = Integer.parseInt(qtyStr.trim());
            }
        } catch (NumberFormatException e) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数非法\"}");
            return;
        }

        // 数量必须为正
        if (quantity < 1) {
            writeJson(response, "{\"code\":1,\"msg\":\"数量不能小于1\"}");
            return;
        }

        // 从数据库读取商品，不信任客户端提交的名称/价格/库存/图片
        Product product = new ProductDAO().findById(pid);
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            writeJson(response, "{\"code\":1,\"msg\":\"商品不存在或已下架\"}");
            return;
        }

        // 校验库存
        Integer stock = product.getStock();
        if (stock == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"商品库存异常\"}");
            return;
        }

        // 查找购物车中是否已有该商品
        CartItem existing = null;
        for (CartItem item : cart) {
            Integer itemPid = item.getProduct().getPid();
            if (itemPid != null && itemPid == pid) {
                existing = item;
                break;
            }
        }

        int existingQty = (existing == null) ? 0 : existing.getQuantity();
        int newQty = existingQty + quantity;
        if (newQty > stock) {
            writeJson(response, "{\"code\":1,\"msg\":\"库存不足，最多还能加入 " + (stock - existingQty) + " 件\"}");
            return;
        }

        if (existing != null) {
            existing.setQuantity(newQty); // 已有商品：累加数量
        } else {
            cart.add(new CartItem(product, quantity)); // 新商品：保存数据库查询到的商品对象
        }

        BigDecimal total = calcTotal(cart);
        String json = "{\"code\":0,\"msg\":\"ok\",\"total\":" + total.toPlainString() + "}";
        writeJson(response, json);
    }

    /**
     * action=update：修改购物车中某商品的数量
     */
    private void updateQuantity(HttpServletRequest request, HttpServletResponse response, List<CartItem> cart) throws IOException {
        String pidStr = request.getParameter("pid");
        String qtyStr = request.getParameter("quantity");
        if (pidStr == null || qtyStr == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数缺失\"}");
            return;
        }

        int pid;
        int quantity;
        try {
            pid = Integer.parseInt(pidStr.trim());
            quantity = Integer.parseInt(qtyStr.trim());
        } catch (NumberFormatException e) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数非法\"}");
            return;
        }

        // 数量必须 >= 1
        if (quantity < 1) {
            writeJson(response, "{\"code\":1,\"msg\":\"数量不能小于1\"}");
            return;
        }

        // 查询商品并校验存在、上架
        Product product = new ProductDAO().findById(pid);
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            writeJson(response, "{\"code\":1,\"msg\":\"商品不存在或已下架\"}");
            return;
        }

        // 校验库存
        Integer stock = product.getStock();
        if (stock == null || quantity > stock) {
            writeJson(response, "{\"code\":1,\"msg\":\"库存不足\"}");
            return;
        }

        // 在购物车中查找对应商品
        CartItem target = null;
        for (CartItem item : cart) {
            Integer itemPid = item.getProduct().getPid();
            if (itemPid != null && itemPid == pid) {
                target = item;
                break;
            }
        }
        if (target == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"商品不在购物车中\"}");
            return;
        }

        // 更新数量
        target.setQuantity(quantity);

        // 返回该商品小计与购物车总金额，供前端局部刷新
        BigDecimal subtotal = BigDecimal.ZERO;
        Double price = target.getProduct().getPrice();
        if (price != null) {
            subtotal = BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity));
        }
        BigDecimal total = calcTotal(cart);

        String json = "{\"code\":0,\"msg\":\"ok\",\"quantity\":" + quantity
                + ",\"subtotal\":" + subtotal.toPlainString()
                + ",\"total\":" + total.toPlainString() + "}";
        writeJson(response, json);
    }

    /**
     * action=remove：从购物车移除某商品
     */
    private void removeItem(HttpServletRequest request, HttpServletResponse response, List<CartItem> cart) throws IOException {
        String pidStr = request.getParameter("pid");
        if (pidStr == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数缺失\"}");
            return;
        }

        int pid;
        try {
            pid = Integer.parseInt(pidStr.trim());
        } catch (NumberFormatException e) {
            writeJson(response, "{\"code\":1,\"msg\":\"参数非法\"}");
            return;
        }

        // 在购物车中查找并移除
        CartItem target = null;
        for (CartItem item : cart) {
            Integer itemPid = item.getProduct().getPid();
            if (itemPid != null && itemPid == pid) {
                target = item;
                break;
            }
        }
        if (target == null) {
            writeJson(response, "{\"code\":1,\"msg\":\"商品不在购物车中\"}");
            return;
        }

        cart.remove(target);

        BigDecimal total = calcTotal(cart);
        String json = "{\"code\":0,\"msg\":\"ok\",\"total\":" + total.toPlainString() + "}";
        writeJson(response, json);
    }

    /**
     * action=clear：清空购物车
     */
    private void clearCart(HttpSession session, HttpServletResponse response) throws IOException {
        session.removeAttribute("cart");
        writeJson(response, "{\"code\":0,\"msg\":\"ok\",\"total\":0}");
    }

    /**
     * action=submit：结算下单，复用 OrderDAO.createOrder 的事务与扣库存逻辑
     */
    private void submitOrder(HttpServletRequest request, HttpServletResponse response, List<CartItem> cart, UserBean loginUser) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (cart.isEmpty()) {
            request.setAttribute("error", "购物车为空，无法结算");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        // 收货信息校验
        String receiver = trimToNull(request.getParameter("receiver"));
        String phone = trimToNull(request.getParameter("phone"));
        String address = trimToNull(request.getParameter("address"));
        if (receiver == null || phone == null || address == null) {
            echoForm(request, receiver, phone, address);
            request.setAttribute("error", "收货人、联系电话、收货地址不能为空");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        // 手机号基础格式校验（11 位数字）
        if (!isValidPhone(phone)) {
            echoForm(request, receiver, phone, address);
            request.setAttribute("error", "联系电话格式不正确，请输入 11 位手机号");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        // 会话级防重复下单（尽力而为；真正的幂等需数据库约束，超出购物车范围）
        if (session.getAttribute("orderSubmitting") != null) {
            echoForm(request, receiver, phone, address);
            request.setAttribute("error", "订单正在提交，请勿重复操作");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }
        session.setAttribute("orderSubmitting", Boolean.TRUE);
        try {
            // 重新从数据库校验商品并组装订单明细（不信任客户端价格）
            ProductDAO productDAO = new ProductDAO();
            List<OrderItemBean> items = new ArrayList<OrderItemBean>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem ci : cart) {
                Integer pid = ci.getProduct().getPid();
                if (pid == null) {
                    echoForm(request, receiver, phone, address);
                    request.setAttribute("error", "购物车数据异常");
                    request.getRequestDispatcher("/cart.jsp").forward(request, response);
                    return;
                }
                Product p = productDAO.findById(pid);
                if (p == null || p.getStatus() == null || p.getStatus() != 1) {
                    echoForm(request, receiver, phone, address);
                    request.setAttribute("error", "商品不存在或已下架，请返回购物车调整后重试");
                    request.getRequestDispatcher("/cart.jsp").forward(request, response);
                    return;
                }
                Double price = p.getPrice();
                Integer stock = p.getStock();
                if (price == null || stock == null) {
                    echoForm(request, receiver, phone, address);
                    request.setAttribute("error", "商品价格或库存异常");
                    request.getRequestDispatcher("/cart.jsp").forward(request, response);
                    return;
                }
                int quantity = ci.getQuantity();
                if (quantity < 1 || quantity > stock) {
                    echoForm(request, receiver, phone, address);
                    request.setAttribute("error", "商品「" + p.getPname() + "」库存不足");
                    request.getRequestDispatcher("/cart.jsp").forward(request, response);
                    return;
                }

                OrderItemBean item = new OrderItemBean();
                item.setProductId(pid);
                item.setProductName(p.getPname());
                item.setPrice(BigDecimal.valueOf(price));
                item.setQuantity(quantity);
                items.add(item);
                totalAmount = totalAmount.add(BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity)));
            }

            // 组装订单主表
            OrderBean order = new OrderBean();
            order.setOrderNo(OrderDAO.generateOrderNo());
            order.setUserId(loginUser.getId());
            order.setTotalAmount(totalAmount);
            order.setReceiver(receiver);
            order.setPhone(phone);
            order.setAddress(address);
            order.setStatus(0); // 0-待付款

            boolean ok = new OrderDAO().createOrder(order, items);
            if (ok) {
                session.removeAttribute("cart");
                response.sendRedirect(request.getContextPath() + "/order_success.jsp");
            } else {
                echoForm(request, receiver, phone, address);
                request.setAttribute("error", "下单失败：库存不足或系统异常，请重试");
                request.getRequestDispatcher("/cart.jsp").forward(request, response);
            }
        } finally {
            session.removeAttribute("orderSubmitting");
        }
    }

    /** 查看购物车时刷新每件商品的最新库存与上下架状态（快照数据可能过期） */
    private void refreshCartInfo(List<CartItem> cart) {
        ProductDAO productDAO = new ProductDAO();
        for (CartItem item : cart) {
            Product p = productDAO.findById(item.getProduct().getPid());
            if (p == null) {
                item.getProduct().setStatus(0); // 商品已不存在，标记为下架
                continue;
            }
            if (p.getStock() != null) {
                item.getProduct().setStock(p.getStock());
            }
            Integer st = p.getStatus();
            item.getProduct().setStatus(st == null ? 0 : st); // 1上架 0下架
        }
    }

    /** 计算购物车总金额（BigDecimal 保证精度，价格为空则跳过） */
    private BigDecimal calcTotal(List<CartItem> cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart) {
            Double price = item.getProduct().getPrice();
            if (price == null) {
                continue;
            }
            total = total.add(BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }

    /** 输出 JSON 响应 */
    private void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    /** 去掉首尾空白，空串返回 null */
    private String trimToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    /** 校验手机号是否为 11 位数字 */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** 结算表单回显 */
    private void echoForm(HttpServletRequest request, String receiver, String phone, String address) {
        request.setAttribute("receiver", receiver);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
    }
}
