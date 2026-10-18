<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台订单管理 - 喵鲜猫粮商城</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: "Microsoft Yahei", "PingFang SC", sans-serif;
            background: #f4f7fa;
            padding: 30px;
        }
        .container {
            width: 92%; margin: 0 auto; background: #fff;
            padding: 30px; border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.09);
        }
        h3 { font-size: 24px; margin-bottom: 8px; color: #333; }
        .top-links { margin-bottom: 20px; font-size: 14px; }
        .top-links a { color: #4080ff; text-decoration: none; margin-right: 16px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 10px 8px; text-align: center; font-size: 14px; }
        th { background: #4080ff; color: #fff; font-weight: normal; }
        tr:nth-child(even) { background: #f8f9fc; }
        a.op { text-decoration: none; margin: 0 4px; }
        .op-detail { color: #28a745; }
        .op-ship { color: #ff9900; }
        .op-complete { color: #4080ff; }
        .status-tag { display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; }
        .st0 { background: #fff3e0; color: #e65100; }
        .st1 { background: #e3f2fd; color: #1565c0; }
        .st2 { background: #ede7f6; color: #5e35b1; }
        .st3 { background: #e8f5e9; color: #2e7d32; }
        .st4 { background: #eceff1; color: #607d8b; }
        .error-box {
            background: #fdecea; color: #c0392b; border: 1px solid #f5c6cb;
            border-radius: 6px; padding: 12px 16px; margin-bottom: 16px;
        }
    </style>
</head>
<body>
<div class="container">
    <h3>管理员后台 - 订单管理</h3>
    <div class="top-links">
        <a href="${pageContext.request.contextPath}/AdminProductServlet?action=list">商品管理</a>
        <a href="${pageContext.request.contextPath}/adminUserList.jsp">用户管理</a>
        <a href="${pageContext.request.contextPath}/index.jsp">返回首页</a>
    </div>

    <c:if test="${not empty error}">
        <div class="error-box">${error}</div>
    </c:if>

    <table>
        <tr>
            <th>订单ID</th>
            <th>订单号</th>
            <th>用户</th>
            <th>金额</th>
            <th>收货人</th>
            <th>电话</th>
            <th>状态</th>
            <th>下单时间</th>
            <th>操作</th>
        </tr>
        <c:choose>
            <c:when test="${empty orders}">
                <tr><td colspan="9" style="padding:40px; color:#999;">暂无订单</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach items="${orders}" var="o">
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.orderNo}</td>
                        <td>${empty o.username ? '（用户已注销）' : o.username}</td>
                        <td>¥ <fmt:formatNumber value="${o.totalAmount}" pattern="0.00"/></td>
                        <td>${o.receiver}</td>
                        <td>${o.phone}</td>
                        <td><span class="status-tag st${o.status}">${o.statusText}</span></td>
                        <td><fmt:formatDate value="${o.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td>
                            <a class="op op-detail" href="${pageContext.request.contextPath}/AdminOrderServlet?action=detail&id=${o.id}">详情</a>
                            <c:if test="${o.status == 1}">
                                <a class="op op-ship" href="${pageContext.request.contextPath}/AdminOrderServlet?action=ship&id=${o.id}"
                                   onclick="return confirm('确认发货？')">发货</a>
                            </c:if>
                            <c:if test="${o.status == 2}">
                                <a class="op op-complete" href="${pageContext.request.contextPath}/AdminOrderServlet?action=complete&id=${o.id}"
                                   onclick="return confirm('确认标记为已完成？')">完成</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</div>
</body>
</html>
