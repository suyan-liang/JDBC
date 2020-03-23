import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:16:05;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * Druid数据库连接池
 */
public class _14Druid {
    public static void main(String[] args) throws Exception {
        getByConfig();
    }

    /**
     * 和DBCP很类似
     * 在实际使用过程中，把导入配置文件的操作放在静态代码块中进行
     * @throws Exception
     */
    public static void getByConfig() throws Exception {
        Properties prop=new Properties();
        InputStream in=new FileInputStream(new File("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\druid.properties"));
        prop.load(in);
        DataSource ds = DruidDataSourceFactory.createDataSource(prop);
        Connection conn=ds.getConnection();
        System.out.println(conn);
    }
}
