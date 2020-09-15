package com.itheima.filter;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Base64Util;
import com.itheima.utils.MD5Util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

//@WebFilter("/*")
//不用注解, 改用XML方式配置，设置初始参数，通过init方法读取不需要过滤的请求字符串数组。
public class PermissionsFilter implements Filter {
    //成员属性,需要忽略的路径数组
    private String[] ignoreArr = null;

    //过滤器初始化方法
    public void init(FilterConfig config) throws ServletException {
        //过滤器创建时读取xml中配置的初始参数ignore, 返回字符串,split获得数组
        //赋值给成员属性ignoreArr,完成初始化
        ignoreArr = config.getInitParameter("ignore").split(",");
    }

    //过滤器销毁前方法
    public void destroy() {
    }

    //自定义方法,校验请求是否是需要忽略
    private Boolean isIgnore(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String s : ignoreArr) {
            if (uri.contains(s)) {
                return true;
            }
        }
        return false;
    }

    //过滤拦截方法
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        // 过滤请求
        // 判断当前是否有用户登录
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        //String requestURI = request.getRequestURI();
        //System.out.println("requestURI = " + requestURI);
        Boolean flag = isIgnore(request);
        if (!flag) {
            //System.out.println("拦截");
            Object user = session.getAttribute("userStatus");

            //user!=null直接放行
            if (user == null) {
                Cookie[] cookies = request.getCookies();
                String str = "";
                if (cookies != null && cookies.length != 0) {
                    for (Cookie cookie : cookies) {
                        if ("autoLogin".equals(cookie.getName())) {
                            str = cookie.getValue();
                            break;
                        }
                    }
                }

                //cookie中 autoLogin 没找到直接再见
                if ("".equals(str)) {
                    response.sendRedirect("/login.jsp");
                    return;
                }

                //base64解密
                String decodeStr = Base64Util.decode(str);
                //分隔
                String[] value = decodeStr.split(":");

                // value[0] username
                // value[1] 有效时间毫秒值
                // value[2] MD5明文

                //长度不等于3 直接再见
                if (value.length != 3) {
                    response.sendRedirect("/login.jsp");
                    return;
                }

                long time = new Date().getTime();
                long validTime = 0;
                try {
                    validTime = Long.parseLong(value[1]);
                } catch (NumberFormatException e) {
                    //转不成功你就再见吧
                    e.printStackTrace();
                    response.sendRedirect("/login.jsp");
                    return;
                }

                //有效时间过了,设置提示信息 再见
                if (time > validTime) {
                    session.setAttribute("msg", "自动登录时效已过,请重新登录");
                    response.sendRedirect("/login.jsp");
                    return;
                }

                //根据Cookie中的username得到User对象
                UserService userService = new UserService();
                User userByUsername = userService.findUserByUsername(value[0]);

                //没有就再见了
                if (userByUsername == null) {
                    response.sendRedirect("/login.jsp");
                    return;
                }

                //根据返回的对象 获取正确加密明文 和 Cookie中的加密明文作比较
                //验证登录信息，有效时间，项目密钥
//                String md5 = MD5Util.getMD5(userByUsername.getUsername() + userByUsername.getPassword() + value[1] + "cycKEY");

                String ip = request.getRemoteAddr();
                String md5 = MD5Util.getMD5(userByUsername.getUsername() + userByUsername.getPassword() + value[1] + ip + "cycKEY");

                System.out.println("value[2] = " + value[2]);
                System.out.println("md5 = " + md5);

                //不对，登录去
                if (!md5.equals(value[2])) {
                    response.sendRedirect("/login.jsp");
                    return;
                }

                //走到这儿验证通过，自动登录

                //userStatus加进去
                session.setAttribute("userStatus", userByUsername);
                //session持久化一下，改个2小时存活，路径配一下
                Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
                jsessionid.setMaxAge(60 * 60 * 2);
                jsessionid.setPath(request.getContextPath());
                session.setMaxInactiveInterval(120);
                response.addCookie(jsessionid);
                //历经磨难 走下面放行
                System.out.println("完成自动登录");
            }
        }
        // 放行
        chain.doFilter(req, resp);
    }


}
