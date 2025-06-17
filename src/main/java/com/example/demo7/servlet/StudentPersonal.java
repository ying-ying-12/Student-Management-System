package com.example.demo7.servlet;


import com.example.demo7.entity.Student;
import com.example.demo7.service.StudentService;
import com.example.demo7.utils.ApiResult;
import com.example.demo7.utils.MyUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
// 处理路径为/student-personal的 POST 请求
//
//个人信息修改：学生可修改除学号外的个人信息（姓名、密码、联系方式等）
//密码修改：学生可修改登录密码，需验证原密码
//权限控制：仅允许学生本人修改自己的信息，禁止越权操作
@WebServlet("/student-personal")
public class StudentPersonal extends HttpServlet {

    StudentService studentService = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");
        if(!MyUtils.hasPermission(req,resp,true,"student")){
            return;
        }

        String sno = req.getParameter("sno");
        Student loginStudent = (Student) req.getSession().getAttribute("user");
        if(!loginStudent.getSno().equals(sno)){
            resp.getWriter().write(ApiResult.json(false,"只能修改自己的信息！拒绝访问"));
        }

        String r = req.getParameter("r");
        if("edit".equals(r)){
            //修改个人信息
            Student student = new Student();
            student.setSno(req.getParameter("sno"));
            student.setPassword(req.getParameter("password"));
            student.setName(req.getParameter("name"));
            student.setTele(req.getParameter("tele"));
            student.setGender(req.getParameter("gender"));
            student.setAddress(req.getParameter("address"));
            try {
                student.setEnterdate(MyUtils.strToDate(req.getParameter("enterdate")));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String ageStr = req.getParameter("age");
            if(ageStr != null){
                student.setAge(Integer.parseInt(ageStr));
            }else {
                student.setAge(0);
            }
            String msg = studentService.update(student);
            if(msg!=null){
                resp.getWriter().write(ApiResult.json(false,msg));
                return;
            }else{
                req.getSession().setAttribute("user",studentService.getBySno(sno));
                resp.getWriter().write(ApiResult.json(true,"修改成功！"));
                return;
            }
        }
        if("pwd".equals(r)){
            String oldPwd = req.getParameter("oldPwd");
            String newPwd = req.getParameter("newPwd");
            String newPwd2 = req.getParameter("newPwd2");
            if(!Objects.equals(newPwd,newPwd2)){
                resp.getWriter().write(ApiResult.json(false,"两次新密码输入不一致！"));
                return;
            }
            if(!oldPwd.equals(loginStudent.getPassword())){
                resp.getWriter().write(ApiResult.json(false,"旧密码错误！"));
                return;
            }
            Student student = new Student();
            student.setSno(sno);
            student.setPassword(newPwd);
            studentService.update(student);
            loginStudent.setPassword(newPwd);
            req.getSession().setAttribute("user",loginStudent);
            resp.getWriter().write(ApiResult.json(true,"修改成功"));
            return;
        }
        //
    }
}
