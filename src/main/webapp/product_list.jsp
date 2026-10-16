
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>猫粮商品列表</title>
</head>
<body>
<h2>喵鲜猫粮商城 - 商品列表</h2>

<%--搜索+分类表单--%>
<form action="ProductListServlet" method="get">
    分类：
    <select name="category">
        <option value="">全部</option>
        <option value="幼猫">幼猫</option>
        <option value="成猫">成猫</option>
        <option value="全期">全期</option>
    </select>
    搜索商品：<input type="text" name="keyword">
    <input type="submit" value="查询">
</form>

<hr>

<div>
    <c:forEach items="${productList}" var="pro">
        <div style="border:1px solid #ccc;width:220px;float:left;margin:10px;padding:10px;">
            <img src="${pro.image}" width="200" height="150">
            <p>商品名称：${pro.pname}</p>
            <p>价格：${pro.price}元</p>
            <p>分类：${pro.category}</p>
            <a href="ProductDetailServlet?pid=${pro.pid}">查看详情</a>
        </div>
    </c:forEach>
</div>
</body>
</html>
