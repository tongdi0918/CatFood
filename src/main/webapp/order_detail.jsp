<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单详情_喵鲜猫粮商城</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: "Microsoft Yahei", "PingFang SC", sans-serif;
            background: #f7f9fc;
            color: #333;
        }
        .container { max-width: 900px; margin: 30px auto; padding: 0 20px; }
        h2 { margin-bottom: 20px; color: #ff6a3d; }
        .card {
            background: #fff;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.06);
            margin-bottom: 20px;
        }
        .card h3 { margin-bottom: 14px; color: #444; }
        .info-row { margin: 6px 0; font-size: 14px; color: #555; }
        .info-row b { color: #333; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #e6e6e6; padding: 10px 12px; text-align: center; font-size: 14px; }
        th { background: #fff3e8; color: #333; }
        td img { width: 48px; height: 48px; object-fit: cover; border-radius: 6px; vertical-align: middle; }
        .total-row { text-align: right; font-size: 16px; font-weight: bold; color: #ff6a3d; padding-top: 16px; }
        .status-tag {
            display: inline-block; padding: 3px 12px; border-radius: 12px;
            background: #e8f5e9; color: #2e7d32; font-size: 13px;
        }
        .back-link {
            display: inline-block; margin-top: 10px; color: #ff6a3d; text-decoration: none;
        }
        .back-link:hover { text-decoration: underline; }
        .actions { margin-top: 16px; }
        .btn {
            display: inline-block; padding: 8px 20px; border-radius: 8px;
            text-decoration: none; font-size: 14px; margin-right: 10px;
        }
        .btn-primary { background: #ff6a3d; color: #fff; }
        .btn-default { background: #eee; color: #555; }
    </style>
</head>
<body>
<div class="container">
    <h2>订单详情</h2>

    <c:if test="${empty order}">
        <div class="card" style="text-align:center; padding:50px; color:#999;">订单不存在。</div>
        <p><a class="back-link" href="${pageContext.request.contextPath}/orders?action=list">返回我的订单</a></p>
    </c:if>

    <c:if test="${not empty order}">
        <div class="card">
            <h3>订单信息</h3>
            <div class="info-row"><b>订单号：</b>${order.orderNo}</div>
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
            <h3>商品明细</h3>
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

        <div class="actions">
            <c:if test="${order.status == 0}">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/orders?action=pay&id=${order.id}"
                   onclick="return confirm('确认支付该订单？')">去支付</a>
                <a class="btn btn-default" href="${pageContext.request.contextPath}/orders?action=cancel&id=${order.id}"
                   onclick="return confirm('确认取消该订单？取消后将恢复商品库存。')">取消订单</a>
            </c:if>
            <a class="btn btn-default" href="${pageContext.request.contextPath}/orders?action=list">返回我的订单</a>
        </div>
    </c:if>
</div>
</body>
</html>
