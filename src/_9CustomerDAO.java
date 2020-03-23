import java.sql.Connection;
import java.sql.SQLException;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:11:25;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * 针对customers表中的数据，进行特定的增删改查的操作
 * 在这里定义接口规范
 *
 *
 */
public interface _9CustomerDAO {
    /**
     * 向customers表中插入一条记录
     * @param connection 连接
     * @param customer 要插入的记录的结果类
     */
    public void insert(Connection connection,Customer customer);

    /**
     * 根据customers中的id删除一条记录
     * @param connection 连接
     * @param id 要删除记录的id
     */
    public void deleteByID(Connection connection,int id);

    /**
     * 根据customers的数据修改成customer
     * @param connection 连接
     * @param customer 要修改成的结果类
     */
    public void update(Connection connection,Customer customer);
}
