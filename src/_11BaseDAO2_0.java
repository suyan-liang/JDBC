import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:14:13;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 *
 * 对BaseDAO的优化，把其定义为泛型类
 * 对于select操作，原来要传进去结果类，
 * 但是对于接口实现类模式，本来就是要操作这张特定表，你知道要操作的结果类，那就没必要再传类了，这是升级的地方
 *
 *
 * 定义这个类为泛型类，方法中使用的泛型都是类的泛型，所以就不必定义为泛型方法了
 * 开头的代码块很关键
 *
 */
//定义为泛型类
public abstract class _11BaseDAO2_0<T> {
    private Class<T> clazz=null;
    //这个不是静态的代码块，是在子类继承时明确的,生成对象时作用 xxxDAOImpl extends BaseDAO<xxx> implements  xxxDAO
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取父类泛型参数
        clazz= (Class<T>) actualTypeArguments[0];//获取到的是一个数组，第一个泛型参数是clazz
    }
    /**
     * 操作后直接commit的增删改操作
     * @param sql 要执行的SQL语句
     * @param args  填补占位符
     */
    public void update_commitnow(String sql,Object ...args) {
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
    public void update_nocommit( Connection connection,String sql,Object ...args) {
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
     * @param sql     要执行的SQL语句
     * @param args    要填补的占位符
     * @return        带有结果的list
     */
    public  List<T> Select_commitnow( String sql, Object ...args){
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
     * @param sql
     * @param args
     * @return
     */
    public  List<T> Select_nocommit(Connection connection, String sql, Object ...args){

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

    /**
     * 查询结果为单列查询，且考虑事务，不进行commit
     * @param connection 连接
     * @param sql   sql语句，查询的结果没有确定的结果类
     * @param args  填补
     * @return 返回含有结果的一个list
     */
    public List<T> Select_value(Connection connection,String sql,Object ...args) {
        PreparedStatement ps= null;
        ResultSet rs= null;
        List<T> list=new ArrayList<>();
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()){
                T t = (T) rs.getObject(1);
                list.add(t);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myJDBC.closeSources(null,ps,rs);
        }
        return null;
    }
    /**
     * 展示list中的数据
     */
    public void show_list(List<T> list){
        for (T t : list) {
            System.out.println(t.toString());
        }
    }
}
