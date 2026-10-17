<%--
  Created by IntelliJ IDEA.
  User: 38532
  Date: 2026/10/17
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录</title>
</head>
<body>
    <h2>用户登录</h2>
    <form action="UserLoginServlet" method="post">
        账号：<input type="text" name="username"><br><br>
        密码：<input type="password" name="password"><br><br>
        <input type="submit" value="登录">
    </form>
    <br>
    <a href="register.jsp">没有账号？点击注册</a>
</body>
</html>
