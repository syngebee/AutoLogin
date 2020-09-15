package com.itheima.service;

import com.itheima.dao.ContactDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Contact;
import com.itheima.pojo.User;
import com.itheima.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserService {
    public User login(User user) {
        //1.获取sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //2.获取dao接口的实现类对象
        UserDao dao = sqlSession.getMapper(UserDao.class);
        //3.调用方法执行
        User u = dao.login(user);
        sqlSession.close();
        return u;
    }

    public User findUserByUsername(String s) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao dao = sqlSession.getMapper(UserDao.class);

        return dao.findUserByUsername(s);
    }
}
