package com.example.demo7.dao;

import com.example.demo7.entity.StuCou;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StuCouDao {

    public PagerVO<StuCou> page(int current,int size,String whereSql){
        PagerVO<StuCou> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_stucou " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);
            // select * from  tb_stucou where .. limit 10,10
            resultSet = helper.executeQuery("select * from tb_stucou "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            List<StuCou> list = new ArrayList<>();
            while (resultSet.next()){
                StuCou stucou = toEntity(resultSet);
                list.add(stucou);
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

    public int insert(StuCou stucou){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_stucou values(?,?,?,?,?,?,?,?,?)",
                stucou.getCno(),stucou.getSno(),stucou.getChosetime(),
                stucou.getScore(),stucou.getEvaluation());
        return res;
    }

    /**
     * StuCou 里面有属性为null的话，就忽视，不是null的话，就加入更新的sql语句，进行更新
     * @param stucou
     * @return
     */
    public int update(StuCou stucou){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_stucou set ";
        List<Object> params = new ArrayList<>();
        //根据课程号和学生号去更新
        if(stucou.getChosetime()!=null){
            sql += "chosetime = ?,";
            params.add(stucou.getChosetime());
        }
        if(stucou.getScore()!=null){
            sql += "score = ?,";
            params.add(stucou.getScore());
        }
        if(stucou.getEvaluation()!=null){
            sql += "evaluation = ?,";
            params.add(stucou.getEvaluation());
        }
        sql = sql.substring(0,sql.length() - 1);
        sql += " where sno = '"+stucou.getSno() + "'and cno='"+stucou.getCno()+"'";
        System.out.println(sql);
        res = helper.excuteUpdate(sql,params.toArray());
        helper.closeDB();
        return res;
    }

    //退选
    public int delete(String sno,String cno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_stucou where sno = ? and cno =?",sno,cno);
        helper.closeDB();
        return res;
    }

    public int count(String whereSql){
        if(whereSql == null){
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_stucou" + whereSql);
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

    public StuCou getBySnoCno(String sno,String cno){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_stucou where sno = ? and cno =?", sno,cno);
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

    public StuCou toEntity(ResultSet resultSet) throws SQLException {
        StuCou stucou = new StuCou();
        stucou.setSno(resultSet.getString("sno"));
        stucou.setCno(resultSet.getString("cno"));
        stucou.setChosetime(resultSet.getDate("chosetime("));
        stucou.setScore(resultSet.getDouble("score"));
        stucou.setEvaluation(resultSet.getString("evaluation"));
        return stucou;
    }

}

