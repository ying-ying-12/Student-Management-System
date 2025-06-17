package com.example.demo7.dao;

import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Teacher;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//对教师信息进行数据库操作
public class TeacherDao {

    //用page方法实现分页查询功能
    public PagerVO<Teacher> page(int current,int size,String whereSql){
        PagerVO<Teacher> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);
        JdbcHelper helper = new JdbcHelper();
        //首先查询符合条件的教师总数
        //然后根据分页参数（当前页码 current 和每页大小 size）查询当前页的教师数据
        //whereSql 参数用于传递额外的查询条件
        //分页查询的 SQL 使用了 limit 子句
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_teacher " + whereSql);
        try {
            resultSet.next();
            int total = resultSet.getInt(1);
            pagerVO.setTotal(total);
                        resultSet = helper.executeQuery("select * from tb_teacher "
                    + whereSql + "limit " + ((current-1)*size)+ ","+size);
            //分页
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

    //管理员插入教师
    public int insert(Teacher teacher){
        //使用 JdbcHelper 执行 SQL 插入语句
        JdbcHelper helper = new JdbcHelper();
//        参数通过 teacher 对象获取，包括教师编号、密码和姓名
        int res = helper.excuteUpdate("insert into tb_teacher values(?,?,?)",
                teacher.getTno(),teacher.getPassword(),teacher.getTname()
        );
        return res;
    }

    //用update更新教师信息
    //动态拼接 SQL 更新语句，根据 teacher 对象的属性判断哪些字段需要更新
    ////使用 params 列表存储 SQL 参数。
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

    //根据教师编号删除教师
    //使用 JdbcHelper 执行 SQL 删除语句
    public int delete(String tno){
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_teacher where tno = ?",tno);
        helper.closeDB();
        return res;
    }

    //count方法统计符合条件的教师数量
    //执行 SQL 查询语句，统计符合条件的记录数
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

    // 统计所有教师数量
    public int count(){
        return count("");
    }

    //根据教师编号查询教师信息（getByTno方法）
    //使用 JdbcHelper 执行 SQL 查询语句，将查询结果封装为 Teacher 对象
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

    // 将结果集转换为Teacher对象
    public Teacher toEntity(ResultSet resultSet) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTno(resultSet.getString("tno"));
        teacher.setPassword(resultSet.getString("password"));
        teacher.setTname(resultSet.getString("tname"));
        return teacher;
    }

    //查询所有教师信息（ListAll方法）
    //执行SQL查询语句，将结果封装为Teacher对象列表
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
