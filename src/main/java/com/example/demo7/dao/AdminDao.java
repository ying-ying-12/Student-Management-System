package com.example.demo7.dao;
//数据访问对象，访问数据库相关的内容
import com.example.demo7.entity.Admin;
import com.example.demo7.utils.JdbcHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
//根据用户名查询管理员信息
    public Admin getByUsername(String username){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_admin where username = ?", username);
        try {
            if(resultSet.next()){
                Admin admin = new Admin();
                admin.setUsername(resultSet.getString("username"));
                admin.setPassword(resultSet.getString("password"));
                return admin;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            helper.closeDB();
        }
        return null;
    }

}
