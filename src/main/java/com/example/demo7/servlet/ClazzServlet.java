package com.example.demo7.servlet;

import com.example.demo7.entity.Clazz;
import com.example.demo7.service.ClazzService;
import com.example.demo7.utils.ApiResult;
import com.example.demo7.utils.MyUtils;
import com.example.demo7.utils.PagerVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/clazz")
public class ClazzServlet extends HttpServlet {
    ClazzService clazzService = new ClazzService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码，防止乱码
        req.setCharacterEncoding("utf-8");
        //查询参数
        String r = req.getParameter("r");
        if(r == null){
            String current = req.getParameter("current");
            if(current==null){
                current = "1";
            }
            String clazzno = req.getParameter("clazzno");
            String name = req.getParameter("name");
            //分页信息查询
            //查询条件返回前端
            PagerVO<Clazz> pagerVO = clazzService.page(Integer.parseInt(current),10,clazzno,name);
            //调用后端分页的方法
            pagerVO.init();
            req.setAttribute("clazzno",clazzno);
            req.setAttribute("name",name);
            req.setAttribute("pagerVO",pagerVO);
            //转发页面
            req.getRequestDispatcher("/WEB-INF/views/clazz-list.jsp").forward(req,resp);
        }

        //clazz-add.jsp放在WEB-INF里面所以不能直接访问
        //用后端的servlet
        if("add".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            req.getRequestDispatcher("/WEB-INF/views/clazz-add.jsp").forward(req,resp);
        }
        if("edit".equals(r)){
            boolean hasPermission = MyUtils.hasPermission(req,resp,false,"admin");
            if(!hasPermission){
                return;
            }
            String clazzno = req.getParameter("clazzno");
            Clazz clazz = clazzService.getByClazzno(clazzno);
            //给前端
            req.setAttribute("entity",clazz);
            req.getRequestDispatcher("/WEB-INF/views/clazz-edit.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //新增 修改 删除
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        resp.setContentType("application/json; charset=utf-8");

        //区分是新增还是删除操作
        String r = req.getParameter("r");
        //添加或者编辑操作获取班级参数
        if("add".equals(r) || "edit".equals(r)) {
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"admin");
            if(!hasPermission){
                return;
            }
            String clazzno = req.getParameter("clazzno");
            String name = req.getParameter("name");
            Clazz clazz = new Clazz();
            clazz.setName(name);
            clazz.setClazzno(clazzno);
            if("add".equals(r)){
                String msg = clazzService.insert(clazz);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"保存班级成功！"));
                    return;
                }
            }else{
                String msg = clazzService.update(clazz);
                if(msg!=null){
                    resp.getWriter().write(ApiResult.json(false,msg));
                    return;
                }else{
                    resp.getWriter().write(ApiResult.json(true,"更新班级成功！"));
                    return;
                }
            }
        }else{
            boolean hasPermission = MyUtils.hasPermission(req,resp,true,"admin");
            if(!hasPermission){
                return;
            }
            //删除
            String clazzno = req.getParameter("clazzno");
            String msg = clazzService.delete(clazzno);
            if(msg!=null){
                resp.getWriter().write(ApiResult.json(false,msg));
                return;
            }else{
                resp.getWriter().write(ApiResult.json(true,"删除成功！"));
                return;
            }
        }
    }
}


