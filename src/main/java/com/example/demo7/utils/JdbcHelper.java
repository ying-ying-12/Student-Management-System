package com.example.demo7.utils;

import java.sql.*;

/**
 * 1 数据库配置信息
 * 2 提供最基本的和数据库交互的方法
 */
public class JdbcHelper {

    private static final String className = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/stu_manage?serverTimezone=GMT%2B8&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false";
    private static final String user = "root";
    private static final String pass = "123456";// 密码

    public static void main(String[] args) throws SQLException {
        JdbcHelper helper = new JdbcHelper();
        //查看所有学生
        ResultSet resultSet = helper.executeQuery("select * from tb_student");
        while (resultSet.next()){
            System.out.println(resultSet.getString("sno"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("age"));
        }
        helper.closeDB();
    }

    //加载驱动
    static {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //构造方法
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public JdbcHelper(){
        try {
            conn = DriverManager.getConnection(url,user,pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //执行查询
    //sql语句，传进去的参数
    public ResultSet executeQuery(String sql,Object... params){
        //预处理sql语句
        try {
            pstmt = conn.prepareStatement(sql);
            //参数不为空，传参数进去
            if(params!=null){
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i+1,params[i]);
                }
            }
            //执行查询
            rs = pstmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;//返回查询结果
    }

//执行增删改
    public int excuteUpdate(String sql,Object ... params){
        //返回影响的行数
        int row = -1;
        try {
            pstmt = conn.prepareStatement(sql);
            if(params!=null){
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i+1,params[i]);
                }
            }
            row = pstmt.executeUpdate();//返回sql执行以后影响的行数
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return row;
    }

    //关闭资源
    public void closeDB(){
        //关闭查询结果占用的资源
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //关闭预处理器
        if(pstmt != null){
            try {
                pstmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
