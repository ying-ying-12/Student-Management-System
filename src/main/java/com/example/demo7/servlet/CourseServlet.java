package com.example.demo7.servlet;

import com.example.demo7.entity.Teacher;
import com.example.demo7.entity.Course;
import com.example.demo7.entity.Teacher;
import com.example.demo7.service.TeacherService;
import com.example.demo7.service.CourseService;
import com.example.demo7.service.TeacherService;
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
import java.util.HashMap;
import java.util.List;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    CourseService courseService = new CourseService();
    TeacherService teacherService = new TeacherService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //查询参数
        String r = req.getParameter("r");
        if(r == null){
            String current = req.getParameter("current");
            if(current==null){
                current = "1";
            }
            //查询条件
            String cno = req.getParameter("cno");
            String cname = req.getParameter("cname");
            String tno = req.getParameter("tno");

            PagerVO<Course> pagerVO = courseService.page(Integer.parseInt(current),10,cno,cname,tno);
            pagerVO.init();

            List<Teacher> teacheres = teacherService.listAll();
            HashMap<String,Teacher> tmap = new HashMap<>();
            for(Teacher teacher:teacheres){
                tmap.put(teacher.getTno(),teacher);
            }
            for (Course course : pagerVO.getList()) {
                course.setTeacher(tmap.get(course.getTno()));
            }

            req.setAttribute("now",new Date());
            req.setAttribute("teacheres",teacheres);
            req.setAttribute("cno",cno);
            req.setAttribute("tno",tno);
            req.setAttribute("cname",cname);
            req.setAttribute("pagerVO",pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req,resp);
        }

        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"teacher");
            if(!hasPermission){
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/course-add.jsp").forward(req,resp);
        }
        if("edit".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"teacher");
            if(!hasPermission){
                return;
            }
            String cno = req.getParameter("cno");
            Course course = courseService.getByCno(cno);
            req.setAttribute("entity",course);
            req.getRequestDispatcher("/WEB-INF/views/course-edit.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //新增 修改 删除
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

        String r = req.getParameter("r");
        if("add".equals(r) || "edit".equals(r)) {
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"teacher");
            if(!hasPermission){
                return;
            }
            Teacher teacher = (Teacher)req.getSession().getAttribute("user");
            Course course = new Course();
            course.setCno(req.getParameter("cno"));
            course.setTno(teacher.getTno());
            course.setCname(req.getParameter("cname"));
            String begindate = req.getParameter("begindate");
            course.setBegindate(MyUtils.strToDate(begindate));
            String enddate = req.getParameter("enddate");
            course.setEnddate(MyUtils.strToDate(enddate));

            String credits = req.getParameter("credits");
            course.setCredits(Double.parseDouble(credits));

            String limi = req.getParameter("limi");
            course.setLimi(Integer.parseInt(limi));

            if("add".equals(r)){
                //添加课程时人数设为0
                course.setCount(0);
                String msg = courseService.insert(course);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"保存课程成功！"));
                    return;
                }
            }else{
                String msg = courseService.update(course);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"更新课程成功！"));
                    return;
                }
            }
        }else{
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"teacher");
            if(!hasPermission){
                return;
            }
            //删除
            String cno = req.getParameter("cno");
            int res = courseService.delete(cno);
            if(res == 0){
                resp.getWriter().write(ApiResult.json(false,"删除失败，请联系管理员！"));
                return;
            }else{
                resp.getWriter().write(ApiResult.json(true,"删除成功！"));
                return;
            }
        }
    }
}
