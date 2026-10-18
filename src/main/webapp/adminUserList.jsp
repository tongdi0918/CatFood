<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.dao.UserDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.catshop.javabean.UserBean" %>

<%
    // ==========页面顶部权限拦截，只在这里定义一次loginUser==========
    UserBean loginUser = (UserBean)session.getAttribute("loginUser");
    if(loginUser == null || !"admin".equals(loginUser.getRole())){
        response.sendRedirect("index.jsp");
        return;
    }
%>

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
            background-color: #f4f7fa;
            padding: 30px;
        }
        .container{
            width: 90%;
            margin: 0 auto;
            background-color: #ffffff;
            padding:30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px #00000018;
        }
        h3{
            font-size:24px;
            margin-bottom:25px;
            color:#333;
        }
        table{
            border-collapse: collapse;
            width:100%;
        }
        th,td{
            border:1px solid #ddd;
            padding:12px 8px;
            text-align:center;
        }
        th{
            background-color:#4080ff;
            color:white;
            font-weight:normal;
        }
        tr:nth-child(even){
            background-color:#f8f9fc;
        }
        a{
            text-decoration:none;
            margin:0 6px;
        }
        a:first-of-type{
            color:#28a745;
        }
        a:last-of-type{
            color:#dc3545;
        }
    </style>
</head>
<body>
<div class="container">
    <h3>管理员用户管理</h3>
    <%
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
                <a href="editUser.jsp?id=<%=u.getId()%>">修改</a>
                <a href="DeleteUserServlet?id=<%=u.getId()%>" onclick="return confirm('确定删除该用户？')">删除</a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>