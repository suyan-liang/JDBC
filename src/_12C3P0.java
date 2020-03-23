import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:14:35;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * 数据库连接池和线程池的套路差不多，好处也差不多
 *
 * 数据库由DataSource来表示，这是一个接口，通常由服务器来实现
 * 常见的开源数据库连接池
 * DBCP--速度快，但不稳定有bug
 * C3P0--速度慢，但稳定性高
 * Druid--阿里提供，速度快，稳定性高，工作中使用
 *
 */
public class _12C3P0 {
    public static void main(String[] args) throws PropertyVetoException, SQLException {
            test_getByConfig();
    }

    /**
     * 不使用配置文件演示一下原理
     * @throws PropertyVetoException
     * @throws SQLException
     */
    public static void test_getNoconfig() throws PropertyVetoException, SQLException {
        ComboPooledDataSource cpds=new ComboPooledDataSource();
        //基本设置的四项
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("hello,mysql");
        //设置管理的属性
        cpds.setInitialPoolSize(10);//比如初始池子中的容量
        Connection conn=cpds.getConnection();
        System.out.println(conn);

        //销毁连接池,一般不使用这个操作
        //DataSources.destroy(cpds);
    }

    /**
     * 通过配置文件获得数据库连接池的配置信息，创建数据库连接池进而获取连接
     * @throws SQLException
     */
    public static void test_getByConfig() throws SQLException {
        //这个名字是在xml文件中设置的   c3p0-config.xml
        ComboPooledDataSource cpds = new ComboPooledDataSource("myconfig");
        Connection connection=cpds.getConnection();
        System.out.println(connection);
    }

}
