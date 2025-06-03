package com.example.demo7.servlet;


import com.example.demo7.entity.Admin;
import com.example.demo7.entity.Student;
import com.example.demo7.service.AdminService;
import com.example.demo7.service.StudentService;
import com.example.demo7.utils.ApiResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    AdminService adminService = new AdminService();
    StudentService studentService = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");

        String captcha = req.getParameter("captcha");
        Object sessionCaptcha = req.getSession().getAttribute("captcha");
        if(captcha == null || !captcha.equalsIgnoreCase((String) sessionCaptcha)){
            resp.getWriter().print(ApiResult.json(false,"验证码输入错误！"));
            return;
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String usertype = req.getParameter("usertype");
        //判断角色
        if("admin".equals(usertype)){
            Admin admin = adminService.getByUsername(username);
            if(admin == null){
                resp.getWriter().print(ApiResult.json(false,"用户不存在"));
                return;
            }
            if(admin.getPassword().equals(password)){
                req.getSession().setAttribute("user",admin);
                req.getSession().setAttribute("role","admin");
                resp.getWriter().print(ApiResult.json(true,"登录成功"));
                return;
            }else {
                resp.getWriter().print(ApiResult.json(false,"密码错误"));
                return;
            }
        }else {
            Student student = studentService.getBySno(username);
            if(student == null){
                resp.getWriter().print(ApiResult.json(false,"用户不存在"));
                return;
            }
            if(student.getPassword().equals(password)){
                req.getSession().setAttribute("user",student);
                req.getSession().setAttribute("role","student");
                resp.getWriter().print(ApiResult.json(true,"登录成功"));
                return;
            }else {
                resp.getWriter().print(ApiResult.json(false,"密码错误"));
                return;
            }
        }

    }
}

