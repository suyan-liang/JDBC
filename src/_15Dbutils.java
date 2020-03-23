import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.util.Date;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:17:19;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 * Dbutils是一个含有封装好的增删改查操作的工具类
 * 用其中的QueryRunner进行具体的增删改查操作
 *
 */
public class _15Dbutils {
    public static void main(String[] args) throws Exception {
        select_1();
    }

    /**
     * 使用QueryRunner进行增删改操作，都是update函数
     * 1.获取一个QueryRunner对象
     * 2.剩下就和原来一样了，获取连接，SQL语句，qr.操作，关资源  都一样
     * @throws Exception
     */
    public static void test_insert() throws Exception {
        Connection conn= null;
        try {
            QueryRunner queryRunner=new QueryRunner();
            conn = myJDBC.getConnection();
            String sql="insert into customers(name,email,birth) values(?,?,?)";
            queryRunner.update(conn,sql,"哈哈哈","haha@163.com","1999-09-12");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(conn,null);
        }
    }
    /**
     * 使用QueryRunner进行查询操作
     * 使用query函数
     * public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params)
     * ResultSetHandler<T>是一个接口，它有许多实现类
     * BeanHandler<T>  ---返回结果是一个类
     * BeanListHandler<T> ---返回一个结果的List
     * ScalarHandler ---查询特殊值时使用，返回一个object对象
     * 还有一些其他的不一一说了
     *
     *
     * 这里不知道为啥老是报错，明明加上无参构造了
     * 问题解决：这个资源类好像要是public型，在一个单独的java文件中才行
     */
    public static void select_1() throws Exception {
        QueryRunner qr=new QueryRunner();
        String sql="select id,name,email,birth from customers where id=? ";
        Connection conn=myJDBC.getConnection();
        BeanHandler<Customer> bh=new BeanHandler<>(Customer.class);
        Customer ans = qr.query(conn, sql, bh,22);
        System.out.println(ans);
    }
    /**
     * 关闭资源
     *
     * Dbutils.close();一次关一个
     * 或者
     * Dbutils.closeQuietly()
     */

}


