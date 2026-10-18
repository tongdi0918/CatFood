<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>个人中心</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family:"Microsoft Yahei",sans-serif;
        }

        body{
            /* 渐变背景，不再单调纯白 */
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
            /* 加深阴影，立体感更强 */
            box-shadow: 0 8px 24px rgba(0,0,0,0.12);
        }

        h3{
            text-align:center;
            margin-bottom:32px;
            color:#ff7b00;
            font-size:26px;
            font-weight:bold;
        }
        .info-item{
            font-size:17px;
            margin:16px 0;
            line-height:1.6;
            padding-left:8px;
        }
        .icon{
            color:#ff7b00;
            margin-right:8px;
        }
        .btn{
            display:block;
            text-align:center;
            width:100%;
            padding:13px;
            margin-top:14px;
            text-decoration:none;
            background:#ff7b00;
            color:white;
            border-radius:10px;
            font-size:16px;
            transition: all 0.3s;
        }
        .btn:hover{
            background:#e66a00;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
<div class="container">
    <h3>个人信息</h3>
    <div class="info-item"><span class="icon">👤</span>账号：${user.username}</div>
    <div class="info-item"><span class="icon">📝</span>姓名：${user.name}</div>
    <div class="info-item"><span class="icon">📞</span>手机号：${user.phone}</div>
    <div class="info-item"><span class="icon">🔖</span>身份：${user.role == 'admin' ? '管理员' : '普通用户'}</div>
    <br>
    <a href="ToUpdateServlet" class="btn">修改个人资料</a>
    <a href="index.jsp" class="btn">首页</a>
    <a href="UserLogoutServlet" class="btn">退出登录</a>
</div>
</body>
</html>