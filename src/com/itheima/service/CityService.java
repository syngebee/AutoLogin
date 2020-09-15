package com.itheima.service;

import com.itheima.dao.CityDao;
import com.itheima.utils.MybatisUtils;

import java.util.List;

public class CityService {

    public List<String> getCities() {
        CityDao dao = MybatisUtils.getSqlSession().getMapper(CityDao.class);
        return dao.getCities();
    }
}
