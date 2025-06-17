package com.example.demo7.servlet;

import com.example.demo7.entity.Clazz;
import com.example.demo7.entity.Student;
import com.example.demo7.service.ClazzService;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**

 学生管理的 Servlet，处理学生信息的增删改查请求
 采用 MVC 架构中的控制器角色，负责接收请求、调用业务逻辑并返回响应
 */
@WebServlet ("/student")
public class StudentServlet extends HttpServlet {
    // 创建业务逻辑处理对象
    private final StudentService studentService = new StudentService();
    private final ClazzService clazzService = new ClazzService();

    /**
     * 处理 HTTP GET 请求
     * 支持学生列表展示、新增学生页面跳转、编辑学生页面跳转
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
// 设置请求编码，确保中文参数能正确解析
        req.setCharacterEncoding("utf-8");
// 获取请求操作类型参数
        String r = req.getParameter("r");
// 1. 处理学生列表请求（默认操作）
        if (r == null) {
// 获取分页参数，默认为第一页
            String current = req.getParameter("current");
            if (current == null) {
                current = "1";
            }
// 获取查询条件参数
            String sno = req.getParameter("sno");
            String gender = req.getParameter("gender");
            String clazzno = req.getParameter("clazzno");
            String name = req.getParameter("name");
// 调用业务层进行分页查询
            PagerVO<Student> pagerVO = studentService.page(
                    Integer.parseInt(current), 10, sno, name, gender, clazzno
            );
// 初始化分页信息（计算总页数等）
            pagerVO.init();
// 获取所有班级信息，用于学生列表中显示班级名称
            List<Clazz> clazzes = clazzService.listAll();
            HashMap<String, Clazz> clazzMap = new HashMap<>();
            for (Clazz clazz : clazzes) {
                clazzMap.put(clazz.getClazzno(), clazz);
            }
// 为每个学生设置对应的班级对象，方便前端展示
            for (Student student : pagerVO.getList()) {
                student.setClazz(clazzMap.get(student.getClazzno()));
            }
// 将查询条件和结果存入请求属性，传递给 JSP 页面
            req.setAttribute("sno", sno);
            req.setAttribute("gender", gender);
            req.setAttribute("clazzno", clazzno);
            req.setAttribute("name", name);
            req.setAttribute("pagerVO", pagerVO);
// 转发到学生列表页面
            req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
        }
// 2. 处理新增学生页面请求
        if ("add".equals(r)) {
// 检查用户权限
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }
// 查询所有班级信息，用于新增表单中的班级下拉选择
            List<Clazz> clazzes = clazzService.listAll();
            req.setAttribute("clazzes", clazzes);
// 转发到新增学生页面
            req.getRequestDispatcher("/WEB-INF/views/student-add.jsp").forward(req, resp);
        }
// 3. 处理编辑学生页面请求
        if ("edit".equals(r)) {
// 检查用户权限
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }
// 查询所有班级信息，用于编辑表单中的班级下拉选择
            List<Clazz> clazzes = clazzService.listAll();
            req.setAttribute("clazzes", clazzes);
// 获取要编辑的学生学号，并查询学生信息
            String sno = req.getParameter("sno");
            Student student = studentService.getBySno(sno);
            req.setAttribute("entity", student);
// 转发到编辑学生页面
            req.getRequestDispatcher("/WEB-INF/views/student-edit.jsp").forward(req, resp);
        }
    }
}