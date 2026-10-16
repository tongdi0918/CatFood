<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>商品新增/编辑</title>
</head>
<body>
<h2>
    ${editProduct==null?"新增商品":"编辑商品"}
</h2>

<form action="AdminProductServlet" method="post">
    <c:if test="${editProduct != null}">
        <input type="hidden" name="action" value="saveEdit">
        <input type="hidden" name="pid" value="${editProduct.pid}">
    </c:if>
    <c:if test="${editProduct == null}">
        <input type="hidden" name="action" value="saveAdd">
    </c:if>

    <table>
        <tr>
            <td>商品名称：</td>
            <td><input type="text" name="pname" value="${editProduct.pname}" required></td>
        </tr>
        <tr>
            <td>价格：</td>
            <td><input type="number" step="0.01" name="price" value="${editProduct.price}" required></td>
        </tr>
        <tr>
            <td>分类：</td>
            <td>
                <select name="category">
                    <option value="幼猫" ${editProduct.category=="幼猫"?"selected":""}>幼猫</option>
                    <option value="成猫" ${editProduct.category=="成猫"?"selected":""}>成猫</option>
                    <option value="全期" ${editProduct.category=="全期"?"selected":""}>全期</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>库存：</td>
            <td><input type="number" name="stock" value="${editProduct.stock}" required></td>
        </tr>
        <tr>
            <td>图片路径：</td>
            <td><input type="text" name="image" value="${editProduct.image}"></td>
        </tr>
        <tr>
            <td>商品描述：</td>
            <td><textarea name="description" rows="4" cols="30">${editProduct.description}</textarea></td>
        </tr>
        <tr>
            <td>状态：</td>
            <td>
                <input type="radio" name="status" value="1" ${editProduct.status==1||editProduct==null?"checked":""}>上架
                <input type="radio" name="status" value="0" ${editProduct.status==0?"checked":""}>下架
            </td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="保存"></td>
        </tr>
    </table>
</form>
<a href="AdminProductServlet?action=list">返回商品列表</a>
</body>
</html>
