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

/**
 * 学生个人信息管理Servlet
 * 处理学生个人信息修改和密码修改请求
 * 权限控制：仅允许学生本人修改自己的信息
 */
@WebServlet("/student-personal")
public class StudentPersonal extends HttpServlet {

    private final StudentService studentService = new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码，确保中文参数正确解析
        req.setCharacterEncoding("utf-8");
        // 设置响应格式为JSON，支持中文
        resp.setContentType("application/json; charset=utf-8");

        // 验证用户权限（必须是已登录的学生角色）
        if (!MyUtils.hasPermission(req, resp, true, "student")) {
            return;
        }

        // 获取当前操作的学生学号，并与登录用户比对
        String sno = req.getParameter("sno");
        Student loginStudent = (Student) req.getSession().getAttribute("user");
        if (!loginStudent.getSno().equals(sno)) {
            // 防止越权操作，返回错误提示
            resp.getWriter().write(ApiResult.json(false, "只能修改自己的信息！拒绝访问"));
            return;
        }

        // 根据请求类型执行不同操作
        String r = req.getParameter("r");

        // 1. 处理个人信息修改请求
        if ("edit".equals(r)) {
            // 从请求参数构建学生对象（学号不可修改）
            Student student = new Student();
            student.setSno(sno);
            student.setPassword(req.getParameter("password"));
            student.setName(req.getParameter("name"));
            student.setTele(req.getParameter("tele"));
            student.setGender(req.getParameter("gender"));
            student.setAddress(req.getParameter("address"));

            // 处理入学日期（字符串转日期）
            try {
                student.setEnterdate(MyUtils.strToDate(req.getParameter("enterdate")));
            } catch (ParseException e) {
                throw new RuntimeException("日期格式错误", e);
            }

            // 处理年龄参数（允许为空，默认为0）
            String ageStr = req.getParameter("age");
            student.setAge(ageStr != null ? Integer.parseInt(ageStr) : 0);

            // 调用服务层更新学生信息
            String msg = studentService.update(student);
            if (msg != null) {
                // 更新失败，返回错误信息
                resp.getWriter().write(ApiResult.json(false, msg));
                return;
            } else {
                // 更新成功，同步更新Session中的用户信息
                req.getSession().setAttribute("user", studentService.getBySno(sno));
                resp.getWriter().write(ApiResult.json(true, "修改成功！"));
                return;
            }
        }

        // 2. 处理密码修改请求
        if ("pwd".equals(r)) {
            // 获取新旧密码参数
            String oldPwd = req.getParameter("oldPwd");
            String newPwd = req.getParameter("newPwd");
            String newPwd2 = req.getParameter("newPwd2");

            // 验证两次新密码一致性
            if (!Objects.equals(newPwd, newPwd2)) {
                resp.getWriter().write(ApiResult.json(false, "两次新密码输入不一致！"));
                return;
            }

            // 验证旧密码正确性
            if (!oldPwd.equals(loginStudent.getPassword())) {
                resp.getWriter().write(ApiResult.json(false, "旧密码错误！"));
                return;
            }

            // 更新密码（仅修改密码字段）
            Student student = new Student();
            student.setSno(sno);
            student.setPassword(newPwd);
            studentService.update(student);

            // 同步更新Session中的密码，避免重复登录
            loginStudent.setPassword(newPwd);
            req.getSession().setAttribute("user", loginStudent);

            resp.getWriter().write(ApiResult.json(true, "修改成功"));
            return;
        }
    }
}