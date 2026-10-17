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
    <title>用户注册</title>
</head>
<body>
    <h2>用户注册</h2>
    <form action="UserRegisterServlet" method="post">
        账号：<input type="text" name="username"><br><br>
        密码：<input type="password" name="password"><br><br>
        <input type="submit" value="注册">
    </form>
    <br>
    <a href="login.jsp">已有账号？返回登录</a>
</body>
</html>
