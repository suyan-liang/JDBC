import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:13:44;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 * 这里象征性地实现了几个，模式就是这样
 * 利用从BaseDAO继承的基本操作的函数，来实现接口中定义的特定的对表的操作
 */
public class _10CustomerDAOImpl extends BaseDAO implements _9CustomerDAO {
    @Override
    public void insert(Connection connection, Customer customer) {
        String sql="insert into customers(name,email,birth) values(?,?,?)";
        update_nocommit(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth());
    }

    @Override
    public void deleteByID(Connection connection, int id) {
        String sql="delete from customers where id=?";
        update_nocommit(connection,sql,id);
    }

    @Override
    public void update(Connection connection, Customer customer) {
        String sql="update customers set name=?,set email=?,set birth=? where id=?";
        update_nocommit(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
    }
}
