package com.itheima.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            InputStream is = Resources.getResourceAsStream("sqlMapConfig.xml");
            sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sqlSession
     *
     * @return
     */
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true);
    }
}
