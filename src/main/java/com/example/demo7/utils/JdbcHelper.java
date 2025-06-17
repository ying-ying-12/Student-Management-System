package com.example.demo7.utils;

import java.sql.*;

/**
 * 数据库连接与操作工具类
 * 功能：
 * 1. 管理数据库连接的创建与释放
 * 2. 提供SQL查询与更新的执行方法
 * 3. 处理JDBC操作中的异常情况
 */
public class JdbcHelper {

    // 数据库连接配置信息（使用MySQL数据库）
    private static final String className = "com.mysql.cj.jdbc.Driver";  // JDBC驱动类名
    private static final String url = "jdbc:mysql://localhost:3306/stu_manage?serverTimezone=GMT%2B8&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false";
    // 连接URL，包含数据库名、时区设置、字符编码等参数
    private static final String user = "root";  // 数据库用户名
    private static final String pass = "123456";  // 数据库密码

    /**
     * 测试数据库连接的main方法
     * 功能：执行简单查询并打印结果，验证数据库连接与操作是否正常
     */
    public static void main(String[] args) throws SQLException {
        JdbcHelper helper = new JdbcHelper();
        ResultSet resultSet = helper.executeQuery("select * from tb_student");
        // 遍历结果集并打印学生基本信息
        while (resultSet.next()) {
            System.out.println(resultSet.getString("sno"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("age"));
        }
        helper.closeDB();  // 释放数据库资源
    }

    /**
     * 静态代码块：加载JDBC驱动
     * 在类加载时自动执行，确保驱动程序被注册
     */
    static {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // JDBC连接相关对象
    private Connection conn = null;       // 数据库连接对象
    private PreparedStatement pstmt = null;  // 预编译SQL语句对象
    private ResultSet rs = null;           // 结果集对象

    /**
     * 构造方法：创建数据库连接
     * 在实例化JdbcHelper时自动建立与数据库的连接
     */
    public JdbcHelper() {
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 执行SQL查询语句（SELECT）
     * @param sql SQL查询语句（支持占位符?）
     * @param params 占位符对应的参数值
     * @return 结果集对象（ResultSet）
     */
    public ResultSet executeQuery(String sql, Object... params) {
        try {
            pstmt = conn.prepareStatement(sql);  // 预编译SQL语句
            // 设置SQL参数（占位符赋值）
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();  // 执行查询并获取结果集
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行SQL更新语句（INSERT/UPDATE/DELETE）
     * @param sql SQL更新语句（支持占位符?）
     * @param params 占位符对应的参数值
     * @return 受影响的行数
     */
    public int excuteUpdate(String sql, Object... params) {
        int row = -1;
        try {
            pstmt = conn.prepareStatement(sql);  // 预编译SQL语句
            // 设置SQL参数（占位符赋值）
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            row = pstmt.executeUpdate();  // 执行更新并获取受影响行数
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return row;
    }

    /**
     * 释放数据库资源
     * 按ResultSet → PreparedStatement → Connection的顺序关闭对象，避免资源泄漏
     */
    public void closeDB() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}