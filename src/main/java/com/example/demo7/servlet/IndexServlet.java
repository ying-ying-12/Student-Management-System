package com.example.demo7.servlet;

import com.example.demo7.entity.Clazz;
import com.example.demo7.service.ClazzService;
import com.example.demo7.service.StudentService;
import com.example.demo7.utils.ApiResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//处理路径为/index的 GET 请求，返回一个包含班级数量、学生数量和班级统计信息的 JSON 响应
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    // 服务层对象，用于业务逻辑处理
    ClazzService clazzService = new ClazzService();
    StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 编码设置
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        try {
            // 调用服务层获取统计数据
            int clazzC = clazzService.count();          // 班级总数
            int stuC = studentService.count();         // 学生总数
            List<Clazz> clazzes = clazzService.statistics();  // 班级统计信息

            // 组装响应数据
            Map<String, Object> res = new HashMap<>();
            res.put("clazzCount", clazzC);
            res.put("studentCount", stuC);
            res.put("clazzes", clazzes != null ? clazzes : new ArrayList<>());

            writer.write(ApiResult.json(true, "成功", res));
        } catch (Exception e) {
            e.printStackTrace();
            writer.write(ApiResult.json(false, "获取数据失败: " + e.getMessage()));
        } finally {
            // 确保输出流关闭
            writer.flush();
            writer.close();
        }
    }
}