<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.javabean.UserBean" %>
<html>
<head>
    <title>修改个人资料</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family:"Microsoft Yahei",sans-serif;
        }
        body{
            background: linear-gradient(135deg,#e8f0fe,#f4f7fa);
            height: 100vh;
            display:flex;
            justify-content:center;
            align-items:center;
        }
        .container{
            width:520px;
            background:#ffffff;
            padding:35px;
            border-radius:16px;
            box-shadow:0 8px 24px rgba(0,0,0,0.12);
        }
        h3{
            text-align:center;
            color:#ff7b26;
            font-size:26px;
            margin-bottom:24px;
        }
        .item{
            margin:16px 0;
            font-size:18px;
        }
        input{
            width:100%;
            padding:10px;
            margin-top:6px;
            border:1px solid #ccc;
            border-radius:6px;
        }
        button{
            width:100%;
            padding:12px;
            background:#ff7b26;
            color:white;
            border:none;
            border-radius:6px;
            font-size:18px;
            cursor:pointer;
        }
        a{
            display:block;
            text-align:center;
            margin-top:16px;
            color:#ff7b26;
            text-decoration:none;
        }
    </style>
</head>
<body>
    <div class="container">
        <h3>修改个人资料</h3>
        <%
            UserBean user = (UserBean)session.getAttribute("loginUser");
            if(user == null){
                response.sendRedirect("login.jsp");
                return;
            }
//            String name = user.getName();
//            String phone = user.getPhone();
        %>
        <form action="UpdateUserInfoServlet" method="post">
            <div class="item">
                用户名：
                <input type="text" name="username" value="<%=user.getUsername()%>" readonly>
            </div>
            <div class="item">
                姓名：
                <input type="text" name="name" value="<%=user.getName()%>">
            </div>
            <div class="item">
                手机号：
                <input type="text" name="phone" value="<%=user.getPhone()%>">
            </div>
            <button type="submit">保存修改</button>
        </form>
        <a href="userinfo.jsp">返回个人中心</a>
    </div>
</body>
</html>