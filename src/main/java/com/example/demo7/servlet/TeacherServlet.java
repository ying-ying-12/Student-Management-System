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

@WebServlet("/teacher")
public class TeacherServlet extends HttpServlet {
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
            String tno = req.getParameter("tno");
            String tname = req.getParameter("tname");

            PagerVO<Teacher> pagerVO = teacherService.page(Integer.parseInt(current),10,tno,tname);
            pagerVO.init();

            req.setAttribute("tno",tno);
            req.setAttribute("tname",tname);
            req.setAttribute("pagerVO",pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/teacher-list.jsp").forward(req,resp);
        }

        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/teacher-add.jsp").forward(req,resp);
        }
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //新增 修改 删除
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

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
            //删除
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

