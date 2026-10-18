<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.catshop.javabean.OrderBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>订单展示</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_list.css">
</head>
<body class="bg">
<h2 style="margin-bottom:20px;">我的订单</h2>
<%-- 操作失败提示（例如订单状态已变化） --%>
<%
    String error = (String) request.getAttribute("error");
    if (error != null && !error.isEmpty()) {
%>
<div style="background:#fdecea;color:#c0392b;border:1px solid #f5c6cb;border-radius:6px;padding:12px 16px;margin-bottom:16px;">
    <%= error %>
</div>
<% } %>
<%
    List<OrderBean> orders = (List<OrderBean>) request.getAttribute("orders");
    if (orders == null || orders.isEmpty()) {
%>
<div class="card" style="text-align:center;padding:60px;color:#999;">
    暂无订单，<a href="${pageContext.request.contextPath}/ProductListServlet">去选购吧~</a>
</div>
<% }else{ %>
    <% for (OrderBean order : orders) { %>
    <div class="card" style="margin-bottom:16px;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:19px;">
            <div>
                <strong>订单号：</strong><%= order.getOrderNo() %>
                <span style="color:#999;margin-left:10px;"><%= order.getCreateTime() %></span>
            </div>
            <span ><%= order.getStatusText() %></span>
        </div>
        <div style="border-bottom:31px solid #f5c6cb;padding-top:12px;display:flex;justify-content:space-between;align-items:center;">
            <div style="color:#666;">
                收货人：<%= order.getReceiver() %><br>
                联系方式：<%= order.getPhone() %><br>
                地址：<%= order.getAddress() %>
            </div>
            <div style="text-align:right;">
                <div style="font-size:18px;color:#ff6700;font-weight:bold;">共计<%= order.getTotalAmount() %>RMB</div>
                <div style="margin-top:8px;">
                    <a href="${pageContext.request.contextPath}/orders?action=detail&id=<%= order.getId() %>"
                       class="btn-default">详情</a>
                    <% if (order.getStatus() == 0) { %>
                        <a href="${pageContext.request.contextPath}/orders?action=pay&id=<%= order.getId() %>" class="btn-primary" onclick="return confirm('确认支付该订单？')">去支付</a>
                        <a href="${pageContext.request.contextPath}/orders?action=cancel&id=<%= order.getId() %>" class="btn-danger" onclick="return confirm('确认取消该订单？取消后将恢复商品库存。')">取消</a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    <% } %>
<% } %>

<p style="margin-top:20px;font-size: 21px"><a href="${pageContext.request.contextPath}/index.jsp">返回首页</a></p>

</body>
</html>
