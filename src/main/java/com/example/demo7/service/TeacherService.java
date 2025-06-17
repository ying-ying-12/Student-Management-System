package com.example.demo7.service;

import com.example.demo7.dao.TeacherDao;
import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Teacher;
import com.example.demo7.utils.PagerVO;

import java.util.List;

public class TeacherService {

    // 创建数据访问对象实例，用于调用数据库操作方法
    TeacherDao dao = new TeacherDao();
    //新增教师
    public String insert(Teacher teacher){
        // 验证信息是否为空
        if(teacher.getTno() == null || teacher.getTno().equals("")){
            return "教师编号不可为空";
        }if(teacher.getPassword() == null || teacher.getPassword().equals("")){
            return "密码不可为空";
        }if(teacher.getTname() == null || teacher.getTname().equals("")){
            return "姓名不可为空";
        }
        //是否存在相同编号的教师
        Teacher ex = dao.getByTno(teacher.getTno());
        if(ex !=null){
            return "教师编号已存在！";
        }
        dao.insert(teacher);
        return null;
    }
    //更新
    public String update(Teacher teacher){
        if(teacher.getTno() == null || teacher.getTno().equals("")){
            return "被修改的教师编号不可为空";
        }
        //成功
        dao.update(teacher);
        return null;
    }

    public Teacher getByTno(String tno){
        return dao.getByTno(tno);
    }

    public int count(){
        // 直接调用DAO层方法统计并返回总数
        return dao.count();// 直接调用DAO层方法统计并返回总数
    }

    public PagerVO<Teacher> page(int current,int size,String tno,String tname){
        String whereSql = " where 1=1 ";
        //根据教师编号和教师名查询
        if(tno!=null && !"".equals(tno)){
            whereSql += " and tno like '%" + tno + "%'";
        }
        if(tname!=null && !"".equals(tname)){
            whereSql += " and tname like '%" + tname + "%'";
        }

        return dao.page(current,size,whereSql);
    }
    public int delete(String tno){
        return dao.delete(tno);
    }

    public List<Teacher> listAll() {
        return dao.ListAll();
    }
}
