package com.example.demo7.servlet;

import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Teacher;
import com.example.demo7.service.ClazzService;
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
import java.util.HashMap;
import java.util.List;

//处理与教师相关的 HTTP 请求
@WebServlet("/teacher")
public class TeacherServlet extends HttpServlet {
    TeacherService teacherService = new TeacherService();

    @Override
    //doGet 方法，处理 HTTP GET 请求，主要用于分页查询、添加和编辑页面的跳转
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //根据请求参数r的值，决定执行的操作
        String r = req.getParameter("r");
        //获取分页参数（当前页码 current,教师编号 tno ,教师姓名 tname）
        if(r == null){
            String current = req.getParameter("current");
            if(current==null){
                current = "1";
            }
            String tno = req.getParameter("tno");
            String tname = req.getParameter("tname");

            //调用 TeacherService 的 page 方法获取分页数据
            PagerVO<Teacher> pagerVO = teacherService.page(Integer.parseInt(current),10,tno,tname);
            pagerVO.init();

            //将分页数据和查询参数设置到请求属性中，并转发到 teacher-list.jsp 页面
            req.setAttribute("tno",tno);
            req.setAttribute("tname",tname);
            req.setAttribute("pagerVO",pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/teacher-list.jsp").forward(req,resp);
        }

        //添加教师页面
        //检查用户是否有权限（MyUtils.hasPermission方法），如果有权限，跳转到 teacher-add.jsp 页面
        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/teacher-add.jsp").forward(req,resp);
        }

        //编辑教师页面
        //根据教师编号查询教师信息，并将教师对象设置到请求属性中，然后跳转到 teacher-edit.jsp 页面
        if("edit".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            String tno = req.getParameter("tno");
            Teacher teacher = teacherService.getByTno(tno);
            req.setAttribute("entity",teacher);
            req.getRequestDispatcher("/WEB-INF/views/teacher-edit.jsp").forward(req,resp);
        }

    }

    //doPost 方法：处理 HTTP POST 请求，主要用于新增、修改和删除教师信息
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

        //根据请求参数 r 的值，决定执行的操作
        String r = req.getParameter("r");
        if("add".equals(r) || "edit".equals(r)) {
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"admin");
            if(!hasPermission){
                return;
            }
            Teacher teacher = new Teacher();
            teacher.setTno(req.getParameter("tno"));
            teacher.setPassword(req.getParameter("password"));
            teacher.setTname(req.getParameter("tname"));

            //新增教师
            //创建 Teacher 对象并设置属性，调用 TeacherService 的 insert 方法插入教师信息
            //根据返回值返回 JSON 格式的响应
            if("add".equals(r)){
                String msg = teacherService.insert(teacher);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"保存教师成功！"));
                    return;
                }
            }else{
                //修改教师
                //创建 Teacher 对象并设置属性，调用 TeacherService 的update方法更新教师信息
                //根据返回值返回JSON格式的响应
                String msg = teacherService.update(teacher);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"更新教师成功！"));
                    return;
                }
            }
        }else{
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"admin");
            if(!hasPermission){
                return;
            }
            //删除教师
            //获取教师编号，调用 TeacherService 的 delete 方法删除教师信息,根据返回值返回 JSON 格式的响应
            String tno = req.getParameter("tno");
            int res = teacherService.delete(tno);
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

