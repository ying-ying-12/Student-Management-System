package com.example.demo7.servlet;

import com.example.demo7.entity.Clazz;
import com.example.demo7.service.ClazzService;
import com.example.demo7.utils.ApiResult;
import com.example.demo7.utils.MyUtils;
import com.example.demo7.utils.PagerVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 班级信息管理Servlet
 * 处理班级的增删改查请求，支持分页查询和条件筛选
 * 权限控制：仅管理员可进行所有操作
 */
@WebServlet("/clazz")
public class ClazzServlet extends HttpServlet {
    // 业务逻辑层对象：处理班级相关业务
    private final ClazzService clazzService = new ClazzService();

    /**
     * 处理HTTP GET请求
     * 支持班级列表展示、新增班级页面跳转、编辑班级页面跳转
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码，确保中文参数能正确解析
        req.setCharacterEncoding("utf-8");

        // 获取请求类型参数（r参数决定具体操作）
        String r = req.getParameter("r");

        // 1. 默认处理班级列表请求
        if (r == null) {
            // 获取分页参数（当前页码，默认为第1页）
            String current = req.getParameter("current");
            if (current == null) {
                current = "1";
            }

            // 获取查询条件参数
            String clazzno = req.getParameter("clazzno"); // 班级编号
            String name = req.getParameter("name");       // 班级名称

            // 调用Service层进行分页查询
            PagerVO<Clazz> pagerVO = clazzService.page(
                    Integer.parseInt(current), 10, clazzno, name
            );
            // 初始化分页信息（计算总页数、页码范围等）
            pagerVO.init();

            // 将查询条件和分页结果存入请求属性，传递给JSP页面
            req.setAttribute("clazzno", clazzno);
            req.setAttribute("name", name);
            req.setAttribute("pagerVO", pagerVO);

            // 转发到班级列表页面
            req.getRequestDispatcher("/WEB-INF/views/clazz-list.jsp").forward(req, resp);
        }

        // 2. 处理新增班级页面请求
        if ("add".equals(r)) {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }

            // 转发到新增班级页面
            req.getRequestDispatcher("/WEB-INF/views/clazz-add.jsp").forward(req, resp);
        }

        // 3. 处理编辑班级页面请求
        if ("edit".equals(r)) {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, false, "admin");
            if (!hasPermission) {
                return;
            }

            // 获取要编辑的班级编号，并查询班级信息
            String clazzno = req.getParameter("clazzno");
            Clazz clazz = clazzService.getByClazzno(clazzno);
            req.setAttribute("entity", clazz);

            // 转发到编辑班级页面
            req.getRequestDispatcher("/WEB-INF/views/clazz-edit.jsp").forward(req, resp);
        }
    }

    /**
     * 处理HTTP POST请求
     * 支持班级信息的新增、修改和删除操作
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求和响应编码，确保中文正常处理
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");

        // 获取请求类型参数
        String r = req.getParameter("r");

        // 1. 处理新增或编辑班级请求
        if ("add".equals(r) || "edit".equals(r)) {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, true, "admin");
            if (!hasPermission) {
                return;
            }

            // 从请求参数构建班级对象
            String clazzno = req.getParameter("clazzno"); // 班级编号
            String name = req.getParameter("name");       // 班级名称
            Clazz clazz = new Clazz();
            clazz.setName(name);
            clazz.setClazzno(clazzno);

            // 执行新增操作
            if ("add".equals(r)) {
                String msg = clazzService.insert(clazz);
                if (msg != null) {
                    // 返回错误信息（如班级编号已存在）
                    resp.getWriter().write(ApiResult.json(false, msg));
                    return;
                } else {
                    // 返回成功信息
                    resp.getWriter().write(ApiResult.json(true, "保存班级成功！"));
                    return;
                }
            }
            // 执行编辑操作
            else {
                String msg = clazzService.update(clazz);
                if (msg != null) {
                    // 返回错误信息
                    resp.getWriter().write(ApiResult.json(false, msg));
                    return;
                } else {
                    // 返回成功信息
                    resp.getWriter().write(ApiResult.json(true, "更新班级成功！"));
                    return;
                }
            }
        }
        // 2. 处理删除班级请求
        else {
            // 验证用户权限（必须是管理员）
            boolean hasPermission = MyUtils.hasPermission(req, resp, true, "admin");
            if (!hasPermission) {
                return;
            }

            // 获取要删除的班级编号
            String clazzno = req.getParameter("clazzno");
            String msg = clazzService.delete(clazzno);

            if (msg != null) {
                // 返回删除失败信息（如班级下存在学生）
                resp.getWriter().write(ApiResult.json(false, msg));
                return;
            } else {
                // 返回删除成功信息
                resp.getWriter().write(ApiResult.json(true, "删除成功！"));
                return;
            }
        }
    }
}

