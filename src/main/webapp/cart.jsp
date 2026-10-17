<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的购物车 - 喵鲜猫粮商城</title>
</head>
<body>

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
        <table class="cart-table">
            <thead>
                <tr>
                    <th>商品</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>小计</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <!-- 商品数据行将在下一阶段实现 -->
            </tbody>
        </table>
    </section>
</main>

<footer>
    <p>©2026 喵鲜猫粮商城 | 第三小组课程项目 版权所有</p>
</footer>

</body>
</html>
