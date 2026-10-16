<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品详情</title>
</head>
<body>
<div>
    <h2>${product.pname}</h2>
    <img src="${product.image}" width="300">
    <p>价格：${product.price} 元</p>
    <p>分类：${product.category}</p>
    <p>库存：${product.stock}</p>
    <p>商品描述：${product.description}</p>
    <%--加入购物车按钮，购物车servlet由其他组员实现--%>
    <button>加入购物车</button>
    <br>
    <a href="ProductListServlet">返回商品列表</a>
</div>
</body>
</html>
