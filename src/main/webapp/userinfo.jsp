<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.javabean.UserBean" %>
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
            background: linear-gradient(135deg,#e8f0fe,#f4f7fa);
            /* 让卡片垂直+水平居中 */
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
            text-align:center;
        }
        h3{
            color:#ff7b26;
            font-size:26px;
            margin-bottom:24px;
        }
        .item{
            line-height:42px;
            font-size:18px;
            text-align:left;
        }
        .btn-group{
            margin-top:24px;
            display:flex;
            gap:10px;
        }
        button{
            flex:1;
            padding:12px;
            background:#ff7b26;
            color:white;
            border:none;
            border-radius:6px;
            font-size:16px;
            cursor:pointer;
        }
    </style>
</head>
<body>
    <div class="container">
        <h3>个人中心</h3>
        <%
            UserBean user = (UserBean)session.getAttribute("loginUser");
            if(user == null){
                response.sendRedirect("login.jsp");
                return;
            }
            // null处理，如果字段为空就变成空字符串
            String name = user.getName() == null ? "" : user.getName();
            String phone = user.getPhone() == null ? "" : user.getPhone();
        %>
        <div class="item">ID：<%=user.getId()%></div>
        <div class="item">用户名：<%=user.getUsername()%></div>
        <div class="item">姓名：<%=name%></div>
        <div class="item">手机号：<%=phone%></div>
        <div class="item">角色：<%=user.getRole()%></div>

        <div class="btn-group">
            <button onclick="location.href='index.jsp'">首页</button>
            <button onclick="location.href='updateUserInfo.jsp'">修改个人资料</button>
            <button onclick="location.href='LogoutServlet'">退出登录</button>
            <%
                if("admin".equals(user.getRole())){
            %>
            <button onclick="location.href='adminUserList.jsp'">管理员用户管理</button>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>