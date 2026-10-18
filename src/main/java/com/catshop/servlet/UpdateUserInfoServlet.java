package com.catshop.servlet;

import com.catshop.dao.UserDAO;
import com.catshop.javabean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UpdateUserInfoServlet")
public class UpdateUserInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ✅ 最开头设置编码，防止中文乱码！
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取表单参数
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        // 封装user对象
        UserBean user = new UserBean();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setPhone(phone);
        user.setRole(role);

        // 调用DAO更新
        UserDAO userDAO = new UserDAO();
        userDAO.update(user);

        // 更新完成，跳回管理员用户列表页面
        response.sendRedirect("adminUserList.jsp");
    }
}