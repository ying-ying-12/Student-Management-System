package com.example.demo7.dao;

import com.example.demo7.entity.Course;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//对课程信息进行数据库操作，包括分页查询、插入、更新、删除、计数以及根据课程编号查询等功能
public class CourseDao {
    //分页查询（page 方法）
    public PagerVO<Course> page(int current,int size,String whereSql){
        //首先查询符合条件的课程总数
        //然后根据分页参数（当前页码 current 和每页大小 size）查询当前页的课程数据
        //将查询结果封装到 PagerVO<Course> 对象中返回
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

    //插入课程信息（insert 方法）
    //使用 JdbcHelper 执行 SQL 插入语句
    //参数通过 course 对象获取，包括课程编号、教师编号、课程名称等
    public int insert(Course course){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_course values(?,?,?,?,?,?,?,?)",
                course.getCno(),course.getTno(),course.getCname()
                ,course.getBegindate(),course.getEnddate(),
                course.getCredits(),course.getLimi(),course.getCount()
        );
        return res;
    }

    //根据课程编号更新课程信息（update 方法）
    //动态拼接 SQL 更新语句，根据 course 对象的属性判断哪些字段需要更新
    //使用 params 列表存储 SQL 参数
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

    //根据课程编号删除课程记录
    //使用 JdbcHelper 执行 SQL 删除语句
    //参数为课程编号 cno
    public int delete(String cno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_course where cno = ?",cno);
        helper.closeDB();
        return res;
    }

    //统计符合条件的课程数量
    //执行 SQL 查询语句，统计符合条件的记录数
    //提供了两个版本：带条件的 count 和无条件的 count
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

    //根据课程编号查询课程信息（getByCno 方法）
    //使用 JdbcHelper 执行 SQL 查询语句
    //将查询结果封装为 Course 对象
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

    //将 ResultSet 数据封装为 Course 对象（toEntity 方法）
    //遍历 ResultSet，将每一列的值设置到 Course 对象的属性中
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

