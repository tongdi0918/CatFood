package com.catshop.servlet;

import com.catshop.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //System.out.println("收到注册：username="+username + " password="+password);

        UserDAO userDAO = new UserDAO();
        boolean result = userDAO.register(username, password);
        //System.out.println("注册结果result = "+result);

        if (result) {
            //注册成功跳登录页

            response.sendRedirect("login.jsp?registermsg=success");
        } else {
            //注册失败，回到注册页
            response.sendRedirect("register.jsp?registermsg=fail");
        }
    }
}