<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>喵鲜猫粮商城 - 商品列表</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_list.css">
</head>
<body>
<nav>
    <strong class="logo">喵鲜猫粮商城</strong>
    <div class="nav-links">
        <a href="index.jsp">首页</a>
        <a href="ProductListServlet">全部商品</a>
        <a href="#">购物车</a>
        <a href="#">个人中心</a>
    </div>
</nav>

<header>
    <h1>商品列表</h1>
    <p>精选优质猫粮，给猫咪更好的饮食体验</p>
</header>

<main>
    <section class="search-section">
        <form action="ProductListServlet" method="get">
            <label>分类：</label>
            <select name="category">
                <option value="全部" ${category == '全部' ? 'selected' : ''}>全部</option>
                <option value="幼猫" ${category == '幼猫' ? 'selected' : ''}>幼猫</option>
                <option value="全期" ${category == '全期' ? 'selected' : ''}>全期</option>
                <option value="成猫" ${category == '成猫' ? 'selected' : ''}>成猫</option>
            </select>
            <label>搜索商品：</label>
            <input type="text" name="keyword" placeholder="输入商品名称进行搜索" value="${keyword}">
            <button type="submit">查询</button>
        </form>
    </section>

    <section class="product-wrap">
        <%-- 新增：判断商品列表是否为空 --%>
        <c:choose>
            <c:when test="${empty productList}">
                <div style="text-align:center; padding:50px 0; font-size:18px; color:#777;">
                    暂无符合条件的商品，请更换分类或关键词重新查询
                </div>
            </c:when>
            <c:otherwise>
                <%-- 原来的商品循环代码不动 --%>
                <c:forEach items="${productList}" var="product">
                    <article class="product-card">
                        <figure class="card-img">
                            <%--图片加跳转链接--%>
                            <a href="${pageContext.request.contextPath}/ProductDetailServlet?pid=${product.pid}">
                                <img src="${pageContext.request.contextPath}/${product.image}" alt="${product.pname}">
                            </a>
                        </figure>
                        <div class="card-body">
                            <h3 class="card-name">${product.pname}</h3>
                            <p class="card-price">¥ ${product.price}</p>
                            <p class="card-category">分类：${product.category}</p>
                        </div>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<footer>
    <p>&copy;202610喵鲜猫粮商城 | 第三小组课程项目 版权所有</p>
</footer>
</body>
</html>