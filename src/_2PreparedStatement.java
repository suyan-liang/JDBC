import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Author:liang;
 * Date:2020/3/21;
 * Time:10:25;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 * 通过创建statement对象来执行SQL语句
 * 一共有三种：Statement,PreparedStatement,CallableStatement
 * 我们使用的是PreparedStatement
 *
 * Statement的弊端
 * https://blog.csdn.net/qq_40148447/article/details/86634441
 *
 * Statement会把传进来的东西都当成SQL语句来执行
 * PreparedStatement可以把需要传进来的东西放上问号，问号里面的都会当做字符串处理，不会去执行
 * 称之为SQL注入问题
 * PreparedStatement是一个预编译的SQL语句，逻辑关系已经确定，所以填入的数据都会被当成字符串来处理
 *
 * 除了解决statement的拼串和SQL注入问题，
 * PreparedStatement还可以操作Blob类型的数据，而Statement不行
 * PreparedStatement可以实现更高效的批量操作，比如在插入的时候，Statement需要一条一条校验，而PreparedStatement只需一次，其他填充占位符即可
 *
 *
 * execute函数返回boolean
 * 如果执行的是查询操作且有返回结果则返回true，如果是增删改操作则返回false
 *
 * executeUpdate返回int  即增删改操作影响的行数
 * 所以，如果是大于0的话就是修改成功，否则修改失败
 *
 */
public class _2PreparedStatement {
    public static void main(String[] args) throws Exception {
        String sql="update customers set name=? where id=?";
        update(sql,"zss",21);
    }

    /**
     * 通用的增删改操作
     * 可以增，可以删，可以改
     *
     * 1.get皇帝
     * 2.写圣旨骨架
     * 3.get钦差大臣
     * 4.填写圣旨
     * 5.执行
     * 6.关闭
     *
     * 一气呵成!!!!!
     *
     * @param sql sql语句
     * @param args 占位符处的参数
     *
     *
     */
    private static void update(String sql,Object ...args) {
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }

    /**
     * 使用自定义的工具类实现修改
     */
    private static void update_demo() {
        //需求：修改id为20的name为zb
        Connection connection=null;
        PreparedStatement ps=null;
        try {
            connection=myJDBC.getConnection();
            String sql="update customers set name=? where id=?";
            ps=connection.prepareStatement(sql);
            //注意索引是从1开始就行
            ps.setString(1,"zb");
            ps.setInt(2,20);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            myJDBC.closeSources(connection,ps);
        }
    }

    /**
     * 演示插入数据
     */
    private static void insert_demo() {
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            //向test数据库下的customers表中插入一条记录
            //1.获取连接
            Properties properties=new Properties();
            FileInputStream in=new FileInputStream("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\info");
            properties.load(in);
            in.close();
            Class.forName(properties.getProperty("driverClass"));
            connection = DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("user"),properties.getProperty("password"));
            //2.写SQL语句并获取PreparedStatement对象  写圣旨框架，找钦差大臣
            String sql="insert into customers (name,email,birth) values (?,?,?)";
            ps = connection.prepareStatement(sql);
            //3.填补占位符   补全圣旨
            ps.setString(1,"zs");
            ps.setString(2,"absxiinasx@126.com");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date=sdf.parse("1234-05-08");
            ps.setDate(3,new Date(date.getTime()));
            //4.执行圣旨
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源.  关闭资源的时候要注意检查空指针异常，要用try catch环绕
            try {
                if(ps!=null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection!=null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
