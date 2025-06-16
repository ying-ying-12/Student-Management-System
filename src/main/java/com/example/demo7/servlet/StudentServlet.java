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
import java.util.HashMap;
import java.util.List;
//处理路径为/student的请求，主要实现：
//
//查询功能：支持分页查询学生信息，可按学号、姓名、性别、班级筛选
//添加功能：管理员可添加新学生
//编辑功能：管理员可修改学生信息
//删除功能：管理员可删除学生记录
@WebServlet("/student")
public class StudentServlet extends HttpServlet {
    StudentService studentService = new StudentService();
    ClazzService clazzService = new ClazzService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //查询参数
        String r = req.getParameter("r");
        if(r == null) {
            // 获取分页和查询参数
            String current = req.getParameter("current"); // 当前页码
            String sno = req.getParameter("sno");         // 学号
            String gender = req.getParameter("gender");   // 性别
            String clazzno = req.getParameter("clazzno"); // 班级号
            String name = req.getParameter("name");       // 姓名

            // 调用服务层分页查询学生
            PagerVO<Student> pagerVO = studentService.page(
                    Integer.parseInt(current), 10, sno, name, gender, clazzno
            );
            pagerVO.init(); // 初始化分页信息（总页数、总记录数等）

            // 查询所有班级并构建班级编号到班级对象的映射
            List<Clazz> clazzes = clazzService.listAll();
            HashMap<String, Clazz> clazzmap = new HashMap<>();
            for (Clazz clazz : clazzes) {
                clazzmap.put(clazz.getClazzno(), clazz);
            }

            // 为每个学生设置班级对象（通过班级编号关联）
            for (Student student : pagerVO.getList()) {
                student.setClazz(clazzmap.get(student.getClazzno()));
            }

            // 将数据存入请求域并转发到列表页面
            req.setAttribute("pagerVO", pagerVO);
            req.getRequestDispatcher("/WEB-INF/views/student-list.jsp").forward(req, resp);
        }
        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            //查询所有班级
            List<Clazz> clazzes = clazzService.listAll();
            req.setAttribute("clazzes",clazzes);
            req.getRequestDispatcher("/WEB-INF/views/student-add.jsp").forward(req,resp);
        }
        if("edit".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            List<Clazz> clazzes = clazzService.listAll();
            req.setAttribute("clazzes",clazzes);
            String sno = req.getParameter("sno");
            Student student = studentService.getBySno(sno);
            req.setAttribute("entity",student);
            req.getRequestDispatcher("/WEB-INF/views/student-edit.jsp").forward(req,resp);
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
            Student student = new Student();
            student.setSno(req.getParameter("sno"));
            student.setPassword(req.getParameter("password"));
            student.setName(req.getParameter("name"));
            student.setTele(req.getParameter("tele"));
            student.setGender(req.getParameter("gender"));
            student.setAddress(req.getParameter("address"));
            student.setClazzno(req.getParameter("clazzno"));
            String enterdate = req.getParameter("enterdate");
            student.setEnterdate(MyUtils.strToDate(enterdate));
            String age = req.getParameter("age");
            student.setAge(Integer.parseInt(age));

            if("add".equals(r)){

                String msg = studentService.insert(student);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"保存学生成功！"));
                    return;
                }
            }else{
                String msg = studentService.update(student);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"更新学生成功！"));
                    return;
                }
            }
        }else{
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"admin");
            if(!hasPermission){
                return;
            }
            //删除
            String sno = req.getParameter("sno");
            int res = studentService.delete(sno);
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
