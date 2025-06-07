package com.example.demo7.dao;



import com.example.demo7.entity.Clazz;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClazzDao {

    public PagerVO<Clazz> page(int current, int size, String whereSql){
        PagerVO<Clazz> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        JdbcHelper helper = new JdbcHelper();
        if(whereSql == null){
            whereSql = " ";
        }
        ResultSet resultSet;
        try {
            resultSet = helper.executeQuery("select count(1) from tb_clazz "+whereSql);
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);

            resultSet = helper.executeQuery("select * from tb_clazz " + whereSql + " limit " + ((current-1)*size) + "," + size);
            List<Clazz> list = new ArrayList<>();
            while (resultSet.next()){
                Clazz e = new Clazz();
                e.setClazzno(resultSet.getString("clazzno"));
                e.setName(resultSet.getString("name"));
                list.add(e);
            }
            pagerVO.setList(list);
            return pagerVO;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return null;
    }

    public int insert(Clazz clazz){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_clazz values(?,?)",clazz.getClazzno(),clazz.getName());
        helper.closeDB();
        return res;
    }

    public int update(Clazz clazz){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        //为null的属性不做更新
        String sql = "update tb_clazz set name = ? where clazzno = ? ";
        res = helper.excuteUpdate(sql,clazz.getName(),clazz.getClazzno());
        helper.closeDB();
        return res;
    }

    public Clazz getByClazzno(String clazzno){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_clazz where clazzno = ?",clazzno);
        try {
            if(resultSet.next()){
                Clazz clazz = new Clazz();
                clazz.setName(resultSet.getString("name"));
                clazz.setClazzno(resultSet.getString("clazzno"));
                return clazz;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return null;
    }



    public int delete(String clazzno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_clazz where clazzno = ?",clazzno);
        helper.closeDB();
        return res;
    }

    public int count(){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_clazz");
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

    //班级学生数量统计
    public List<Clazz> statistics(){
        String sql = "select c.clazzno,c.name, count(1) as stuCount \n" +
                "from tb_clazz c, tb_student s\n" +
                "where s.clazzno = c.clazzno\n" +
                "GROUP BY c.clazzno,c.name order by count(1) desc";
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery(sql);
        try {
            List<Clazz> list = new ArrayList<>();
            while (resultSet.next()){
                Clazz clazz = new Clazz();
                clazz.setClazzno(resultSet.getString("clazzno"));
                clazz.setName(resultSet.getString("name"));
                clazz.setStuCount(Integer.parseInt(resultSet.getString("stuCount")));
                list.add(clazz);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return null;
    }

    public List<Clazz> listAll() {
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet;
        try {
            resultSet = helper.executeQuery("select * from tb_clazz");
            List<Clazz> list = new ArrayList<>();
            while (resultSet.next()){
                Clazz e = new Clazz();
                e.setClazzno(resultSet.getString("clazzno"));
                e.setName(resultSet.getString("name"));
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
