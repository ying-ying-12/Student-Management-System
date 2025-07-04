package com.example.demo7.service;
//首页数据的查询
import com.example.demo7.dao.ClazzDao;
import com.example.demo7.dao.StudentDao;
import com.example.demo7.entity.Clazz;
import com.example.demo7.utils.PagerVO;

import java.util.List;

/**
 * 班级业务逻辑服务类
 * 处理班级信息的增删改查业务逻辑，包含数据验证和业务规则
 */
public class ClazzService {
    // 数据访问对象：处理班级数据的数据库操作
    private final ClazzDao dao = new ClazzDao();
    // 数据访问对象：处理学生数据的数据库操作（用于班级删除时的学生检查）
    private final StudentDao studentDao = new StudentDao();
    public PagerVO<Clazz> page(int current, int size, String clazzno, String name) {
        // 初始化查询条件SQL（默认查询所有记录）
        String whereSql = " where 1=1 ";
        // 拼接班级名称模糊查询条件
        if (name != null) {
            whereSql += " and name like '%" + name + "%'";
        }
        // 拼接班级编号模糊查询条件
        if (clazzno != null) {
            whereSql += " and clazzno like '%" + clazzno + "%'";
        }
        // 调用DAO层执行分页查询
        return dao.page(current, size, whereSql);
    }

    //获取班级总数
    public int count() {
        return dao.count();
    }

    //统计各班级学生数量
    public List<Clazz> statistics() {
        return dao.statistics();
    }

    //新增班级
    public String insert(Clazz clazz) {
        // 验证班级编号非空
        if (clazz.getClazzno() == null || clazz.getClazzno().equals("")) {
            return "班级编号不可为空！";
        }
        // 验证班级名称非空
        if (clazz.getName() == null || clazz.getName().equals("")) {
            return "班级名不可为空！";
        }
        // 检查班级编号是否已存在
        Clazz exists = dao.getByClazzno(clazz.getClazzno());
        if (exists != null) {
            return "班级编号已存在！";
        }
        // 调用DAO层插入班级记录
        dao.insert(clazz);
        return null;
    }

    //更新班级信息
    public String update(Clazz clazz) {
        // 验证班级编号非空
        if (clazz.getClazzno() == null || clazz.getClazzno().equals("")) {
            return "班级编号不可为空！";
        }
        // 调用DAO层更新班级记录
        dao.update(clazz);
        return null;
    }

    //删除班级
    public String delete(String clazzno) {
        // 查询该班级下的学生数量
        int count = studentDao.count(" where clazzno = '" + clazzno + "'");
        if (count > 0) {
            return "删除失败，此班级已经有" + count + "名学生";
        }
        // 调用DAO层删除班级记录
        dao.delete(clazzno);
        return null;
    }

    //根据班级编号查询班级信息
    public Clazz getByClazzno(String clazzno) {
        return dao.getByClazzno(clazzno);
    }

    //查询所有班级信息
    public List<Clazz> listAll() {
        return dao.listAll();
    }
}