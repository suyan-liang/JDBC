import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Author:liang;
 * Date:2020/3/21;
 * Time:8:59;
 * Package Name:Connection;
 * 需求：
 * 步骤：
 *
 * 一个形象的比喻
 * driver看成司机，数据库就是目的地
 * 只有这个厂商的司机才能去这个厂商
 *
 * 然后你要去目的地，要和司机说你的目的地在哪，然后你的通关文牒
 * 司机一按connect，so~的就连接上了
 * 即java程序和数据库连接上了
 */
public class _1connection {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

    }

    /**
     * 最终版本
     * 把需要用的信息放入配置文件，用的时候直接从配置文件中读取，实现了数据和代码的分离
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void method_final_version() throws IOException, ClassNotFoundException, SQLException {
        //1.把需要提供的基本信息放到配置文件中，读取配置文件
        Properties properties=new Properties();
        FileInputStream in=new FileInputStream("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\info");
        properties.load(in);
        in.close();
        //2.加载内存，进行注册
        Class.forName(properties.getProperty("driverClass"));
        //3.获取连接
        Connection connection= DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("user"),properties.getProperty("password"));
        System.out.println(connection);
    }

    /**
     * 连接的优化写法
     *
     * 第2步中，将com.mysql.jdbc.Driver加载进内存，这个类有个代码块，会自动帮你注册
     *
     *  static {
     *         try {
     *             DriverManager.registerDriver(new Driver());
     *         } catch (SQLException var1) {
     *             throw new RuntimeException("Can't register driver!");
     *         }
     *     }
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void method_4() throws ClassNotFoundException, SQLException {
        //1.提供获取连接基本信息
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="hello,mysql";
        //2.获取driver实现类对象，不用自己写实现类对象去注册了，加载进内存就可以获取连接了
        Class.forName("com.mysql.jdbc.Driver");
        //3.获取连接
        Connection connection= DriverManager.getConnection(url,user,password);
        System.out.println(connection);
    }

    /**
     * 通过DriverManger来实现获取连接
     * 这个东西感觉就像是一个代理人，你把事情交给它去做，先注册，然后它帮你获取连接
     * 但是有什么好处呢？
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private static void method_3() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class clazz =Class.forName("com.mysql.jdbc.Driver");
        Driver driver= (Driver) clazz.newInstance();
        DriverManager.registerDriver(driver);
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="hello,mysql";
        Connection connection=DriverManager.getConnection(url,user,password);
        System.out.println(connection);
    }

    /**
     * 通过反射的方式获取驱动，使得可移植性增强
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private static void method_2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.通过反射获取驱动
        Class clazz =Class.forName("com.mysql.jdbc.Driver");
        Driver driver= (Driver) clazz.newInstance();
        //2.剩下的都一样
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","hello,mysql");
        Connection connection=driver.connect(url,info);
        System.out.println(connection);
    }

    /**
     * 简陋地获取连接
     * @throws SQLException
     */
    private static void method_1() throws SQLException {
        //这是我MySQL封装的一个驱动，你想连接我MySQL，只需要调用我这个驱动的connect方法即可获得一个Connection对象
        Driver driver=new com.mysql.jdbc.Driver();
        //connect方法需要两个参数，一个是要调用数据库的url，一个是配置文件（用户名和密码）
        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","hello,mysql");
        //获取connection对象
        Connection connection=driver.connect(url,info);
        System.out.println(connection);
    }
}
