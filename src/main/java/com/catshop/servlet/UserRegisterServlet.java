package com.catshop.servlet;

import com.catshop.dao.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        boolean result = userDAO.register(username, password);

        if (result) {
            //注册成功跳转到登录页面
            response.sendRedirect("login.jsp");
        } else {
            //注册失败留在注册页面
            response.sendRedirect("register.jsp");
        }
    }
}