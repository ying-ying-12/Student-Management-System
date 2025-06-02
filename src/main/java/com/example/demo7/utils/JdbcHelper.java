package com.example.demo7.utils;


import java.sql.*;

/**
 * 1 数据库配置信息
 * 2 提供最基本的和数据库交互的方法
 */
public class JdbcHelper {

    private static final String className = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://123.207.79.43:3306/stu_manage?serverTimezone=GMT%2B8&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false";
    private static final String user = "stu_manage";
    private static final String pass = "stu_manage";// 自行修改密码

    public static void main(String[] args) throws SQLException {
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_student");
        while (resultSet.next()){
            System.out.println(resultSet.getString("sno"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("age"));
        }
        helper.closeDB();
    }

    static {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public ResultSet executeQuery(String sql,Object... params){
        try {
            pstmt = conn.prepareStatement(sql);
            if(params!=null){
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i+1,params[i]);
                }
            }
            rs = pstmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    public int excuteUpdate(String sql,Object ... params){
        int row = -1;
        try {
            pstmt = conn.prepareStatement(sql);
            if(params!=null){
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i+1,params[i]);
                }
            }
            row = pstmt.executeUpdate();//sql执行以后影响的行数
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return row;
    }

    public void closeDB(){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
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

