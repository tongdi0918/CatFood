<%--
  Created by IntelliJ IDEA.
  User: 38532
  Date: 2026/10/18
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.javabean.UserBean" %>
<%@ page import="com.catshop.dao.UserDAO" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>管理员用户管理</title>
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family:"Microsoft Yahei",sans-serif;
        }
        body{
            background: linear-gradient(135deg,#e8f0fe,#f4f7fa);
            padding:40px;
        }
        .container{
            width:800px;
            margin:0 auto;
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
        table{
            width:100%;
            border-collapse: collapse;
        }
        th,td{
            border:1px solid #ccc;
            padding:12px;
            text-align:center;
        }
        th{
            background-color:#ff7b26;
            color:white;
        }
        .back-btn{
            margin-top:20px;
            text-align:center;
        }
        a{
            padding:10px 20px;
            background:#ff7b26;
            color:white;
            text-decoration:none;
            border-radius:6px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h3>管理员用户管理</h3>
        <%
            // 判断是否是管理员登录
            UserBean loginUser = (UserBean)session.getAttribute("loginUser");
            if(loginUser == null || !"admin".equals(loginUser.getRole())){
                response.sendRedirect("login.jsp");
                return;
            }
            UserDAO userDAO = new UserDAO();
            List<UserBean> userList = userDAO.findAllUser();
        %>
        <table>
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>角色</th>
                <th>操作</th>
            </tr>
            <%
                for(UserBean u : userList){
                    String name = u.getName()==null ? "" : u.getName();
                    String phone = u.getPhone()==null ? "" : u.getPhone();
            %>
            <tr>
                <td><%=u.getId()%></td>
                <td><%=u.getUsername()%></td>
                <td><%=name%></td>
                <td><%=phone%></td>
                <td><%=u.getRole()%></td>
                <td>
                    <a href="editUser.jsp?id=<%=u.getId()%>">编辑</a>
                    &nbsp;&nbsp;
                    <a href="DeleteUserServlet?id=<%=u.getId()%>">删除</a>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <div class="back-btn">
            <a href="userinfo.jsp">返回个人中心</a>
        </div>
    </div>
</body>
</html>