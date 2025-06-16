package com.example.demo7.servlet;

import com.example.demo7.entity.Course;
import com.example.demo7.entity.StuCou;
import com.example.demo7.entity.Student;
import com.example.demo7.service.CourseService;
import com.example.demo7.service.StuCouService;
import com.example.demo7.service.StudentService;
import com.example.demo7.utils.ApiResult;
import com.example.demo7.utils.MyUtils;
import com.example.demo7.utils.PagerVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/stucou")
public class StuCouServlet extends HttpServlet {
    StuCouService stucouService = new StuCouService();
    StudentService studentService = new StudentService();
    CourseService courseService = new CourseService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //查询
        String r = req.getParameter("r");
        if(r == null){
            String current = req.getParameter("current");
            if(current==null){
                current = "1";
            }
            String sno = req.getParameter("sno");
            String cno = req.getParameter("cno");
            String chosetime1 = req.getParameter("chosetime1");
            String chosetime2 = req.getParameter("chosetime2");

            PagerVO<StuCou> pagerVO = stucouService.page(Integer.parseInt(current),10,sno,cno,chosetime1,chosetime2);
            pagerVO.init();

            //每个选课记录设置学生名和课程名
            for (StuCou stuCou: pagerVO.getList()){
                Student student =studentService.getBySno(stuCou.getSno());
                if (student!=null){
                    stuCou.setSname(student.getName());
                }
                Course course =courseService.getByCno(stuCou.getCno());
                if (course!=null){
                    stuCou.setCname(course.getCname());
                }
            }
//            req.setAttribute("now",new Date());
            req.setAttribute("sno",sno);
            req.setAttribute("cno",cno);
            req.setAttribute("chosetime1",chosetime1);
            req.setAttribute("chosetime2",chosetime2);
            req.setAttribute("pagerVO",pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/stucou-list.jsp").forward(req,resp);
        }

       //编辑,只有教师有
        if("edit".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"teacher");
            if(!hasPermission){
                return;
            }
            //根据学生号和课程号获取选课记录
            String sno = req.getParameter("sno");
            String cno = req.getParameter("cno");
            StuCou stuCou = stucouService.getBySnoCno(sno,cno);
            Student student = studentService.getBySno(stuCou.getSno());
            if (student!=null){
                stuCou.setSname(student.getName());
            }
            Course course =courseService.getByCno(stuCou.getCno());
            if (course!=null){
                stuCou.setCname(course.getCname());
            }
            req.setAttribute("entity",stuCou);
            req.getRequestDispatcher("/WEB-INF/views/stucou-edit.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //新增 修改 删除
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

        String r = req.getParameter("r");
        if("add".equals(r) || "edit".equals(r)) {

            //新增选课
            if("add".equals(r)){
                boolean hasPermission = MyUtils.hasPermission(req,resp,true,"student");
                if(!hasPermission){
                    return;
                }
                Student student = (Student) req.getSession().getAttribute("user");
                String msg = stucouService.courseChoose(student.getSno(),req.getParameter("cno"));
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"选课成功！"));
                    return;
                }
            }else{
                //教师打分
                boolean hasPermission = MyUtils.hasPermission(req,resp,true,"teacher");
                if(!hasPermission){
                    return;
                }
                //编辑页面
                StuCou stucou = new StuCou();
                stucou.setSno(req.getParameter("sno"));
                stucou.setCno(req.getParameter("cno"));
                stucou.setScore(Double.parseDouble(req.getParameter("score")));
                stucou.setEvaluation(req.getParameter("evaluation"));
                String msg = stucouService.update(stucou);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"打分成功！"));
                    return;
                }
            }
        }else{
            //删除选课,只有学生可以
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"student");
            if(!hasPermission){
                return;
            }
            //删除
            Student student = (Student) req.getSession().getAttribute("user");
            String cno = req.getParameter("cno");
            String res = stucouService.delete(student.getSno(),cno);
            if(res != null){
                resp.getWriter().write(ApiResult.json(false,res));
                return;
            }else{
                resp.getWriter().write(ApiResult.json(true,"退选成功！"));
                return;
            }
        }
    }
}