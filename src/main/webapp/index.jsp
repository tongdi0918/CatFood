<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.catshop.javabean.UserBean" %>
<html>
<head>
    <title>喵鲜猫粮商城</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Microsoft Yahei", "PingFang SC", sans-serif;
            background: #f7f9fc;
            color: #333;
        }

        /* 顶部导航 */
        .top-nav {
            height: 70px;
            background: #ffffff;
            border-bottom: 1px solid #eaeaea;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .logo {
            font-size: 22px;
            font-weight: bold;
            color: #ff8a3d;
        }

        .logo span {
            color: #333;
        }

        .nav-links a {
            margin: 0 12px;
            text-decoration: none;
            color: #555;
            font-size: 14px;
        }

        .nav-links a:hover {
            color: #ff8a3d;
        }

        /* 顶部横幅 */
        .banner {
            position: relative;
            height: 420px;
            background: linear-gradient(135deg, #ff9a56 0%, #ff6a3d 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .banner::before {
            content: "";
            position: absolute;
            inset: 0;
            background-image:
                radial-gradient(circle at 20% 30%, rgba(255,255,255,0.25) 0, transparent 40%),
                radial-gradient(circle at 80% 70%, rgba(255,255,255,0.18) 0, transparent 35%);
        }

        .banner-content {
            position: relative;
            text-align: center;
            color: #fff;
            z-index: 2;
        }

        .banner h1 {
            font-size: 44px;
            margin-bottom: 12px;
            letter-spacing: 2px;
        }

        .banner p {
            font-size: 16px;
            margin-bottom: 30px;
            opacity: 0.95;
        }

        .btn-primary {
            display: inline-block;
            padding: 14px 36px;
            background: #fff;
            color: #ff6a3d;
            text-decoration: none;
            border-radius: 30px;
            font-weight: bold;
            box-shadow: 0 8px 20px rgba(0,0,0,0.12);
            transition: 0.3s;
        }

        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 12px 24px rgba(0,0,0,0.18);
        }

        /* 通用区块 */
        .section {
            max-width: 1100px;
            margin: 50px auto;
            padding: 0 30px;
        }

        .section-title {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
            border-left: 5px solid #ff8a3d;
            padding-left: 12px;
        }

        /* 商城介绍 */
        .intro-card {
            background: #fff;
            padding: 30px;
            border-radius: 14px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.06);
            line-height: 1.8;
            color: #555;
        }

        /* 首页功能入口 */
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-top: 20px;
        }

        .feature-item {
            text-decoration: none;
            color:#333;
            background: #fff;
            padding: 24px;
            border-radius: 14px;
            text-align: center;
            box-shadow: 0 4px 14px rgba(0,0,0,0.06);
            transition: 0.3s;
        }

        .feature-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 24px rgba(255,138,61,0.2);
        }

        .feature-icon {
            width: 56px;
            height: 56px;
            margin: 0 auto 14px;
            background: #fff3e8;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 26px;
        }

        .feature-item h3 {
            margin-bottom: 8px;
            color: #333;
        }

        .feature-item p {
            font-size: 14px;
            color: #777;
        }

        /* 底部 */
       footer {
             background: #2c2c2c;
             color: #bbb;
             text-align: center;
             padding: 24px;
             margin-top: 60px;
             font-size: 14px;
       }
            </style>
        </head>
        <body>
        <!-- 顶部导航 -->
        <div class="top-nav">
            <div class="logo">喵鲜<span>猫粮商城</span></div>
            <%
                UserBean loginUser = (UserBean)session.getAttribute("loginUser");
            %>
           <div class="nav-links">
               <a href="index.jsp">首页</a>
               <a href="product_list.jsp">全部商品</a>
               <a href="userinfo.jsp">个人中心</a>
               <%
                   if(loginUser != null && "admin".equals(loginUser.getRole())){
               %>
                   <a href="adminUserList.jsp">管理员</a>
               <%
                   }
               %>
               <a href="UserLogoutServlet">退出登录</a>
           </div>
        </div>
        <!-- 顶部横幅 -->
        <div class="banner">
            <div class="banner-content">
                <h1>喵鲜猫粮商城</h1>
                <p>专为猫咪准备的优质猫粮，科学配方，健康好粮</p>
                <a href="product_list.jsp" class="btn-primary">进入商品商城</a>
            </div>
        </div>
        <!-- 首页功能入口 -->
        <div class="section">
            <h2 class="section-title">商城入口</h2>
            <div class="feature-grid">
                <a href="product_list.jsp" class="feature-item">
                    <div class="feature-icon">🛒</div>
                    <h3>全部商品</h3>
                    <p>浏览猫粮商品</p>
                </a>
                <a href="userinfo.jsp" class="feature-item">
                    <div class="feature-icon">👤</div>
                    <h3>个人中心</h3>
                    <p>查看个人信息</p>
                </a>
                <a href="order_Servlet" class="feature-item">
                    <div class="feature-icon">📦</div>
                    <h3>我的订单</h3>
                    <p>管理订单记录</p>
                </a>

                <%
                    if(loginUser != null && "admin".equals(loginUser.getRole())){
                %>
                <a href="adminUserList.jsp" class="feature-item">
                    <div class="feature-icon">⚙️</div>
                    <h3>管理后台</h3>
                    <p>管理用户与商品</p>
                </a>
                <%
                    }
                %>
            </div>
        </div>
        <!-- 商城介绍 -->
        <div class="section">
            <h2 class="section-title">商城介绍</h2>
            <div class="intro-card">
                <p>
                    喵鲜猫粮商城是一个面向猫咪爱好者的猫粮购买平台，提供多种优质猫粮商品，支持用户注册、登录、浏览商品、下单购买和订单管理。
                    管理员可以通过后台管理用户信息，维护商品数据，帮助商城稳定运行。我们致力于为每一只猫咪提供健康、安全、营养丰富的主食选择。
                </p>
            </div>
        </div>
        <!-- 底部信息 -->
        <footer>
            &copy; 2026 喵鲜猫粮商城 | 小猫的每一口，都值得认真选择|第三小组课程项目
        </footer>
        </body>
        </html>