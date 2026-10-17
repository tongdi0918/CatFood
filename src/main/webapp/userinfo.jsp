<%--
  Created by IntelliJ IDEA.
  User: 38532
  Date: 2026/10/17
  Time: 22:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>个人中心</title>
    <style>
        .box{width:420px;margin:40px auto;padding:25px;border:1px solid #ddd;border-radius:10px;}
    </style>
</head>
<body>
<div style="text-align:center;margin-top:20px;">
    <a href="index.jsp">首页</a> | <a href="UserLogoutServlet">退出登录</a>
</div>
<div class="box">
    <h3 align="center">个人信息</h3>
    <p>账号：${user.username}</p>
    <p>姓名：${user.name}</p>
    <p>手机号：${user.phone}</p>
    <p>身份：${user.role == 'admin' ? '管理员' : '普通用户'}</p>
    <br>
    <a href="updateUserInfo.jsp">修改个人资料</a>
</div>
</body>
</html>