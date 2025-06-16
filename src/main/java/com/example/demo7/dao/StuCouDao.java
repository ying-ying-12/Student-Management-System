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
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_stu_cou " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);
            resultSet = helper.executeQuery("select * from tb_stu_cou "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            List<StuCou> list = new ArrayList<>();
            while (resultSet.next()){
                StuCou stu_cou = toEntity(resultSet);
                list.add(stu_cou);
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

    public int insert(StuCou stu_cou){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_stu_cou values(?,?,?,?,?)",
                stu_cou.getCno(),stu_cou.getSno(),stu_cou.getChosetime(),
                stu_cou.getScore(),stu_cou.getEvaluation());
        return res;
    }

    public int update(StuCou stu_cou){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_stu_cou set ";
        List<Object> params = new ArrayList<>();
        //根据课程号和学生号去更新
        if(stu_cou.getChosetime()!=null){
            sql += "chosetime = ?,";
            params.add(stu_cou.getChosetime());
        }
        if(stu_cou.getScore()!=null){
            sql += "score = ?,";
            params.add(stu_cou.getScore());
        }
        if(stu_cou.getEvaluation()!=null){
            sql += "evaluation = ?,";
            params.add(stu_cou.getEvaluation());
        }
        sql = sql.substring(0,sql.length() - 1);
        sql += " where sno = '"+stu_cou.getSno() + "'and cno='"+stu_cou.getCno()+"'";
        System.out.println(sql);
        res = helper.excuteUpdate(sql,params.toArray());
        helper.closeDB();
        return res;
    }

    //退选
    public int delete(String sno,String cno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_stu_cou where sno = ? and cno =?",sno,cno);
        helper.closeDB();
        return res;
    }

    public int count(String whereSql){
        if(whereSql == null){
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_stu_cou" + whereSql);
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
        ResultSet resultSet = helper.executeQuery("select * from tb_stu_cou where sno = ? and cno =?", sno,cno);
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
        StuCou stu_cou = new StuCou();
        stu_cou.setSno(resultSet.getString("sno"));
        stu_cou.setCno(resultSet.getString("cno"));
        stu_cou.setChosetime(resultSet.getDate("chosetime"));
        stu_cou.setScore(resultSet.getDouble("score"));
        stu_cou.setEvaluation(resultSet.getString("evaluation"));
        return stu_cou;
    }

}

