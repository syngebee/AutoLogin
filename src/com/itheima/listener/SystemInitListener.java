package com.itheima.listener;

import com.itheima.service.CityService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class SystemInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("服务器开始处理数据字典");
        CityService cityService = new CityService();
        List<String> cityList = cityService.getCities();
        servletContextEvent.getServletContext().setAttribute("cityList", cityList);
        System.out.println("处理数据字典结束");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
