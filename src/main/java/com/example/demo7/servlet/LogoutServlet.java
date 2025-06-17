package com.example.demo7.servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//销毁用户的会话（Session）并将用户重定向到登录页面，从而实现用户的安全退出
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");// 设置编码，否则从前端获取参数乱码
        req.getSession().invalidate();
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
