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

@WebServlet("/UpdateUserInfoServlet")
public class UpdateUserInfoServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("login.jsp");
            return;
        }
        //拿到表单数据
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        UserBean user = new UserBean();
        user.setId(loginUser.getId());
        user.setName(name);
        user.setPhone(phone);

        boolean result = userDAO.updateUserInfo(user);
        if(result){
            //修改成功，回到个人中心页面
            response.sendRedirect("UserInfo.jsp");
        }else{
            response.getWriter().write("修改失败！");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}