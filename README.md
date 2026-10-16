# 喵鲜猫粮商城 - Java Web 实训项目

一个基于 **原生 Servlet + JSP + MySQL** 的简易猫粮线上商店，采用经典 MVC 架构。

## 技术栈

| 层次 | 技术 |
|------|------|
| 后端 | Java Servlet 4.0、JSP、原生 JDBC |
| 数据库 | MySQL 8.0 |
| 前端 | HTML5、CSS3、原生 JavaScript (AJAX) |
| 架构 | MVC（Model - View - Controller） |

## 项目结构---根据实际需求改动

```
CatFood/
├── sql/
│   └── catfood.sql              # 数据库建表脚本 + 测试数据
├── src/
│   └── com/catfood/
│       ├── model/               # JavaBean 实体层
│       │   ├── User.java        # 用户
│       │   ├── Product.java     # 商品（猫粮）
│       │   ├── Order.java       # 订单
│       │   ├── OrderItem.java   # 订单明细
│       │   └── CartItem.java    # 购物车项
│       ├── dao/                 # 数据访问层（原生 JDBC）
│       │   ├── DBUtil.java      # 数据库连接工具
│       │   ├── UserDAO.java     # 用户 DAO
│       │   ├── ProductDAO.java  # 商品 DAO
│       │   └── OrderDAO.java    # 订单 DAO（含事务）
│       ├── filter/
│       │   └── LoginFilter.java  # 登录验证过滤器
│       └── servlet/             # 控制器层
│           ├── UserLoginServlet.java    # 登录
│           ├── UserRegisterServlet.java # 注册
│           ├── UserLogoutServlet.java   # 登出
│           ├── ProductListServlet.java # 商品列表
│           ├── ProductDetailServlet.java # 商品详情
│           ├── CartServlet.java         # 购物车
│           ├── OrderServlet.java        # 订单
│           ├── AdminProductServlet.java # 后台商品管理
│           └── AdminOrderServlet.java   # 后台订单管理
└── WebContent/
    ├── WEB-INF/
    │   ├── web.xml              # 部署描述符
    │   └── lib/                # 放 MySQL 驱动 jar
    ├── css/style.css           # 公共样式
    ├── js/main.js              # 公共 JS
    ├── index.jsp                # 首页（重定向到商品列表）
    ├── login.jsp                # 登录页
    ├── register.jsp             # 注册页
    ├── product_list.jsp         # 商品列表（带分类筛选+搜索）
    ├── product_detail.jsp       # 商品详情（AJAX 加购物车）
    ├── cart.jsp                 # 购物车（AJAX 改数量）
    ├── order_list.jsp           # 我的订单
    ├── order_detail.jsp         # 订单详情
    └── admin/
        ├── product_list.jsp     # 后台商品列表
        ├── product_edit.jsp      # 后台商品新增/编辑
        ├── order_list.jsp        # 后台订单列表
        └── order_detail.jsp      # 后台订单详情
```

## 核心功能

### 前台用户端
- 加载商品页面，调用filter进行登录验证
- 用户注册 / 登录 / 登出
- 商品浏览：分类筛选、关键字搜索、商品详情
- 购物车：加入购物车（AJAX）、修改数量（AJAX）、删除、清空
- 订单管理：提交订单（JDBC+扣库存）、查看订单列表、订单详情、模拟支付、取消订单

### 管理员后台
- 商品管理：新增、编辑、删除、上架/下架
- 订单管理：查看所有订单、订单详情、发货、完成订单

