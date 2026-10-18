package com.catshop.servlet;

import com.catshop.dao.UserDAO;
import com.catshop.javabean.UserBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 权限校验
        HttpSession session = request.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        UserDAO userDAO = new UserDAO();
        userDAO.delete(id);

        // 删除成功跳回用户列表
        response.sendRedirect("adminUserList.jsp");
    }
}