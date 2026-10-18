<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品详情 - 喵鲜猫粮商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/productDetail.css">
</head>
<body>

<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="nav-links">
        <a href="index.jsp">首页</a>
        <a href="ProductListServlet">全部商品</a>
        <a href="#">购物车</a>
        <a href="#">个人中心</a>
    </div>
</nav>

<section class="detail-container">
    <div class="detail-img">
        <img src="${pageContext.request.contextPath}/${product.image}" alt="${product.pname}">
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
            <a href="#" class="btn-primary">加入购物车</a>
            <a href="#" class="btn-secondary">立即购买</a>
        </div>

        <a href="ProductListServlet" class="back-link">返回商品列表</a>
    </div>
</section>

<footer>
    <p>&copy;202610喵鲜猫粮商城 | 第三小组课程项目 版权所有</p>
</footer>

</body>
</html>