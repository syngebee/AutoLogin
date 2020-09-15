package com.itheima.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("过滤器执行");
        //过滤请求
        HttpServletRequest request = (HttpServletRequest) req;
        //1.获取请求方式
        String method = request.getMethod();
        //2.判断请求方式
        if ("post".equalsIgnoreCase(method)) {
            request.setCharacterEncoding("utf-8");
        }
        // 放行
        chain.doFilter(request, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
