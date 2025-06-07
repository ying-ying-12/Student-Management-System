//首页数据的查询
package com.example.demo7.service;


import com.example.demo7.dao.ClazzDao;
import com.example.demo7.dao.StudentDao;
import com.example.demo7.entity.Clazz;
import com.example.demo7.utils.PagerVO;

import java.util.List;

public class ClazzService {

    ClazzDao dao = new ClazzDao();
    StudentDao studentDao = new StudentDao();

    public PagerVO<Clazz> page(int current, int size, String clazzno, String name) {
        String whereSql = " where 1=1 ";
        if(name!=null){
            whereSql += " and name like '%" + name + "%'";
        }
        if(clazzno!=null){
            whereSql += " and clazzno like '%" + clazzno + "%'";
        }
        return dao.page(current,size,whereSql);
    }

    public int count(){
        return dao.count();
    }

    public List<Clazz> statistics(){
        return dao.statistics();
    }

    public String insert(Clazz clazz){
        if(clazz.getClazzno() == null || clazz.getClazzno().equals("")){
            return "班级编号不可为空！";
        }
        if(clazz.getName() == null || clazz.getName().equals("")){
            return "班级名不可为空！";
        }
        //检查
        Clazz exists = dao.getByClazzno(clazz.getClazzno());
        if(exists!=null){
            return "班级编号已存在！";
        }
        dao.insert(clazz);
        return null;
    }

    public String update(Clazz clazz){
        if(clazz.getClazzno() == null || clazz.getClazzno().equals("")){
            return "班级编号不可为空！";
        }
        dao.update(clazz);
        return null;
    }


    public String delete(String clazzno){
        //查询班级里是否有学生
        int count = studentDao.count(" where clazzno = '" + clazzno + "'");
        if(count>0){
            return "删除失败，此班级已经有"+count + "人";
        }
        dao.delete(clazzno);
        //没有人，删除成功
        return null;
    }

    public Clazz getByClazzno(String clazzno) {
        return dao.getByClazzno(clazzno);
    }

    public List<Clazz> listAll() {
        return dao.listAll();
    }
}
