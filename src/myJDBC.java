import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Author:liang;
 * Date:2020/3/21;
 * Time:11:28;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * 把常用的操作比如获取连接和关闭资源封装成一个自定义的工具类供自己使用
 *
 */
public class myJDBC {

    /**
     * 获取数据库的connection
     * @return 连接connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception
    {
        Connection connection=null;
        Properties properties=new Properties();
        FileInputStream in=new FileInputStream("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\info");
        properties.load(in);
        in.close();
        Class.forName(properties.getProperty("driverClass"));
        connection = DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("user"),properties.getProperty("password"));
        return connection;
    }

    /**
     * 关闭增删改操作的函数
     * @param con
     * @param ps
     */
    public static void closeSources(Connection con, PreparedStatement ps)
    {
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(con!=null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载关闭资源的方法，用于关闭select操作的资源
     * @param con
     * @param ps
     * @param rs
     */
    public static void closeSources(Connection con, PreparedStatement ps, ResultSet rs)
    {
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(con!=null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs!=null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
