<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family:"Microsoft Yahei",sans-serif;
        }

        body{
            background: linear-gradient(135deg,#e8f0fe,#f4f7fa);
            height:100vh;
            display:flex;
            justify-content:center;
            align-items:center;
        }

        .container{
            width:520px;
            background:#ffffff;
            padding:45px;
            border-radius:16px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.12);
        }

        h3{
            text-align:center;
            margin-bottom:32px;
            color:#ff7b00;
            font-size:26px;
            font-weight:bold;
        }

        p{
            margin:18px 0;
            font-size:17px;
        }

        input[type="text"],
        input[type="password"]{
            width:100%;
            padding:13px 15px;
            border:1px solid #ddd;
            border-radius:10px;
            margin-top:8px;
            font-size:16px;
            transition:0.2s;
        }
        input:focus{
            outline:none;
            border:1px solid #ff7b00;
        }

        .btn-login{
            width:100%;
            padding:13px;
            border:none;
            background:#ff7b00;
            color:white;
            border-radius:10px;
            cursor:pointer;
            font-size:16px;
            margin-top:16px;
            transition: all 0.3s;
        }

        .btn-login:hover{
            background:#e66a00;
            transform: translateY(-2px);
        }

        .bottom-links{
            text-align:center;
            margin-top:24px;
            font-size:15px;
        }

        .bottom-links a{
            color:#ff7b00;
            text-decoration:none;
            margin:0 10px;
        }

        .bottom-links a:hover{
            text-decoration:underline;
        }
    </style>
</head>
<body>
<div >
    <%
        String msg=request.getParameter("registermsg");
        if("success".equals(msg) ){
    %>
    <script>alert("注册成功，请登录");</script>
    <%
        }
    %>

    <%
        String loginmsg=request.getParameter("loginmsg");
        if("loginfail".equals(loginmsg) ){
    %>
    <script>alert("登录失败，请重新来过~");</script>
    <%
        }
    %>
</div>
<div class="container">
    <h3>用户登录</h3>
    <form action="UserLoginServlet" method="post">
        <p>
            账号：
            <input type="text" name="username" placeholder="请输入账号">
        </p>
        <p>
            密码：
            <input type="password" name="password" placeholder="请输入密码">
        </p>
        <p>
            <input type="submit" class="btn-login" value="登录">
        </p>
    </form>

    <div class="bottom-links">
        <a href="register.jsp">注册账号</a>
    </div>
</div>

</body>
</html>