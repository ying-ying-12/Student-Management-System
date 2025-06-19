package com.example.demo7.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo7.entity.Student;
import com.example.demo7.utils.JdbcHelper;
import com.example.demo7.utils.PagerVO;

/*
 * 学生数据访问对象
 * 负责与数据库中的学生表进行交互，实现学生信息的增删改查操作
 */
public class StudentDao {

    /*
     * 分页查询学生信息
     */
    public PagerVO<Student> page(int current, int size, String whereSql) {
        // 创建分页对象并设置当前页和每页大小
        PagerVO<Student> pagerVO = new PagerVO<>();
        pagerVO.setCurrent(current);
        pagerVO.setSize(size);

        // 创建 JdbcHelper 实例，用于执行 SQL 查询
        JdbcHelper helper = new JdbcHelper();
        try {
            // 查询符合条件的记录总数
            ResultSet resultSet = helper.executeQuery("select count(1) from tb_student " + whereSql);
            resultSet.next();
            int total = resultSet.getInt(1);
            // 设置总记录数
            pagerVO.setTotal(total);

            // 查询当前页的学生记录
            resultSet = helper.executeQuery("select * from tb_student "
                    + whereSql + " limit " + ((current - 1) * size) + "," + size);
            List<Student> list = new ArrayList<>();
            while (resultSet.next()) {
                Student student = toEntity(resultSet);
                list.add(student);
            }
            pagerVO.setList(list);
            return pagerVO;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接，释放资源
            helper.closeDB();
        }
        return pagerVO;
    }

    //插入新的学生记录
    public int insert(Student student) {
        // 创建 JdbcHelper 实例，用于执行 SQL 插入操作
        JdbcHelper helper = new JdbcHelper();
        // 执行插入操作，使用占位符防止SQL注入
        int res = helper.excuteUpdate("insert into tb_student values(?,?,?,?,?,?,?,?,?)",
                student.getSno(), student.getPassword(), student.getName(),
                student.getTele(), student.getEnterdate(), student.getAge(),
                student.getGender(), student.getAddress(), student.getClazzno()
        );
        helper.closeDB(); // 关闭数据库连接
        return res;
    }

    //更新学生信息，只更新非空字段
    public int update(Student student) {
        JdbcHelper helper = new JdbcHelper();
        int res = 0;
        String sql = "update tb_student set ";
        List<Object> params = new ArrayList<>();

        // 动态构建SQL语句，只添加非空字段的更新
        if (student.getPassword() != null) {
            sql += "password = ?,";
            params.add(student.getPassword());
        }
        if (student.getName() != null) {
            sql += "name = ?,";
            params.add(student.getName());
        }
        if (student.getTele() != null) {
            sql += "tele = ?,";
            params.add(student.getTele());
        }
        if (student.getEnterdate() != null) {
            sql += "enterdate = ?,";
            params.add(student.getEnterdate());
        }
        if (student.getAge() != null) {
            sql += "age = ?,";
            params.add(student.getAge());
        }
        if (student.getGender() != null) {
            sql += "gender = ?,";
            params.add(student.getGender());
        }
        if (student.getAddress() != null) {
            sql += "address = ?,";
            params.add(student.getAddress());
        }
        if (student.getClazzno() != null) {
            sql += "clazzno = ?,";
            params.add(student.getClazzno());
        }

        // 移除最后一个逗号，并添加查询条件
        if (sql.endsWith(",")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        sql += " where sno = '" + student.getSno() + "'";
        System.out.println(sql); // 打印SQL语句，用于调试
        res = helper.excuteUpdate(sql, params.toArray());
        helper.closeDB();
        return res;
    }

    /*
     * 删除学生记录
     */
    public int delete(String sno) {
        JdbcHelper helper = new JdbcHelper();
        int res = helper.excuteUpdate("delete from tb_student where sno = ?", sno);
        helper.closeDB();
        return res;
    }

    /*
     * 统计学生数量（带查询条件）
     */
    public int count(String whereSql) {
        if (whereSql == null) {
            whereSql = "";
        }
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select count(1) from tb_student" + whereSql);
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            helper.closeDB();
        }
        return 0;
    }

    /*
     * 统计所有学生数量
     */
    public int count() {
        return count("");
    }

    //根据学号查询学生信息
    public Student getBySno(String sno) {
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_student where sno = ?", sno);
        try {
            if (resultSet.next()) {
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
        // 从结果集中获取数据并设置到学生实体对象中
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