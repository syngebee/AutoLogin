package com.itheima.dao;

import com.itheima.pojo.Contact;
import com.itheima.pojo.PageCondition;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface ContactDao {
    @Select("select * from contact")
    List<Contact> findContactAll();

    @Insert("insert into contact(name,sex,age,address,qq,email) values(#{name},#{sex},#{age},#{address},#{qq},#{email})")
    int addContact(Contact contact);

    @Select("select * from contact where id=#{id}")
    Contact getContactById(String id);

    @Update("update contact set name=#{name},sex=#{sex},age=#{age},address=#{address},qq=#{qq},email=#{email} where id=#{id}")
    int updateContact(Contact contact);

    @Delete("delete from contact where id=#{id}")
    int deleteContactById(String id);

    @Delete("<script>delete from contact where id in " +
            "<foreach collection='array' item='id' separator=',' open='(' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteContacts(String[] ids);


    List<Contact> findByPage(PageCondition condition);

    int getContactNumber(PageCondition condition);
}
