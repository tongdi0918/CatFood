package com.catshop.servlet;

import com.catshop.dao.UserDAO;
import com.catshop.javabean.UserBean;
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

        // 空值校验：用户名、密码不能为空
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            response.sendRedirect("register.jsp?registermsg=empty");
            return;
        }
        username = username.trim();

        UserDAO userDAO = new UserDAO();

        // 用户名查重：已存在则提示，而不是靠数据库唯一键报错
        UserBean exist = userDAO.findUserByUsername(username);
        if (exist != null) {
            response.sendRedirect("register.jsp?registermsg=exists");
            return;
        }

        boolean result = userDAO.register(username, password);

        if (result) {  response.sendRedirect("login.jsp?registermsg=success");  }
        else {   response.sendRedirect("register.jsp?registermsg=fail");    }
    }
}
