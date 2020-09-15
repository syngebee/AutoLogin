package com.itheima.service;

import com.itheima.dao.ContactDao;
import com.itheima.pojo.Contact;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.PageCondition;
import com.itheima.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ContactService {

    public List<Contact> findContactAll() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        List<Contact> contactAll = dao.findContactAll();
        sqlSession.close();
        return contactAll;
    }

    public Boolean addContact(Contact contact) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        Boolean flag = true;
        int count = dao.addContact(contact);
        if (count != 1) {
            flag = false;
        }
        sqlSession.close();
        return flag;
    }

    public Contact getContactById(String id) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        Contact contactById = dao.getContactById(id);
        sqlSession.close();
        return contactById;
    }

    public Boolean updateContact(Contact contact) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        Boolean flag = true;
        int count = dao.updateContact(contact);
        if (count != 1) {
            flag = false;
        }
        sqlSession.close();
        return flag;
    }

    public Boolean deleteContactById(String id) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        Boolean flag = true;
        int count = dao.deleteContactById(id);
        if (count != 1) {
            flag = false;
        }
        sqlSession.close();
        return flag;
    }

    public Boolean deleteContacts(String[] ids) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        Boolean flag = true;
        int count = dao.deleteContacts(ids);
        if (count != ids.length) {
            flag = false;
        }
        sqlSession.close();
        return flag;
    }

    private int getContactNumber(PageCondition condition) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        int contactNumber = dao.getContactNumber(condition);
        sqlSession.close();
        return contactNumber;
    }

    public PageBean findByPage(PageCondition condition) {
        //目的: 封装一个完整的PageBean,存进域中使用el调用展示
        PageBean pageBean = new PageBean();
        //先不设值,计算完总页码后判断后赋值
        String current = condition.getCurrent();
        //存在参数中,已处理
        Integer size = condition.getSize();
        pageBean.setSize(size + "");
        //dao预备
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        ContactDao dao = sqlSession.getMapper(ContactDao.class);
        //查询总记录数
        int totalCount = dao.getContactNumber(condition);
        pageBean.setTotalCount(totalCount + "");
        //计算总页数
        int totalPage = totalCount % size == 0 ?
                totalCount / size :
                totalCount / size + 1;
        pageBean.setTotalPage(totalPage + "");
        //判断当前页码是否超出总页数
        if (Integer.parseInt(current) > totalPage) {
            //超出设置尾页
            pageBean.setCurrent(totalPage + "");
        } else {
            //未超出直接赋值
            pageBean.setCurrent(current);
        }

        //计算当前页首条数据索引
        int startIndex = (Integer.parseInt(pageBean.getCurrent()) - 1) * size;
        if (startIndex < 0) startIndex = 0;
        condition.setStartIndex(startIndex);
        //查询当前页的用户
        List<Contact> byPage = dao.findByPage(condition);
        pageBean.setShowList(byPage);
        //关闭sqlSession
        sqlSession.close();
        //返回完整PageBean
        return pageBean;
    }
}
