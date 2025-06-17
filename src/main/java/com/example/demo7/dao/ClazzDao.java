package com.example.demo7.dao;

import com.example.demo7.entity.Clazz;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级数据访问对象，负责与数据库中的班级表进行交互
 * 提供班级信息的增删改查以及统计功能
 */
public class ClazzDao {

    /**
     * 分页查询班级信息
     * @param current 当前页码
     * @param size 每页记录数
     * @param whereSql 查询条件SQL子句
     * @return 包含分页信息和班级列表的PagerVO对象
     */
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
            // 查询总记录数
            resultSet = helper.executeQuery("select count(1) from tb_clazz "+whereSql);
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);

            // 查询当前页数据
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

    /**
     * 插入新班级记录
     * @param clazz 班级实体对象
     * @return 受影响的行数
     */
    public int insert(Clazz clazz){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("insert into tb_clazz values(?,?)",clazz.getClazzno(),clazz.getName());
        helper.closeDB();
        return res;
    }

    /**
     * 更新班级信息
     * @param clazz 班级实体对象，包含要更新的属性
     * @return 受影响的行数
     */
    public int update(Clazz clazz){
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        //为null的属性不做更新
        String sql = "update tb_clazz set name = ? where clazzno = ? ";
        res = helper.excuteUpdate(sql,clazz.getName(),clazz.getClazzno());
        helper.closeDB();
        return res;
    }

    /**
     * 根据班级编号查询班级信息
     * @param clazzno 班级编号
     * @return 班级实体对象，如果不存在则返回null
     */
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

    /**
     * 根据班级编号删除班级记录
     * @param clazzno 班级编号
     * @return 受影响的行数
     */
    public int delete(String clazzno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_clazz where clazzno = ?",clazzno);
        helper.closeDB();
        return res;
    }

    /**
     * 获取班级总数
     * @return 班级总数
     */
    public int count(){
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_clazz");
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            helper.closeDB();//释放资源
        }
        return 0;
    }

    /**
     * 统计各班级学生数量
     * @return 包含班级编号、名称和学生数量的列表
     */
    public List<Clazz> statistics(){
        //班级编号，班级名，班级人数
        String sql = "select c.clazzno, c.name, COUNT(s.sno) as stuCount \n" +
                "from tb_clazz c \n" +
                "LEFT JOIN tb_student s ON s.clazzno = c.clazzno \n" +
                "GROUP BY c.clazzno, c.name \n" +
                "ORDER BY stuCount DESC";
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery(sql);
        try {
            List<Clazz> list = new ArrayList<>();
            while (resultSet.next()){
                Clazz clazz = new Clazz();
                clazz.setClazzno(resultSet.getString("clazzno"));
                clazz.setName(resultSet.getString("name"));
                clazz.setStuCount(resultSet.getInt("stuCount"));
                list.add(clazz);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            helper.closeDB();
        }
        return new ArrayList<>(); // Return empty list instead of null
    }

    /**
     * 获取所有班级信息
     * @return 班级列表
     */
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