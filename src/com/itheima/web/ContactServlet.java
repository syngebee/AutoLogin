package com.itheima.web;

import com.itheima.pojo.Contact;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.PageCondition;
import com.itheima.service.ContactService;
import com.itheima.web.base.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/contact")
public class ContactServlet extends BaseServlet {
    private static final Integer defaultSize = 5;
    private static final ContactService contactService = new ContactService();


    public void addServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Contact contact = new Contact();
        try {
            BeanUtils.populate(contact, parameterMap);
            Boolean flag = contactService.addContact(contact);
            if (flag) {
                response.sendRedirect("/contact?action=findByPageServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] ids = parameterMap.get("id");
        System.out.println(Arrays.toString(ids));
        Boolean flag = contactService.deleteContacts(ids);
        if (flag) {
            response.sendRedirect("/contact?action=findByPageServlet&current=1");
        } else {
            response.sendRedirect("/contact/error.jsp");
        }
    }

    public void deleteServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Boolean flag = contactService.deleteContactById(id);
        response.sendRedirect("/contact?action=findByPageServlet");
    }

    public void findByPageServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        PageCondition condition = new PageCondition();
        try {
            BeanUtils.populate(condition, parameterMap);
            //封装条件对象
            //根据条件设置显示条数,默认显示条数,当前页码
            //如果用户没有填写/修改 每页显示条数,则先查找Session域中有没有默认显示条数
            //如果没有再使用 服务器默认的静态成员变量 defaultSize 赋值 （final修饰保护)
            Integer size = condition.getSize();
            if (size != null) {
                //有指定每页条数,则用用户指定,在session中设置以供该用户下次使用
                request.getSession().setAttribute("defaultSizeByUser", size);
            } else {
                //没有指定每页条数,先去session中查找有没有设置过
                if (request.getSession().getAttribute("defaultSizeByUser") == null) {
                    //没有的话,使用服务器默认的
                    condition.setSize(defaultSize);
                    //设置用户的默认值为defaultSize
                    request.getSession().setAttribute("defaultSizeByUser", defaultSize);
                } else {
                    //取session中用户的默认值
                    Integer defaultSizeByUser = (Integer) request.getSession().getAttribute("defaultSizeByUser");

                    //查询条件对象赋值，以供分页功能查询
                    condition.setSize(defaultSizeByUser);
                }
            }
            //如果没有指定要跳转那一页,默认设置跳转第一页,若当前页小于1设置为1
            String current = condition.getCurrent();
            if (current == null || current.equals("") || Integer.parseInt(current) < 1) {
                condition.setCurrent("1");
            }

            //拿到结果,因为要重定向塞入session中,一次查询一个新的PageBean
            PageBean p = contactService.findByPage(condition);
            request.getSession().setAttribute("page", p);
            //把查询条件也塞入session中,因为需要回显上一次查询的条件
            request.getSession().setAttribute("condition", condition);
            response.sendRedirect("/contact/list.jsp");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void findContactAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Contact> contactAll = contactService.findContactAll();
        request.setAttribute("list", contactAll);
        request.getRequestDispatcher("/contact/list.jsp").forward(request, response);
    }

    public void getContactServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String updatePage = request.getParameter("updatePage");
        Contact c = contactService.getContactById(id);
        request.setAttribute("contact", c);
        request.setAttribute("updatePage", updatePage);
        request.getRequestDispatcher("/contact/update.jsp").forward(request, response);
    }

    public void updateServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Contact contact = new Contact();
        try {
            String updatePage = request.getParameter("updatePage");
            BeanUtils.populate(contact, parameterMap);
            Boolean flag = contactService.updateContact(contact);
            response.sendRedirect("/contact?action=findByPageServlet&current=" + updatePage);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
