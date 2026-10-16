<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>后台商品管理</title>
</head>
<body>
<h2>管理员后台 - 商品管理</h2>
<a href="AdminProductServlet?action=add">【新增商品】</a>
<hr>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>商品名称</th>
        <th>价格</th>
        <th>分类</th>
        <th>库存</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${allProduct}" var="p">
        <tr>
            <td>${p.pid}</td>
            <td>${p.pname}</td>
            <td>${p.price}</td>
            <td>${p.category}</td>
            <td>${p.stock}</td>
            <td>${p.status==1?"上架":"下架"}</td>
            <td>
                <a href="AdminProductServlet?action=edit&pid=${p.pid}">编辑</a>
                <a href="AdminProductServlet?action=delete&pid=${p.pid}" onclick="return confirm('确认删除该商品？')">删除</a>
                <c:if test="${p.status==1}">
                    <a href="AdminProductServlet?action=updateStatus&pid=${p.pid}&status=0">下架</a>
                </c:if>
                <c:if test="${p.status==0}">
                    <a href="AdminProductServlet?action=updateStatus&pid=${p.pid}&status=1">上架</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
