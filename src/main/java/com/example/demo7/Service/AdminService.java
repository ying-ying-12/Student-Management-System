package com.example.demo7.Service;


import com.example.demo7.dao.AdminDao;
import com.example.demo7.entity.Admin;

public class AdminService {

    AdminDao adminDao = new AdminDao();

    public Admin getByUsername(String username){
        return adminDao.getByUsername(username);
    }

}
