<%--
  Created by IntelliJ IDEA.
  User: 38532
  Date: 2026/10/18
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.javabean.UserBean" %>
<%@ page import="com.catshop.dao.UserDAO" %>
<html>
<head>
    <title>编辑用户</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body{
            background: linear-gradient(135deg,#e8f0fe,#f4f7fa);
            padding:40px;
            font-family:"Microsoft Yahei",sans-serif;
        }
        .container{
            width:500px;
            margin:0 auto;
            background:#ffffff;
            padding:35px;
            border-radius:16px;
        }
        h2{
            text-align:center;
            color:#f06020;
            margin-bottom:30px;
        }
        .item{
            margin:15px 0;
        }
        label{
            display:block;
            margin-bottom:6px;
        }
        input{
            width:100%;
            padding:10px;
            border:1px solid #ccc;
            border-radius:6px;
        }
        .btn-group{
            margin-top:30px;
            text-align:center;
        }
        button{
            padding:10px 25px;
            background-color:#f06020;
            color:white;
            border:none;
            border-radius:6px;
            cursor:pointer;
        }
        a{
            display:inline-block;
            margin-left:20px;
            text-decoration:none;
            color:#f06020;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>编辑用户信息</h2>
        <%
            // 获取url上面传过来的id
            String idStr = request.getParameter("id");
            int uid = Integer.parseInt(idStr);
            UserDAO userDAO = new UserDAO();
            UserBean user = userDAO.findById(uid);
        %>
        <form action="UpdateUserInfoServlet" method="post">
            <input type="hidden" name="id" value="<%=user.getId()%>">
            <div class="item">
                <label>用户名</label>
                <input type="text" name="username" value="<%=user.getUsername()%>">
            </div>
            <div class="item">
                <label>姓名</label>
                <input type="text" name="name" value="<%=user.getName()%>">
            </div>
            <div class="item">
                <label>手机号</label>
                <input type="text" name="phone" value="<%=user.getPhone()%>">
            </div>
            <div class="item">
                <label>角色</label>
                <input type="text" name="role" value="<%=user.getRole()%>">
            </div>
            <div class="btn-group">
                <button type="submit">保存修改</button>
                <a href="adminUserList.jsp">返回列表</a>
            </div>
        </form>
    </div>
</body>
</html>