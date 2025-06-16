package com.example.demo7.dao;

import com.example.demo7.entity.Course;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public PagerVO<Course> page(int current,int size,String whereSql){
        PagerVO<Course> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_course " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);
            // select * from  tb_course where  limit 10,10
            resultSet = helper.executeQuery("select * from tb_course "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            List<Course> list = new ArrayList<>();
            while (resultSet.next()){
                Course course = toEntity(resultSet);
                list.add(course);
            }
            pagerVO.setList(list);
            return pagerVO;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return pagerVO;
    }

    public int insert(Course course){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_course values(?,?,?,?,?,?,?,?)",
                course.getCno(),course.getTno(),course.getCname()
                ,course.getBegindate(),course.getEnddate(),
                course.getCredits(),course.getLimi(),course.getCount()
        );
        return res;
    }

    /**
     * Course 里面有属性为null的话，就忽视，不是null的话，就加入更新的sql语句，进行更新
     * @param course
     * @return
     */
    public int update(Course course){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_course set ";
        List<Object> params = new ArrayList<>();

        if(course.getTno()!=null){
            sql += "tno = ?,";
            params.add(course.getTno());
        }
        if(course.getCname()!=null){
            sql += "cname = ?,";
            params.add(course.getCname());
        }
        if(course.getBegindate()!=null){
            sql += "begindate= ?,";
            params.add(course.getBegindate());
        }if(course.getEnddate()!=null){
            sql += "enddate = ?,";
            params.add(course.getEnddate());
        }if(course.getCredits()!=null){
            sql += "credits = ?,";
            params.add(course.getCredits());
        }if(course.getLimi()!=null){
            sql += "limi = ?,";
            params.add(course.getLimi());
        }if(course.getCount()!=null){
            sql += "count = ?,";
            params.add(course.getCount());
        }

        sql = sql.substring(0,sql.length() - 1);
        sql += " where cno = '"+course.getCno() + "'";
        System.out.println(sql);
        res = helper.excuteUpdate(sql,params.toArray());
        helper.closeDB();
        return res;
    }

    public int delete(String cno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_course where cno = ?",cno);
        helper.closeDB();
        return res;
    }

    public int count(String whereSql){
        if(whereSql == null){
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_course" + whereSql);
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return 0;
    }

    public int count(){
        return count("");
    }

    public Course getByCno(String cno){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_course where cno = ?", cno);
        try {
            if(resultSet.next()){
                return toEntity(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            helper.closeDB();
        }
        return null;
    }

    public Course toEntity(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setCno(resultSet.getString("cno"));
        course.setTno(resultSet.getString("tno"));
        course.setCname(resultSet.getString("cname"));
        course.setBegindate(resultSet.getDate("begindate"));
        course.setEnddate(resultSet.getDate("enddate"));
        course.setCredits(resultSet.getDouble("credits"));
        course.setLimi(resultSet.getInt("limi"));
        course.setCount(resultSet.getInt("count"));
        return course;
    }

}

