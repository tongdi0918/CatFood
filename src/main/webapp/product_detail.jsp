<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="product" type="com.catshop.javabean.Product"--%>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品详情_喵鲜猫粮商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/productDetail.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="nav-links">
        <a href="index.jsp">首页</a>
        <a href="ProductListServlet">全部商品</a>
        <a href="cart">购物车</a>
        <a href="userinfo.jsp">个人中心</a>
    </div>
</nav>

<c:if test="${empty product}">
    <section class="detail-container">
        <p style="text-align:center; padding:60px 0; font-size:18px; color:#777;">商品不存在或已删除，请返回商品列表重新选择。</p>
        <p style="text-align:center;">
            <a href="ProductListServlet" class="back-link">返回商品列表</a>
        </p>
    </section>
</c:if>

<c:if test="${not empty product}">
<section class="detail-container">
    <div class="detail-img">
        <img src="${pageContext.request.contextPath}${product.image}" alt="${product.pname}">
    </div>

    <div class="detail-info">
        <h1>${product.pname}</h1>

        <div class="detail-price">
            ¥ ${product.price}
        </div>

        <div class="detail-meta">
            <p>分类：${product.category}</p>
            <p>库存：${product.stock} 件</p>
        </div>

        <div class="detail-description">
            <strong>商品描述</strong>
            <p>${product.description}</p>
        </div>

        <div class="detail-buttons">
            <a href="javascript:void(0)" class="btn-primary" id="btn-add-cart" data-pid="${product.pid}">加入购物车</a>
            <a href="javascript:void(0)" class="btn-secondary" id="btn-buy-now" data-pid="${product.pid}">立即购买</a>
        </div>

        <a href="ProductListServlet" class="back-link">返回商品列表</a>
    </div>
</section>
</c:if>

<footer>
    <p>&copy; 202610喵鲜猫粮商城of第三小组课程项目</p>
</footer>

<script src="${pageContext.request.contextPath}/js/cart.js"></script>
<script>
    // 商品详情页：加入购物车 / 立即购买（AJAX 加购，成功后立即购买跳转购物车结算）
    (function () {
        var addBtn = document.getElementById('btn-add-cart');
        if (addBtn) {
            addBtn.addEventListener('click', function () {
                window.addToCart(addBtn.getAttribute('data-pid'), 1);
            });
        }
        var buyBtn = document.getElementById('btn-buy-now');
        if (buyBtn) {
            buyBtn.addEventListener('click', function () {
                window.addToCart(buyBtn.getAttribute('data-pid'), 1, function () {
                    window.location.href = 'cart';
                });
            });
        }
    })();
</script>

</body>
</html>
