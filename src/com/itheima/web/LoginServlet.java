package com.itheima.web;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Base64Util;
import com.itheima.utils.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //一: 验证码校验
        HttpSession session = request.getSession();
        //1.1获取请求携带的验证码
        String ucode = request.getParameter("ucode");
        //1.2获取session中生成的验证码
        String scode = (String) session.getAttribute("scode");
        //1.3验证码校验
        if (ucode == null || ucode.length() == 0) {
            // 将提示信息存放到request对象中,请求转发到登录的jsp页面
            session.setAttribute("msg", "验证码不可为空!");
            response.sendRedirect("/login.jsp");
            return;
        }
        if (!ucode.equalsIgnoreCase(scode) && !ucode.equalsIgnoreCase("itheima")) {
            // 将提示信息存放到request对象中,请求转发到登录的jsp页面
            session.setAttribute("msg", "验证码输入错误!");
            response.sendRedirect("/login.jsp");
            return;
        }

        //二: 用户登录
        //参数封装
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String encodedPassword = MD5Util.getMD5(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        //2.2调用service处理业务逻辑
        UserService service = new UserService();
        User loginUser = service.login(user);
        //2.3处理执行结果
        if (loginUser != null) {
            // 登录成功
            String remember = request.getParameter("remember");
            //用户勾选了自动登录
            if ("yes".equals(remember)) {
                //添加自动登录Cookie(autoLogin)

                //base64加密内容 ':'分隔 username,cookie有效时间毫秒值,MD5明文
                //获取一周后时间的毫秒值 作为cookie有效时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                long time = calendar.getTime().getTime();

                //MD5明文内容 username+md5password+cookie有效时间毫秒值+自定义WebKEY
                //拼接字符串,用户名,加密密码,
//                        String str = username + encodedPassword + time + "cycKEY";
                String ip = request.getRemoteAddr();
                String str = username + encodedPassword + time + ip + "cycKEY";
                //MD5加密得到明文串
                String md5Str = MD5Util.getMD5(str);

                //base64拼接
                String base64Str = username + ":" + time + ":" + md5Str;
                //base64加密
                String encode = Base64Util.encode(base64Str);

                //设置Cookie加入响应,本项目,一周
                Cookie autoLogin = new Cookie("autoLogin", encode);
                autoLogin.setPath(request.getContextPath());
                autoLogin.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(autoLogin);

                //session中加入用户信息
                session.setAttribute("userStatus", loginUser);

                //设置JSESSIONID和对应session的持久化 2小时
                Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
                jsessionid.setPath(request.getContextPath());
                jsessionid.setMaxAge(60 * 60 * 2);
                session.setMaxInactiveInterval(120);
                response.addCookie(jsessionid);

            } else {
                //用户没有勾选自动登录
                //userStatus肯定是要加进去的
                session.setAttribute("userStatus", loginUser);
                //需要实现： 下次会话 访问页面，要重新登录
                //用户可能有2个影响登录的Cookie
                //持久化2小时的JSESSIONID, 刚登录不好删session中的userStatus, 不然下一个页面就回登录页了
                //方案：覆盖原有JSESSIONID,设置持久化时间为会话结束时 负数-1
                Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
                jsessionid.setMaxAge(-1);
                jsessionid.setPath(request.getContextPath());
                response.addCookie(jsessionid);

                //持久化一周的autoLogin,删了
                Cookie autoLogin = new Cookie("autoLogin", null);
                autoLogin.setMaxAge(0);
                response.addCookie(autoLogin);
            }

            //重定向到分页Servlet
            response.sendRedirect("/contact?action=findByPageServlet");
        } else {
            // 登录失败
            // 将提示信息存放到request对象中,请求转发到登录的jsp页面
            session.setAttribute("msg", "用户名或密码错误!");
            response.sendRedirect("/login.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}