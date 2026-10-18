package com.catshop.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 统一字符编码过滤器
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String enc = filterConfig.getInitParameter("encoding");
        if (enc != null && !enc.isEmpty()) {
            this.encoding = enc;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        // 不在此强制 text/html：否则 CSS/JS/图片等静态资源会被错误地以 HTML 返回，
        // 浏览器会忽略样式表，导致页面无样式、图片不缩放。各 Servlet/JSP 会自行设置正确的 Content-Type。
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
