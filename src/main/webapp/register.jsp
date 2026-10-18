<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
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

        .btn-reg{
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

        .btn-reg:hover{
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

<div class="container">
    <h3>用户注册</h3>

    <form action="UserRegisterServlet" method="post">
        <p>
            用户名：
            <input type="text" name="username" placeholder="请输入用户名">
        </p>
        <p>
            密码：
            <input type="password" name="password" placeholder="请输入密码">
        </p>
        <p>
            <input type="submit" class="btn-reg" value="注册">
        </p>
    </form>

    <div class="bottom-links">
        <a href="login.jsp">返回登录</a>
    </div>
</div>

</body>
</html>