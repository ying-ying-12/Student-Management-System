package com.example.demo7.entity;

import java.util.Date;

//课程实体
public class Course {

    private Teacher teacher;
   private String cno;//课程编号
   private String tno;//教师编号
   private String cname;//课程名称
   private Date begindate;//课程开始日期
   private Date enddate;//课程结束日期
   private Double credits;//学分
   private Integer limi;//课程人数限制
   private Integer count;//已选人数

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Integer getLimi() {
        return limi;
    }

    public void setLimi(Integer limi) {
        this.limi = limi;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
