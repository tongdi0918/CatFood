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
        // 获取页面提交的账号密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        //调用DAO里面你写好的 register 方法
        boolean result = userDAO.register(username,password);

        if(result){
            //注册成功 → 跳转到登录页面 login.jsp
            response.sendRedirect("login.jsp");
        }else{
            //注册失败 → 返回注册页面 register.jsp
            response.sendRedirect("register.jsp");
        }
    }
}