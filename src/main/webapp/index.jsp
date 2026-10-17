<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>喵鲜猫粮商城 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/productDetail.css">
    <style>
        .banner{
            background: #d46b29;
            color:white;
            text-align:center;
            padding:60px 20px;
        }
        .banner h1{
            font-size:42px;
            margin:0 0 15px;
        }
        .banner p{
            font-size:18px;
        }
        .enter-btn{
            display:inline-block;
            margin-top:25px;
            padding:14px 32px;
            background:white;
            color:#d46b29;
            text-decoration:none;
            font-weight:bold;
            border-radius:8px;
            font-size:18px;
        }
        .intro{
            width:80%;
            margin:40px auto;
            text-align:center;
        }
        .intro h2{
            color:#333;
        }
        .intro p{
            font-size:16px;
            color:#666;
            line-height:1.8;
        }
        footer{
            background:#443328;
            color:#fff;
            text-align:center;
            padding:20px;
            margin-top:60px;
        }
    </style>
</head>
<body>
<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="nav-links">
        <a href="#">首页</a>
        <a href="ProductListServlet">全部商品</a>
        <a href="#">购物车</a>
        <a href="UserInfoServlet">个人中心</a>
        <a href="UserLogoutServlet">退出登录</a>
    </div>
</nav>

<section class="banner">
    <h1>喵鲜猫粮商城</h1>
    <p>专注优质猫粮，守护猫咪健康饮食</p>
    <a href="ProductListServlet" class="enter-btn">进入商品列表</a>
</section>

<section class="intro">
    <h2>商城介绍</h2>
    <p>喵鲜猫粮商城是第三小组课程开发的线上猫粮电商平台。平台提供幼猫、成猫、全期多种猫粮选购</br>
        支持商品浏览、条件检索、查看商品详情。管理员后台可完成商品的新增、修改、删除、管理。</p>
</section>

<footer>
    <p>&copy;202610 喵鲜猫粮商城 | 第三小组课程项目 版权所有</p>
</footer>
</body>
</html>