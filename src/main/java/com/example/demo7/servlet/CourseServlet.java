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
import java.text.ParseException;
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
        //根据请求参数 r 的值，决定执行的操作
        String r = req.getParameter("r");
        if(r == null){
            String current = req.getParameter("current");
            if(current==null){
                current = "1";
            }
            //获取分页参数（当前页码 current、课程编号 cno、课程名称 cname 和教师编号 tno）
            String cno = req.getParameter("cno");
            String cname = req.getParameter("cname");
            String tno = req.getParameter("tno");

            //调用 CourseService 的 page 方法获取分页数据
            PagerVO<Course> pagerVO = courseService.page(Integer.parseInt(current),10,cno,cname,tno);
            pagerVO.init();

            //查询所有教师信息，并将教师信息与课程信息关联
            List<Teacher> teacheres = teacherService.listAll();
            HashMap<String,Teacher> tmap = new HashMap<>();
            for(Teacher teacher:teacheres){
                tmap.put(teacher.getTno(),teacher);
            }
            for (Course course : pagerVO.getList()) {
                course.setTeacher(tmap.get(course.getTno()));
            }

            //将分页数据和查询参数设置到请求属性中，并转发到 course-list.jsp 页面
            req.setAttribute("teacheres",teacheres);
            req.setAttribute("cno",cno);
            req.setAttribute("tno",tno);
            req.setAttribute("cname",cname);
            req.setAttribute("pagerVO",pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/course-list.jsp").forward(req,resp);
        }

        //添加课程页面
        //检查用户是否有权限（通过 MyUtils.hasPermission 方法）,如果有权限，跳转到 course-add.jsp 页面
        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"teacher");
            if(!hasPermission){
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/course-add.jsp").forward(req,resp);
        }

        //编辑课程页面
        //检查用户是否有权限
        //根据课程编号查询课程信息，并将课程对象设置到请求属性中，然后跳转到 course-edit.jsp 页面
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

    //doPost 方法:主要用于新增、修改和删除课程信息
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

        //根据请求参数 r 的值，决定执行的操作
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
            try {
                course.setBegindate(MyUtils.strToDate(begindate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String enddate = req.getParameter("enddate");
            try {
                course.setEnddate(MyUtils.strToDate(enddate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            String credits = req.getParameter("credits");
            course.setCredits(Double.parseDouble(credits));

            String limi = req.getParameter("limi");
            course.setLimi(Integer.parseInt(limi));

            //创建 Course 对象并设置属性
            //设置教师编号为当前登录教师的编号。
            //调用 CourseService 的 insert 方法插入课程信息
            //根据返回值返回 JSON 格式的响应
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
                //修改课程
                //创建 Course 对象并设置属性
                //调用 CourseService 的 update 方法更新课程信息
                //根据返回值返回 JSON 格式的响应
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
            //删除课程
            //获取课程编号，调用 CourseService 的 delete 方法删除课程信息
            //根据返回值返回 JSON 格式的响应
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