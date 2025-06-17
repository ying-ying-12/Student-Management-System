package com.example.demo7.service;

import com.example.demo7.dao.StudentDao;
import com.example.demo7.entity.Student;
import com.example.demo7.utils.PagerVO;

public class StudentService {

    // 创建 StudentDao 实例，用于调用数据访问方法
    // 创建数据访问对象实例，用于调用数据库操作方法
    StudentDao dao = new StudentDao();

    //新增
    public String insert(Student student){
        // 验证信息是否为空
        if(student.getSno() == null || student.getSno().equals("")){
            return "学生学号不可为空";
        }if(student.getPassword() == null || student.getPassword().equals("")){
            return "密码不可为空";
        }if(student.getName() == null || student.getName().equals("")){
            return "学生姓名不可为空";
        }if(student.getClazzno() == null || student.getClazzno().equals("")){
            return "班级不可为空";
        }
        //是否存在相同学号的学生！
        Student ex = dao.getBySno(student.getSno());
        if(ex !=null){
            return "学号已存在！";
        }
        // 调用 StudentDao 的 insert 方法插入学生信息
        dao.insert(student);
        return null;
    }


    // 验证被修改的学生学号是否为空
    public String update(Student student){
        if(student.getSno() == null || student.getSno().equals("")){
            return "被修改的学生学号不可为空";
        }
        // 调用 StudentDao 的 update 方法更新学生信息
        dao.update(student);
        return null;//成功
    }

    public Student getBySno(String sno){

        return dao.getBySno(sno);
    }

    public int count(){

        return dao.count();// 直接调用DAO层方法统计并返回总数
    }

    public PagerVO<Student> page(int current,int size,String sno,String name,String gender,String clazzno){
        // 初始化查询条件 SQL 语句
        String whereSql = " where 1=1 ";
        // 根据传入的查询条件拼接 SQL 语句
        //模糊查询
        if(sno!=null && !"".equals(sno)){
            whereSql += " and sno like '%" + sno + "%'";
        }
        if(name!=null && !"".equals(name)){
            whereSql += " and name like '%" + name + "%'";
        }
        if(gender!=null && !"".equals(gender)){
            whereSql += " and gender = '" + gender + "'";
        }
        if(clazzno!=null && !"".equals(clazzno)){
            whereSql += " and clazzno = '" + clazzno + "'";
        }
        // 调用 StudentDao 的 page 方法进行分页查询
        return dao.page(current,size,whereSql);
    }

    //删除功能
    public int delete(String sno){
        return dao.delete(sno);
    }
}