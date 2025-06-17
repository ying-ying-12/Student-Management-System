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

    ClazzService clazzService = new ClazzService();
    StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        
        PrintWriter writer = resp.getWriter();
        try {
            int clazzC = clazzService.count();
            int stuC = studentService.count();
            List<Clazz> clazzes = clazzService.statistics();
            
            Map<String,Object> res = new HashMap<>();
            res.put("clazzCount", clazzC);
            res.put("studentCount", stuC);
            res.put("clazzes", clazzes != null ? clazzes : new ArrayList<>());
            
            writer.write(ApiResult.json(true, "成功", res));
        } catch (Exception e) {
            e.printStackTrace();
            writer.write(ApiResult.json(false, "获取数据失败: " + e.getMessage()));
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
