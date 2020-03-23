import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:7:54;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 *
 * 增删改查的一个工具类
 */
public class myCRUD {
    /**
     * 操作后直接commit的增删改操作
     * @param sql 要执行的SQL语句
     * @param args  填补占位符
     */
    public static void update_commitnow(String sql,Object ...args) {
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }

    /**
     * 考虑事务的增删改操作，不会立即提交，提交放到外面去做
     *
     * 把连接当做参数传递进来
     * @param connection  连接
     * @param sql
     * @param args
     */
    public static void update_nocommit( Connection connection,String sql,Object ...args) {
        PreparedStatement ps= null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(null,ps);//这里connection不要关闭，否则就会提交了
        }
    }

    /**
     * 普适的查询操作
     * @param clazz  要操作表的字节码文件
     * @param sql     要执行的SQL语句
     * @param args    要填补的占位符
     * @param <T>     要查询表的结果类
     * @return        带有结果的list
     */
    public static <T> List<T> Select_commitnow(Class<T> clazz, String sql, Object ...args){
        Connection connection= null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        List<T> list=null;
        try {
            //获取查询结果
            connection = myJDBC.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd=rs.getMetaData();
            //把查询结果放到结果类中，并添加到list中
            list = new ArrayList<>();
            while (rs.next())
            {
                T t=clazz.newInstance();
                for (int i = 0; i <rsmd.getColumnCount() ; i++) {
                    String field_name=rsmd.getColumnLabel(i+1);
                    Object value=rs.getObject(i+1);
                    Field field=clazz.getDeclaredField(field_name);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps,rs);
        }
        return list;
    }

    /**
     * 不进行提交的查询操作
     * @param connection
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> Select_nocommit(Connection connection,Class<T> clazz, String sql, Object ...args){

        PreparedStatement ps= null;
        ResultSet rs= null;
        List<T> list=null;
        try {
            //获取查询结果

            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd=rs.getMetaData();
            //把查询结果放到结果类中，并添加到list中
            list = new ArrayList<>();
            while (rs.next())
            {
                T t=clazz.newInstance();
                for (int i = 0; i <rsmd.getColumnCount() ; i++) {
                    String field_name=rsmd.getColumnLabel(i+1);
                    Object value=rs.getObject(i+1);
                    Field field=clazz.getDeclaredField(field_name);
                    field.setAccessible(true);
                    field.set(t,value);
                }
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(null,ps,rs);
        }
        return list;
    }

}
