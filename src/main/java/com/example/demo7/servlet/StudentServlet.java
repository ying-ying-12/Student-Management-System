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

/*
 * 学生信息管理Servlet
 * 处理学生的增删改查请求，采用MVC架构实现前后端交互
 * 权限控制：仅管理员可执行增删改操作，查询功能对所有用户开放
 */
@WebServlet("/student")
public class StudentServlet extends HttpServlet {
    // 业务逻辑层对象：处理学生信息相关业务
    private final StudentService studentService = new StudentService();
    // 业务逻辑层对象：处理班级信息相关业务（用于获取班级列表）
    private final ClazzService clazzService = new ClazzService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码，确保中文参数正确解析
        req.setCharacterEncoding("utf-8");
        // 获取请求操作类型参数（r参数决定具体功能）
        String r = req.getParameter("r");

        // 1. 处理学生列表查询请求（默认操作）
        if (r == null) {
            // 获取分页参数（默认从第1页开始）
            String current = req.getParameter("current");
            if (current == null) {
                current = "1";
            }
            // 获取查询条件参数
            String sno = req.getParameter("sno");       // 学号
            String gender = req.getParameter("gender"); // 性别
            String clazzno = req.getParameter("clazzno"); // 班级编号
            String name = req.getParameter("name");     // 姓名

            // 调用Service层进行分页查询（每页10条记录）
            PagerVO<Student> pagerVO = studentService.page(
                    Integer.parseInt(current), 10, sno, name, gender, clazzno
            );
            // 初始化分页信息（计算总页数、页码范围等）
            pagerVO.init();

            // 获取所有班级信息，用于关联学生所属班级
            List<Clazz> clazzes = clazzService.listAll();
            // 构建班级编号到班级对象的映射表，便于快速查询
            HashMap<String, Clazz> clazzMap = new HashMap<>();
            for (Clazz clazz : clazzes) {
                clazzMap.put(clazz.getClazzno(), clazz);
            }
            // 为每个学生对象设置所属班级信息
            for (Student student : pagerVO.getList()) {
                student.setClazz(clazzMap.get(student.getClazzno()));
            }

            // 将查询条件和分页结果存入请求属性，传递给JSP页面
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
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }
            // 查询所有班级信息，用于新增表单的班级下拉选择
            List<Clazz> clazzes = clazzService.listAll();
            req.setAttribute("clazzes", clazzes);
            // 转发到新增学生页面
            req.getRequestDispatcher("/WEB-INF/views/student-add.jsp").forward(req, resp);
        }

        // 3. 处理编辑学生页面请求
        if ("edit".equals(r)) {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }
            // 查询所有班级信息，用于编辑表单的班级下拉选择
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码（处理中文参数）和响应格式（JSON）
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");

        // 获取请求操作类型参数
        String r = req.getParameter("r");

        // 1. 处理新增或编辑学生请求
        if ("add".equals(r) || "edit".equals(r)) {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, true, "admin");
            if (!hasPermission) {
                return;
            }
            // 从请求参数构建学生对象
            Student student = new Student();
            student.setSno(req.getParameter("sno"));         // 学号
            student.setPassword(req.getParameter("password")); // 密码
            student.setName(req.getParameter("name"));       // 姓名
            student.setTele(req.getParameter("tele"));       // 电话
            student.setGender(req.getParameter("gender"));   // 性别
            student.setAddress(req.getParameter("address")); // 地址
            student.setClazzno(req.getParameter("clazzno")); // 班级编号
            // 处理入学日期（字符串转日期）
            String enterdate = req.getParameter("enterdate");
            try {
                student.setEnterdate(MyUtils.strToDate(enterdate));
            } catch (ParseException e) {
                throw new RuntimeException("日期格式转换失败", e);
            }
            // 处理年龄参数
            String age = req.getParameter("age");
            student.setAge(Integer.parseInt(age));

            // 执行新增操作
            if ("add".equals(r)) {
                String msg = studentService.insert(student);
                if (msg != null) {
                    // 新增失败，返回错误信息（如学号重复）
                    resp.getWriter().write(ApiResult.json(false, msg));
                    return;
                } else {
                    // 新增成功，返回成功提示
                    resp.getWriter().write(ApiResult.json(true, "保存学生成功！"));
                    return;
                }
            }
            // 执行编辑操作
            else {
                String msg = studentService.update(student);
                if (msg != null) {
                    // 编辑失败，返回错误信息
                    resp.getWriter().write(ApiResult.json(false, msg));
                    return;
                } else {
                    // 编辑成功，返回成功提示
                    resp.getWriter().write(ApiResult.json(true, "更新学生成功！"));
                    return;
                }
            }
        }
        // 2. 处理删除学生请求
        else {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, true, "admin");
            if (!hasPermission) {
                return;
            }
            // 获取要删除的学生学号
            String sno = req.getParameter("sno");
            // 调用Service层执行删除操作
            int res = studentService.delete(sno);
            if (res == 0) {
                // 删除失败（如数据库操作异常），返回错误提示
                resp.getWriter().write(ApiResult.json(false, "删除失败，请联系管理员！"));
                return;
            } else {
                // 删除成功，返回成功提示
                resp.getWriter().write(ApiResult.json(true, "删除成功！"));
                return;
            }
        }
    }
}