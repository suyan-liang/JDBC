import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Handler;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:18:14;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 */
public class _16selfResultSetHandler {
    public static void main(String[] args) throws Exception {
        QueryRunner qr=new QueryRunner();
        Connection conn=myJDBC.getConnection();
        String sql="select name,email,birth from customers where id=?";
        //这里用匿名内部类的方法自定义一个ResultSetHandler的实现类
        ResultSetHandler<Customer> handle=new ResultSetHandler<Customer>() {
            /**
             * 其实就是我们处理结果方法的代码封装成的一个方法
             * @param resultSet
             * @return
             * @throws SQLException
             */
            @Override
            public Customer handle(ResultSet resultSet) throws SQLException {
                System.out.println("在这里处理结果集");
                System.out.println("query方法的返回值其实就是这个函数的返回值，假设返回null");
                return null;
            }
        };
        Customer customer=qr.query(conn,sql,handle,1);
        System.out.println(customer);
    }
}
