import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author:liang;
 * Date:2020/3/21;
 * Time:14:03;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 */

/**
 * 用于存储查询结果的类
 * ORM编程思想
 * 一个数据表对应一个java类
 * 表中的一条数据对应一个java对象
 * 表中的一个字段对应着java的一个属性
 */
class result{
    private int id;
    private String name;
    private String password;

    public result(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
class Customer{
    //首先，参数名必须和要查询的表的字段名一致或者后面的SQL语句中用as改成这个名后面用getColumnLabel，方便后面反射
    private int id;
    private String name;
    private String email;
    private Date birth;
    //这儿必须搞一个空参构造函数
    public Customer()
    {
        super();
    }
    public Customer(int id, String name, String email, Date date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = date;
    }

    //要设置get,set方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date date) {
        this.birth = date;
    }
}
public class _3selectdemo {
    public static void main(String[] args) {
        String sql="select id,name,email,birth from customers where id<=?";
        List<Customer> list=CustomerForQuery(sql,3);
        show_customer(list);
    }

    /**
     * 针对customers表的通用查询操作
     * select的列不用关心
     * @param sql 要执行的sql语句
     * @param args 要填的占位符的参数
     * @return 返回的结果列表
     */
    public static List<Customer> CustomerForQuery(String sql,Object ...args){
        Connection connection= null;
        PreparedStatement ps= null;
        ResultSet rs= null;
        List<Customer> list= null;

        try {
            connection = myJDBC.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <args.length ; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            //这个是结果集的元数据，里面有查询到的列数，以及各列的列名等数据
            ResultSetMetaData rsmd=rs.getMetaData();
            list = new ArrayList<>();
            while (rs.next())
            {
                //这里不能使用构造函数的方法来创建结果类了，因为不知道查的是谁
                Customer customer=new Customer();
                //这里采用的是反射的方法来设置对象的各属性的值
                for (int i = 0; i <rsmd.getColumnCount() ; i++) {
                    Object columValue=rs.getObject(i+1);
                    //如果是用别名的话，这里应是getColumnLabel,尽量使用Label,尽量别使用name
                    //如果没有别名，Label获取的就是列名
                    String columName=rsmd.getColumnName(i+1);
                    Class clazz=Class.forName("Customer");
                    Field field=clazz.getDeclaredField(columName);
                    field.setAccessible(true);
                    field.set(customer,columValue);
                }
                list.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps,rs);
        }
        return list;
    }
    /**
     * 测试一下select
     */
    private static void test_select() {
        Connection connection= null;
        PreparedStatement ps= null;
        ResultSet rs= null;//这点有区别，首先是返回一个结果集，其次是用的executeQuery函数
        try {
            connection = myJDBC.getConnection();
            //千万要注意，这里的SQL语句中的占位符都是当做字符串看待的，不要把要执行的语句设置成占位符，只有表中的字段可以设置成占位符
            String sql="select id,name,password from user where id=?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1,1);
            rs = ps.executeQuery();
            List<result> ans=new ArrayList<>();
            //此处的next的作用有二 一是判断有没有下一个，有的话返回true并下移一位，否则返回false，不动
            while (rs.next())
            {
                int id=rs.getInt(1);
                String name=rs.getString(2);
                String password=rs.getString(3);
                ans.add(new result(id,name,password));
            }
            show_ans(ans);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps,rs);
        }
    }

    /**
     * 打印list中的结果  result
     * @param list
     */
    public static void show_ans(List list) {
        for (Object o : list) {
            result rs=(result) o;
            System.out.println(rs.getId()+"..."+rs.getName()+"..."+rs.getPassword());
        }
    }

    /**
     * 打印list中结果  customer
     * @param list
     */
    public static void show_customer(List list) {
        for (Object o : list) {
            Customer cu=(Customer)o;
            System.out.println(cu.getId()+"..."+cu.getName()+"..."+cu.getEmail()+"..."+cu.getBirth());
        }
    }
}
