<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情 - 喵鲜猫粮商城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_list.css">
</head>
<body class="bg">
<h2 style="margin-bottom:20px;">订单详情</h2>

<div class="card" style="margin-bottom:16px;padding:20px;">
    <p><strong>订单号：</strong>${order.orderNo}</p>
    <p><strong>收货人：</strong>${order.receiver}</p>
    <p><strong>联系电话：</strong>${order.phone}</p>
    <p><strong>收货地址：</strong>${order.address}</p>
    <p><strong>订单状态：</strong>${order.statusText}</p>
    <p><strong>订单金额：</strong>¥ <fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/></p>
</div>

<c:choose>
    <c:when test="${empty items}">
        <div class="card" style="text-align:center;padding:60px;color:#999;">该订单暂无明细</div>
    </c:when>
    <c:otherwise>
        <table class="card" style="width:100%;border-collapse:collapse;text-align:left;margin-bottom:16px;">
            <thead>
                <tr>
                    <th>商品</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>小计</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${items}" var="item">
                    <tr>
                        <td>${item.productName}</td>
                        <td>¥ <fmt:formatNumber value="${item.price}" pattern="0.00"/></td>
                        <td>${item.quantity}</td>
                        <td>¥ <fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<div style="margin-top:20px;">
    <a href="${pageContext.request.contextPath}/orders?action=list" class="btn-default">返回订单列表</a>
</div>
</body>
</html>
