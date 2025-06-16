package com.example.demo7.service;

import com.example.demo7.dao.CourseDao;
import com.example.demo7.dao.StuCouDao;
import com.example.demo7.entity.Course;
import com.example.demo7.entity.StuCou;
import com.example.demo7.utils.PagerVO;

import java.util.Date;

public class StuCouService {

    StuCouDao dao = new StuCouDao();
    CourseDao courseDao = new CourseDao();
   /*选课*/
    public String courseChoose(String sno,String cno){
        Date now=new Date();
        // 验证信息是否为空
        if(sno == null || sno.equals("")){
            return "选课学生不可为空";
        }if(cno == null || cno.equals("")){
            return "选课课程不可为空";
        }
        //校验课程是否被选择
        Course byCno = courseDao.getByCno(cno);
        if (byCno.getCount()>=byCno.getLimi()){
            return byCno.getCname() + ":当前课程已经被选满了！";
        }
        //选课时间范围
        if (!(now.before(byCno.getEnddate()) && now.after(byCno.getBegindate()))){
            return "不在选课时间范围";
        }
        //是否重复选课的学生！
        StuCou ex = dao.getBySnoCno(sno,cno);
        if(ex !=null){
            return "重复选课！";
        }
        //保存选课记录
        StuCou stuCou = new StuCou();
        stuCou.setSno(sno);
        stuCou.setCno(cno);
        stuCou.setChosetime(now);
        stuCou.setEvaluation("");//评价默认为空
        stuCou.setScore(0.0);//分数默认为零
        dao.insert(stuCou);

        byCno.setCount(byCno.getCount()+1);
        courseDao.update(byCno);
        return null;
    }
    //更新
    public String update(StuCou stucou){
        if(stucou == null || stucou.getSno().equals("")){
            return "选课学生不可为空";
        }if(stucou.getCno().equals("")){
            return "选课课程不可为空";
        }
        stucou.setChosetime(null);//选课时间为空，不更新
        dao.update(stucou);
        return null;//成功
    }

    public int count(){
        return dao.count();// 直接调用DAO层方法统计并返回总数
    }

    public PagerVO<StuCou> page(int current,int size,String sno,String cno,String chosetime1,String chosetime2){
        String whereSql = " where 1=1 ";
        //拼接
        if(sno!=null && !"".equals(sno)){
            whereSql += " and sno '" + sno + "'";
        }
        if(cno!=null && !"".equals(cno)){
            whereSql += " and cno = '" + cno + "'";
        }
        if(chosetime1!=null && !"".equals(chosetime1)){
            whereSql += " and chosetime >= '" + chosetime1 + "'";
        }
        if(chosetime2!=null && !"".equals(chosetime2)){
            whereSql += " and chosetime <= '" + chosetime2 + "'";
        }
        return dao.page(current,size,whereSql);
    }
    //删除
    public String delete(String sno,String cno){
        Course byCno = courseDao.getByCno(cno);
        if (!new Date().before(byCno.getEnddate())){
            return "当前时间不可退选";
        }
        int delete = dao.delete(sno,cno);
        if (delete>0){
            byCno.setCount(byCno.getCount()-1);//选课后课程数量减一
            courseDao.update(byCno);
        }
        return null;
    }

    public StuCou getBySnoCno(String sno, String cno) {

        return dao.getBySnoCno(sno,cno);
    }
}