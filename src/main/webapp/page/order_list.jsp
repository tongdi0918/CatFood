<%@ page import="com.catfood.javabean.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>订单展示</title>
  </head>
  <body>

  <h2 style="margin-bottom:20px;">我的订单</h2>
    <%List<Order> orders = (List<Order>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) { %>
    <div class="card" style="text-align:center;padding:60px;color:#999;">
       暂无订单，<a href="${pageContext.request.contextPath}/products">去选购吧~</a>
    </div>





  </body>
</html>
