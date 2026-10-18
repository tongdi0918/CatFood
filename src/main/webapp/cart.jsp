<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的购物车 - 喵鲜猫粮商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/index.jsp">首页</a>
        <a href="${pageContext.request.contextPath}/ProductListServlet">全部商品</a>
        <a href="${pageContext.request.contextPath}/cart">购物车</a>
        <a href="#">个人中心</a>
    </div>
</nav>

<header>
    <h1>我的购物车</h1>
</header>

<main>
    <section class="cart-container">
        <c:choose>
            <c:when test="${empty sessionScope.cart}">
                <div class="cart-empty">
                    <p>购物车还是空的</p>
                    <a href="${pageContext.request.contextPath}/ProductListServlet">去逛逛</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th><input type="checkbox" id="cart-select-all" checked></th>
                            <th>商品</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>小计</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${sessionScope.cart}" var="item">
                            <tr data-pid="${item.product.pid}" data-stock="${item.product.stock}" data-price="${item.product.price}">
                                <td><input type="checkbox" class="cart-item-check" checked></td>
                                <td>
                                    <img src="${pageContext.request.contextPath}${item.product.image}" alt="${item.product.pname}">
                                    <span>${item.product.pname}</span>
                                    <c:if test="${item.product.status != 1}">
                                        <span class="off-shelf-tag">已下架</span>
                                    </c:if>
                                    <c:if test="${item.quantity > item.product.stock}">
                                        <div class="stock-warning">库存不足，请调整数量</div>
                                    </c:if>
                                </td>
                                <td>¥ <fmt:formatNumber value="${item.product.price}" pattern="0.00"/></td>
                                <td class="cart-qty">
                                    <button type="button" class="qty-minus">-</button>
                                    <input type="text" class="qty-value" value="${item.quantity}" inputmode="numeric">
                                    <button type="button" class="qty-plus">+</button>
                                </td>
                                <td class="qty-subtotal">¥ <fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></td>
                                <td><button type="button" class="qty-remove">移除</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="cart-total">
                    <span>合计：</span>
                    <span id="cart-total">¥ <fmt:formatNumber value="${totalAmount}" pattern="0.00"/></span>
                </div>
                <div class="cart-actions">
                    <button type="button" id="cart-clear">清空购物车</button>
                </div>
                <div id="cart-feedback"></div>
                <c:if test="${not empty error}">
                    <div class="cart-error">${error}</div>
                </c:if>
                <div class="cart-checkout">
                    <form id="checkout-form" action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="action" value="submit">
                        <p>
                            <label>收货人：</label>
                            <input type="text" name="receiver" value="${receiver}" required>
                        </p>
                        <p>
                            <label>联系电话：</label>
                            <input type="text" name="phone" value="${phone}" maxlength="11" placeholder="请输入11位手机号" required>
                        </p>
                        <p>
                            <label>收货地址：</label>
                            <input type="text" name="address" value="${address}" required>
                        </p>
                        <button type="submit" id="checkout-submit">提交订单</button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<footer>
    <p>©2026 喵鲜猫粮商城 | 第三小组课程项目 版权所有</p>
</footer>

<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
