package com.catshop.filter;

import com.catshop.javabean.UserBean;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();

        //白名单：登录页面、登录servlet，不需要登录直接访问
        if(path.contains("login.jsp") || path.contains("UserLoginServlet")){
            chain.doFilter(request,response);
            return;
        }

        HttpSession session = req.getSession();
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");

        if(loginUser != null){
            //已经登录，放行
            chain.doFilter(request,response);
        }else{
            //未登录，拦截，跳转到登录页
            resp.sendRedirect("login.jsp");
        }
    }
}