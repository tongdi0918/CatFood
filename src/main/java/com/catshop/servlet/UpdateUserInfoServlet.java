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
        response.setCharacterEncoding("UTF-8");   // getWriter 输出中文必须显式 UTF-8，否则按 ISO-8859-1 乱码
        HttpSession session = request.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 表单中的 id 参数：仅管理员在 editUser.jsp 编辑其他用户时才携带
        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.trim().isEmpty()) {
            // ========== 管理员编辑指定用户 ==========
            if (!"admin".equals(loginUser.getRole())) {
                response.sendRedirect("index.jsp");
                return;
            }
            int uid;
            try {
                uid = Integer.parseInt(idStr.trim());
            } catch (NumberFormatException e) {
                response.getWriter().write("用户ID格式错误！");
                return;
            }
            UserBean user = new UserBean();
            user.setId(uid);
            user.setUsername(request.getParameter("username"));
            user.setName(request.getParameter("name"));
            user.setPhone(request.getParameter("phone"));
            user.setRole(request.getParameter("role"));

            boolean ok = userDAO.update(user); // update: username,name,phone,role where id=?
            if (ok) {
                response.sendRedirect("adminUserList.jsp");
            } else {
                response.getWriter().write("修改失败！");
            }
            return;
        }

        //  普通用户只修改自己的姓名、手机号
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        UserBean user = new UserBean();
        user.setId(loginUser.getId());
        user.setName(name);
        user.setPhone(phone);

        boolean result = userDAO.updateUserInfo(user);
        if (result) {
            loginUser.setName(name);
            loginUser.setPhone(phone);
            session.setAttribute("loginUser", loginUser);
            // 修改成功，回到个人中心页面
            response.sendRedirect("userinfo.jsp");
        } else {
            response.getWriter().write("修改失败！");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
