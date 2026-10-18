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

@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        UserBean user = userDAO.login(username,password);

        if(user != null){
            HttpSession session = request.getSession();
            session.setAttribute("loginUserId", user.getId() );
            session.setAttribute("loginUser", user);
            response.sendRedirect("index.jsp?loginmsg=loginsuccess");
        }else{
            response.sendRedirect("login.jsp?loginmsg=loginfail");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}