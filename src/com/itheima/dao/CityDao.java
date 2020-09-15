package com.itheima.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CityDao {

    @Select("select city from city")
    List<String> getCities();
}
