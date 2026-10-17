<%--
  Created by IntelliJ IDEA.
  User: 38532
  Date: 2026/10/17
  Time: 22:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>修改个人资料</title>
    <style>
        .box{width:420px;margin:40px auto;padding:25px;border:1px solid #ddd;border-radius:10px;}
    </style>
</head>
<body>
<div style="text-align:center;margin-top:20px;">
    <a href="UserInfoServlet">返回个人中心</a>
</div>
<div class="box">
    <h3 align="center">修改个人资料</h3>
    <form action="UpdateUserInfoServlet" method="post">
        <p>账号：${user.username}</p>
        <p>姓名：<input type="text" name="name" value="${user.name}"></p>
        <p>手机号：<input type="text" name="phone" value="${user.phone}"></p>
        <p align="center"><input type="submit" value="保存修改"></p>
    </form>
</div>
</body>
</html>
