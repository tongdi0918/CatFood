package com.catshop.servlet;

import com.catshop.dao.ProductDAO;
import com.catshop.javabean.CartItem;
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
 * 支持 action: view（默认）/ update（修改数量）
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

        // 修改数量（AJAX）
        String action = request.getParameter("action");
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

        // 默认操作：查看购物车，放入 request 后转发到 cart.jsp
        request.setAttribute("cart", cart);
        request.setAttribute("totalAmount", calcTotal(cart));
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
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
}
