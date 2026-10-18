<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台订单详情 - 喵鲜猫粮商城</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: "Microsoft Yahei", "PingFang SC", sans-serif;
            background: #f4f7fa;
            padding: 30px;
        }
        .container {
            width: 86%; margin: 0 auto; background: #fff;
            padding: 30px; border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.09);
        }
        h3 { font-size: 22px; margin-bottom: 18px; color: #333; }
        .card { background: #fafbfc; border: 1px solid #eef0f3; border-radius: 8px; padding: 16px 20px; margin-bottom: 18px; }
        .info-row { margin: 6px 0; font-size: 14px; color: #555; }
        .info-row b { color: #333; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 10px 12px; text-align: center; font-size: 14px; }
        th { background: #4080ff; color: #fff; font-weight: normal; }
        td img { width: 48px; height: 48px; object-fit: cover; border-radius: 6px; vertical-align: middle; }
        .total-row { text-align: right; font-size: 16px; font-weight: bold; color: #e65100; padding-top: 16px; }
        .status-tag { display: inline-block; padding: 3px 12px; border-radius: 12px; background: #e8f5e9; color: #2e7d32; font-size: 13px; }
        .back-link { display: inline-block; margin-top: 10px; color: #4080ff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h3>后台订单详情</h3>

    <c:if test="${empty order}">
        <p style="color:#999; padding:30px;">订单不存在。</p>
        <p><a class="back-link" href="${pageContext.request.contextPath}/AdminOrderServlet?action=list">返回订单列表</a></p>
    </c:if>

    <c:if test="${not empty order}">
        <div class="card">
            <div class="info-row"><b>订单号：</b>${order.orderNo}</div>
            <div class="info-row"><b>下单用户：</b>${empty order.username ? '（用户已注销）' : order.username}</div>
            <div class="info-row"><b>下单时间：</b><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
            <c:if test="${not empty order.payTime}">
                <div class="info-row"><b>支付时间：</b><fmt:formatDate value="${order.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
            </c:if>
            <div class="info-row"><b>订单状态：</b><span class="status-tag">${order.statusText}</span></div>
            <div class="info-row"><b>收货人：</b>${order.receiver}</div>
            <div class="info-row"><b>联系电话：</b>${order.phone}</div>
            <div class="info-row"><b>收货地址：</b>${order.address}</div>
        </div>

        <div class="card">
            <table>
                <tr>
                    <th>商品图片</th>
                    <th>商品名称</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>小计</th>
                </tr>
                <c:forEach items="${items}" var="item">
                    <tr>
                        <td><img src="${pageContext.request.contextPath}${empty item.productImage ? '/images/kitten_milk_cake.jpg' : item.productImage}" alt="${item.productName}"></td>
                        <td>${item.productName}</td>
                        <td>¥ <fmt:formatNumber value="${item.price}" pattern="0.00"/></td>
                        <td>${item.quantity}</td>
                        <td>¥ <fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></td>
                    </tr>
                </c:forEach>
            </table>
            <div class="total-row">
                订单总额：¥ <fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/>
            </div>
        </div>

        <p>
            <a class="back-link" href="${pageContext.request.contextPath}/AdminOrderServlet?action=list">返回订单列表</a>
        </p>
    </c:if>
</div>
</body>
</html>
