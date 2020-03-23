import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:9:15;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * 事务处理 ACID
 * 要么commit，要么rollback到上一次commit，不会出现在中间的状态
 *
 * 数据一旦提交，再回滚就只能回滚到最近的一次提交时的状态
 *
 * DDL操作一旦执行都会自动提交（对表和库的操作）
 * DML默认自动提交，但可以通过设置set autocommit=false让其不自动提交
 * 关闭连接（connection）时自动提交数据
 *
 *
 * Java中设置事务的隔离级别
 *
 *         connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
 *         connection.getTransactionIsolation();
 *
 *         具体不演示了，和mysql差不多
 *
 */
public class _7transaction {
    public static void main(String[] args) throws Exception {



        Connection connection=myJDBC.getConnection();

        String sql="select user,password,balance from user_table where user=?";
        List<User> list=myCRUD.Select_nocommit(connection,User.class,sql,"CC");
        show_ans(list);
    }

    /**
     * 显示list中的内容
     * @param list 结果list
     * @param <T>  结果类
     */
    private static <T>void  show_ans(List<T> list){
        for (T t : list) {
            System.out.println(t.toString());
        }
    }
    /**
     * 考虑上事务的增删改操作
     * 1.设置自动提交为false
     * 2.catch里面回滚,finally里面恢复自动提交
     * 3.传递连接，update里面不要关闭传进来的连接
     */
    private static void test_transaction() {
        Connection connection= null;
        try {
            String sql1="update user_table set balance = ? where user=?";
            String sql2="update user_table set balance = ? where user=?";
            connection = myJDBC.getConnection();
            connection.setAutoCommit(false);
            myCRUD.update_nocommit(connection,sql1,900,"AA");
            System.out.println(10/0);
            myCRUD.update_nocommit(connection,sql2,1100,"BB");
            connection.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("网络异常");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("已回滚");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            myJDBC.closeSources(connection,null);
        }
    }

    /**
     * 不考虑事务的情况下进行的转账操作
     */
    private static void test_notransaction() {
        //在user_table表中，AA转给BB100元
        String sql1="update user_table set balance = ? where user=?";
        String sql2="update user_table set balance = ? where user=?";
        myCRUD.update_commitnow(sql1,900,"AA");
        //模拟异常
        System.out.println(10/0);
        myCRUD.update_commitnow(sql2,1100,"BB");
    }

}

    /**
     * 用于接收查询user_table表的结果类
     */
class User{
    private String user;
    private String password;
    private int balance;

    public User(String user, String password, int balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    public User() {
        super();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}

