package com.catshop.filter;

import com.catshop.javabean.UserBean;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();

        //白名单：登录页面、登录servlet、注册页面、注册servlet、静态资源(css/js/images)直接放行
        //（静态资源必须放行，否则未登录时样式、脚本、图片都会被重定向到登录页，页面样式错乱）
        if(path.contains("login.jsp") || path.contains("UserLoginServlet")
                || path.contains("register.jsp") || path.contains("UserRegisterServlet")
                || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")){
            chain.doFilter(request,response);
            return;
        }

        HttpSession session = req.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if(loginUser == null){
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }       //未登录，拦截跳转到登录页

        //访问后台，必须是管理员admin
        if(path.startsWith("/admin/")){
            if(!"admin".equals(loginUser.getRole())){       //普通用户，无权访问后台，跳转到首页
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                return;
            }
        }
        //全部校验通过，放行
        chain.doFilter(request,response);
    }
}