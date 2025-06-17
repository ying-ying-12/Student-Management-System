package com.example.demo7.service;

import com.example.demo7.dao.CourseDao;
import com.example.demo7.entity.Course;
import com.example.demo7.utils.PagerVO;
//实现了课程的全生命周期管理，包括：
//
//新增课程：验证数据合法性后插入数据库
//更新课程：修改已有课程信息
//查询课程：按课程号查询单条记录
//分页查询：支持条件筛选的课程列表分页
//统计计数：获取课程总数
//删除课程：从数据库删除课程记录
public class CourseService {

    CourseDao dao = new CourseDao();// 创建数据访问对象实例，用于调用数据库操作方法

    //新增
    public String insert(Course course){
        // 验证信息是否为空
        if(course.getCno() == null || course.getCno().equals("")){
            return "课程编号不可为空";
        }if(course.getCname() == null || course.getCname().equals("")){
            return "课程名不可为空";
        }if(course.getBegindate() == null || course.getEnddate() == null){
            return "开始和结束时间不可为空";
        }if(course.getLimi() == null){
            return "限制人数不可为空";
        }

        Course ex = dao.getByCno(course.getCno());
        if(ex !=null){
            return "课程编号已存在！";
        }
        dao.insert(course);
        return null;
    }
    //更新
    public String update(Course course){
        if(course.getCno() == null || course.getCno().equals("")){
            return "被修改的课程编号不可为空";
        }
        dao.update(course);
        return null;//成功
    }

    public Course getByCno(String cno){

        return dao.getByCno(cno);
    }

    public int count(){

        return dao.count();// 直接调用DAO层方法统计并返回总数
    }

    public PagerVO<Course> page(int current,int size,String cno,String cname,String tno){
        String whereSql = " where 1=1 ";
        //拼接
        if(cno!=null && !"".equals(cno)){
            whereSql += " and cno like '%" + cno + "%'";
        }
        if(cno!=null && !"".equals(tno)){
            whereSql += " and tno = '" + tno + "'";
        }
        if(cname!=null && !"".equals(cname)){
            whereSql += " and cname like '%" + cname + "%'";
        }

        return dao.page(current,size,whereSql);
    }
    public int delete(String cno){
        return dao.delete(cno);
    }
}
