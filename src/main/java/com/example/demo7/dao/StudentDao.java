package com.example.demo7.dao;


import com.example.demo7.entity.Student;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public PagerVO<Student> page(int current,int size,String whereSql){
        // 创建分页对象并设置当前页和每页大小
        PagerVO<Student> pagerVO = new PagerVO<>();
        //返回数据
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        //查询记录
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_student " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);//获取记录

            //查询数据
            resultSet = helper.executeQuery("select * from tb_student "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            List<Student> list = new ArrayList<>();
            while (resultSet.next()){
                Student student = toEntity(resultSet);
                list.add(student);
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

    public int insert(Student student){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_student values(?,?,?,?,?,?,?,?,?)",
                student.getSno(),student.getPassword(),student.getName(),
                student.getTele(),student.getEnterdate(),student.getAge(),
                student.getGender(),student.getAddress(),student.getClazzno()
        );
        return res;
    }

    // 动态构建SQL语句，只更新非空字段
    public int update(Student student){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_student set ";
        List<Object> params = new ArrayList<>();
        if(student.getPassword()!=null){
            sql += "password = ?,";
            params.add(student.getPassword());
        }
        if(student.getName()!=null){
            sql += "name = ?,";
            params.add(student.getName());
        }
        if(student.getTele()!=null){
            sql += "tele = ?,";
            params.add(student.getTele());
        }if(student.getEnterdate()!=null){
            sql += "enterdate = ?,";
            params.add(student.getEnterdate());
        }if(student.getAge()!=null){
            sql += "age = ?,";
            params.add(student.getAge());
        }if(student.getGender()!=null){
            sql += "gender = ?,";
            params.add(student.getGender());
        }if(student.getAddress()!=null){
            sql += "address = ?,";
            params.add(student.getAddress());
        }if(student.getClazzno()!=null){
            sql += "clazzno = ?,";
            params.add(student.getClazzno());
        }
        // 移除最后一个逗号并添加条件子句
        sql = sql.substring(0,sql.length() - 1);
        sql += " where sno = '"+student.getSno() + "'";
        System.out.println(sql);
        res = helper.excuteUpdate(sql,params.toArray());
        helper.closeDB();
        return res;
    }

    public int delete(String sno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_student where sno = ?",sno);
        helper.closeDB();
        return res;
    }

    public int count(String whereSql){
        if(whereSql == null){
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_student" + whereSql);
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

    //学生统计
    public int count(){
        return count("");
    }

    public Student getBySno(String sno){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_student where sno = ?", sno);
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
//将数据库结果集转换为学生实体对象
    public Student toEntity(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setSno(resultSet.getString("sno"));
        student.setPassword(resultSet.getString("password"));
        student.setName(resultSet.getString("name"));
        student.setTele(resultSet.getString("tele"));
        student.setEnterdate(resultSet.getDate("enterdate"));
        student.setAge(resultSet.getInt("age"));
        student.setGender(resultSet.getString("gender"));
        student.setAddress(resultSet.getString("address"));
        student.setClazzno(resultSet.getString("clazzno"));
        return student;
    }

}

