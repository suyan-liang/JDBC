import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:7:47;
 * Package Name:PACKAGE_NAME;
 *
 *
 *
 *
 * 使用PreparedStatement进行数据的批量插入
 * 由于update和delete天然就具有批量的特性，所以此处只探讨如何对insert进行批量操作
 *
 *
 * 需求：向goods表中插入20000条数据
 */
public class _6multiInsert {
    public static void main(String[] args) throws Exception {
        insert_3();
    }

    /**
     * 第一种批量插入数据的方法
     * 与数据库进行20000次交互
     * 每一条SQL语句都进行一次交互
     */
    public static void insert_1(){
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            String sql="insert into goods(name,price) values(?,?)";
            ps = connection.prepareStatement(sql);
            long start=System.currentTimeMillis();
            for (int i = 0; i <20000 ; i++) {
                //这里是随便填的，实际操作中会有专门的数据返回过来让我们填入
                ps.setObject(1,"name"+(i+1));
                ps.setObject(2,i+1);
                ps.execute();
            }
            long end=System.currentTimeMillis();
            System.out.println("花费时间"+((end-start)/1000)+"s");//花费时间14s
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }
    /**
     * 第二种批量插入数据的方式，考虑到IO流优化的时候我们使用了缓冲区buf数组，攒够一波再交互，这样大大减少了花费的时间
     *
     * 那个类似于缓冲区的东西是Batch
     * addBatch放到里面
     * executeBatch执行这里面的语句
     * clearBatch清空里面
     *
     * 但是mysql服务器默认关闭批处理，我们要通过在我们配置文件的url后面添加
     * ?rewriteBatchedStatements=true
     * 这样才能进行批处理
     * 或者
     * 导入新的MySQL jar包驱动，这个包支持
     *
     */
    public static void insert_2() {
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            String sql="insert into goods(name,price) values(?,?)";
            ps = connection.prepareStatement(sql);
            long start=System.currentTimeMillis();
            for (int i = 0; i <20040 ; i++) {
                //这里是随便填的，实际操作中会有专门的数据返回过来让我们填入
                ps.setObject(1,"name"+(i+1));
                ps.setObject(2,i+1);

                //!!!!!!!!!!!!!!此处不同
                ps.addBatch();//攒SQL语句
                //每500条执行一次
                if(i%500==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
                //把最后剩余的单独判断
                if(i==20039){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("花费时间"+((end-start))+"ms");//花费时间215ms
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }
    /**
     * 第三种批量插入数据的方式，final版本
     * 先不提交，等所有的SQL语句都执行完毕，再commit
     */
    public static void insert_3(){
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            String sql="insert into goods(name,price) values(?,?)";
            ps = connection.prepareStatement(sql);
            long start=System.currentTimeMillis();
            //设置自动提交为false
            connection.setAutoCommit(false);
            for (int i = 0; i <20040 ; i++) {
                //这里是随便填的，实际操作中会有专门的数据返回过来让我们填入
                ps.setObject(1,"name"+(i+1));
                ps.setObject(2,i+1);

                //!!!!!!!!!!!!!!此处不同
                ps.addBatch();//攒SQL语句
                //每500条执行一次
                if(i%500==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
                //把最后剩余的单独判断
                if(i==20039){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            //等SQL语句都执行完毕，再commit
            connection.commit();
            long end=System.currentTimeMillis();
            System.out.println("花费时间"+((end-start))+"ms");//花费时间172ms
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }

}
