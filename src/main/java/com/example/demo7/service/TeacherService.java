package com.example.demo7.service;

import com.example.demo7.dao.TeacherDao;
import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Teacher;
import com.example.demo7.utils.PagerVO;

import java.util.List;

//处理与教师相关的业务逻辑，并调用TeacherDao来操作数据库
public class TeacherService {

    // 创建数据访问对象实例，用于调用数据库操作方法
    TeacherDao dao = new TeacherDao();

    //新增教师
    public String insert(Teacher teacher){
        // 验证教师编号、密码和姓名是否为空
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
        //如果验证通过，调用TeacherDao的insert方法插入记录
        dao.insert(teacher);
        return null;
    }

    //更新教师信息
    public String update(Teacher teacher){
        //验证教师编号是否为空
        if(teacher.getTno() == null || teacher.getTno().equals("")){
            return "被修改的教师编号不可为空";
        }
        //调用TeacherDao的update方法进行更新
        dao.update(teacher);
        return null;
    }

    //根据教师编号查询教师信息（getByTno方法）
    //调用TeacherDao的getByTno方法
    public Teacher getByTno(String tno){
        return dao.getByTno(tno);
    }

    //统计教师总数（count方法）
    public int count(){
        //调用TeacherDao的count方法
        return dao.count();
    }

    //根据教师编号和姓名进行分页查询
    public PagerVO<Teacher> page(int current,int size,String tno,String tname){
        String whereSql = " where 1=1 ";
        if(tno!=null && !"".equals(tno)){
            //动态拼接whereSql条件
            whereSql += " and tno like '%" + tno + "%'";
        }
        if(tname!=null && !"".equals(tname)){
            whereSql += " and tname like '%" + tname + "%'";
        }
        //调用TeacherDao的page方法
        return dao.page(current,size,whereSql);
    }
    public int delete(String tno){
        return dao.delete(tno);
    }

    public List<Teacher> listAll() {
        return dao.ListAll();
    }
}
