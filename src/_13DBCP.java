import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:15:40;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 */
public class _13DBCP {
    public static void main(String[] args) throws Exception {
        getByConfig();
    }

    /**
     * 不使用配置文件创建一个DBCP数据库连接池
     * @throws SQLException
     */
    public static void getNoConfig() throws SQLException {
        BasicDataSource bds=new BasicDataSource();
        //基本信息设置
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/test");
        bds.setUsername("root");
        bds.setPassword("hello,mysql");

        //设置管理信息,等等，看下面的dbcp.txt文件
        bds.setInitialSize(10);
        bds.setMaxActive(10);

        Connection conn=bds.getConnection();
        System.out.println(conn);
    }

    /**
     * 使用配置文件创建一个DBCP数据库连接池
     * 在实际使用过程中，把导入配置文件的操作放在静态代码块中进行
     * @throws Exception
     */
    public static void getByConfig() throws Exception {
        Properties prop=new Properties();
        FileInputStream in=new FileInputStream(new File("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\dbcp.properties"));
        prop.load(in);
        DataSource bds= BasicDataSourceFactory.createDataSource(prop);
        Connection conn=bds.getConnection();
        System.out.println(conn);
    }
}
