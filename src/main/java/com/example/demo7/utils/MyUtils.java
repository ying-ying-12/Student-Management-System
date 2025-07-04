package com.example.demo7.utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 时间操作工具
 */
public class MyUtils {
    /**
     * 字符串转日期
     */
    //两个静态方法，分别用于日期格式转换和权限验证
    public static Date strToDate(String datestr) throws  ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
//验证当前用户是否拥有指定角色权限
    public static boolean hasPermission(HttpServletRequest req, HttpServletResponse resp,boolean isAjax,String... roles) throws ServletException, IOException {
        Object role = req.getSession().getAttribute("role");
        List<String> roleList = Arrays.asList(roles);
        if(role == null || !roleList.contains(role)){
            if(!isAjax){
                req.getRequestDispatcher("/login.jsp").forward(req,resp);
            }else {
                resp.getWriter().write(ApiResult.json(false,"没有权限"));
            }
            return false;
        }
        return true;
    }

}

