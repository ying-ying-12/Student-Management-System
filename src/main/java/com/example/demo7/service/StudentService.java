package com.example.demo7.service;

import com.example.demo7.dao.StudentDao;
import com.example.demo7.entity.Student;
import com.example.demo7.utils.PagerVO;

/*
 * 学生业务逻辑服务类
 * 处理学生信息的增删改查业务逻辑，包含数据验证和业务规则
 */
public class StudentService {

    // 数据访问对象：处理学生数据的数据库操作
    private final StudentDao dao = new StudentDao();

    /*
     * 新增学生
     * 学生实体对象
     *错误信息（若新增失败），null（若新增成功）
     */
    public String insert(Student student) {
        // 验证学生基本信息非空
        if (student.getSno() == null || student.getSno().equals("")) {
            return "学生学号不可为空";
        }
        if (student.getPassword() == null || student.getPassword().equals("")) {
            return "密码不可为空";
        }
        if (student.getName() == null || student.getName().equals("")) {
            return "学生姓名不可为空";
        }
        if (student.getClazzno() == null || student.getClazzno().equals("")) {
            return "班级不可为空";
        }

        // 检查学号唯一性
        Student ex = dao.getBySno(student.getSno());
        if (ex != null) {
            return "学号已存在！";
        }

        // 调用DAO层插入学生记录
        dao.insert(student);
        return null;
    }

    /*
     * 更新学生信息
     *  student 学生实体对象（包含要更新的信息）
     * 错误信息（若更新失败），null（若更新成功）
     */
    public String update(Student student) {
        // 验证学号非空（学号是更新的关键条件）
        if (student.getSno() == null || student.getSno().equals("")) {
            return "被修改的学生学号不可为空";
        }

        // 调用DAO层更新学生记录
        dao.update(student);
        return null;
    }

    /*
     * 根据学号查询学生信息
     * 学生学号
     * 学生实体对象（若存在），null（若不存在）
     */
    public Student getBySno(String sno) {
        return dao.getBySno(sno);
    }

    /*
     * 获取学生总数
     * 学生记录总数
     */
    public int count() {
        return dao.count();
    }


    public PagerVO<Student> page(int current, int size, String sno, String name, String gender, String clazzno) {
        // 初始化查询条件SQL（默认查询所有记录）
        String whereSql = " where 1=1 ";

        // 拼接查询条件（支持模糊查询）
        if (sno != null && !"".equals(sno)) {
            whereSql += " and sno like '%" + sno + "%'";
        }
        if (name != null && !"".equals(name)) {
            whereSql += " and name like '%" + name + "%'";
        }
        if (gender != null && !"".equals(gender)) {
            whereSql += " and gender = '" + gender + "'";
        }
        if (clazzno != null && !"".equals(clazzno)) {
            whereSql += " and clazzno = '" + clazzno + "'";
        }

        // 调用DAO层执行分页查询
        return dao.page(current, size, whereSql);
    }

    /*
     * 删除学生信息
     *要删除的学生学号
     * 受影响的行数（1表示成功，0表示失败）
     */
    public int delete(String sno) {
        return dao.delete(sno);
    }
}