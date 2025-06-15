package com.example.demo7.dao;

import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Teacher;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao {

    public PagerVO<Teacher> page(int current,int size,String whereSql){
        PagerVO<Teacher> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_teacher " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);
            // select * from  tb_teacher where .. limit 10,10
            resultSet = helper.executeQuery("select * from tb_teacher "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            List<Teacher> list = new ArrayList<>();
            while (resultSet.next()){
                Teacher teacher = toEntity(resultSet);
                list.add(teacher);
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

    public int insert(Teacher teacher){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_teacher values(?,?,?)",
                teacher.getTno(),teacher.getPassword(),teacher.getTname()
        );
        return res;
    }

    /**
     * Teacher 里面有属性为null的话，就忽视，不是null的话，就加入更新的sql语句，进行更新
     * @param teacher
     * @return
     */
    public int update(Teacher teacher){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_teacher set ";
        List<Object> params = new ArrayList<>();
        if(teacher.getPassword()!=null){
            sql += "password = ?,";
            params.add(teacher.getPassword());
        }
        if(teacher.getTname()!=null){
            sql += "tname = ?,";
            params.add(teacher.getTname());
        }

        sql = sql.substring(0,sql.length() - 1);
        sql += " where tno = '"+teacher.getTno() + "'";
        System.out.println(sql);
        res = helper.excuteUpdate(sql,params.toArray());
        helper.closeDB();
        return res;
    }

    public int delete(String tno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_teacher where tno = ?",tno);
        helper.closeDB();
        return res;
    }

    public int count(String whereSql){
        if(whereSql == null){
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_teacher" + whereSql);
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

    public Teacher getByTno(String tno){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_teacher where tno = ?", tno);
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

    public Teacher toEntity(ResultSet resultSet) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTno(resultSet.getString("tno"));
        teacher.setPassword(resultSet.getString("password"));
        teacher.setTname(resultSet.getString("tname"));
        return teacher;
    }

    public List<Teacher> ListAll() {
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet;
        try {
            resultSet = helper.executeQuery("select * from tb_teacher");
            List<Teacher> list = new ArrayList<>();
            while (resultSet.next()){
                Teacher e = new Teacher();
                e.setTno(resultSet.getString("tno"));
                e.setTname(resultSet.getString("tname"));
                e.setPassword(resultSet.getString("password"));
                list.add(e);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return null;
    }
}
