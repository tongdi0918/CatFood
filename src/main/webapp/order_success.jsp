<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>下单成功_喵鲜猫粮商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_list.css">
</head>
<body>

<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="top-nav">
        <a href="${pageContext.request.contextPath}/index.jsp">首页</a>
        <a href="${pageContext.request.contextPath}/ProductListServlet">全部商品</a>
        <a href="${pageContext.request.contextPath}/cart">购物车</a>
        <a href="${pageContext.request.contextPath}/userinfo.jsp">个人中心</a>
    </div>
</nav>

<header>
    <h1>下单成功</h1>
</header>

<main>
    <c:if test="${not empty sessionScope.lastOrderNo}">
        <div class="order-summary">
            <p>订单号：<strong>${sessionScope.lastOrderNo}</strong></p>
            <p>订单金额：<strong><fmt:formatNumber value="${sessionScope.lastOrderAmount}" pattern="0.00"/>RMB</strong></p>
        </div>
    </c:if>
    <p>您的订单已提交成功，感谢购买！</p>
    <p style="margin-top:14px;">
        <a href="${pageContext.request.contextPath}/orders?action=list" style="margin-right:16px;">查看我的订单</a>
        <a href="${pageContext.request.contextPath}/ProductListServlet">继续购物</a>
    </p>
</main>

<footer>
    <p>&copy; 202610喵鲜猫粮商城of第三小组课程项目</p>
</footer>

</body>
</html>
