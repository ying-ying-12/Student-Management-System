package com.example.demo7.service;

import com.example.demo7.dao.CourseDao;
import com.example.demo7.entity.Course;
import com.example.demo7.utils.PagerVO;

public class CourseService {

    CourseDao dao = new CourseDao();// 创建数据访问对象实例，用于调用数据库操作方法

    //新增课程（insert 方法）
    public String insert(Course course){
        // 验证课程编号、课程名称、开始和结束时间、人数限制是否为空
        if(course.getCno() == null || course.getCno().equals("")){
            return "课程编号不可为空";
        }if(course.getCname() == null || course.getCname().equals("")){
            return "课程名不可为空";
        }if(course.getBegindate() == null || course.getEnddate() == null){
            return "开始和结束时间不可为空";
        }if(course.getLimi() == null){
            return "限制人数不可为空";
        }

        //检查是否已存在相同编号的课程
        Course ex = dao.getByCno(course.getCno());
        if(ex !=null){
            return "课程编号已存在！";
        }
        //如果验证通过，则调用 CourseDao 的 insert 方法插入记录
        dao.insert(course);
        return null;
    }
    //更新课程信息（update 方法）
    public String update(Course course){
        //验证课程编号是否为空
        if(course.getCno() == null || course.getCno().equals("")){
            return "被修改的课程编号不可为空";
        }
        //调用 CourseDao 的 update 方法进行更新
        dao.update(course);
        return null;
    }

    //根据课程编号查询课程信息（getByCno 方法）
    public Course getByCno(String cno){
        //调用 CourseDao 的 getByCno 方法
        return dao.getByCno(cno);
    }

    //统计课程总数（count 方法）
    public int count(){
        //调用 CourseDao 的 count 方法
        return dao.count();// 直接调用DAO层方法统计并返回总数
    }

    //根据课程编号、课程名称和教师编号进行分页查询（page 方法）
    public PagerVO<Course> page(int current,int size,String cno,String cname,String tno){
        String whereSql = " where 1=1 ";
        //动态拼接 whereSql 条件
        if(cno!=null && !"".equals(cno)){
            whereSql += " and cno like '%" + cno + "%'";
        }
        if(cno!=null && !"".equals(tno)){
            whereSql += " and tno = '" + tno + "'";
        }
        if(cname!=null && !"".equals(cname)){
            whereSql += " and cname like '%" + cname + "%'";
        }
        //调用 CourseDao 的 page 方法
        return dao.page(current,size,whereSql);
    }
    public int delete(String cno){
        return dao.delete(cno);
    }
}
