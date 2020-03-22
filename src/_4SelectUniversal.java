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
 * Date:2020/3/22;
 * Time:16:52;
 * Package Name:PACKAGE_NAME;
 *
 *
 * 普适的select操作
 *
 * Customer资源类在3里面，只需要在这里面创建一个order的资源类
 *
 * 实现普适的select的关键在于泛型的使用
 *
 *
 *
 *
 */

/**
 * Order表的结果类，属性可以不用与字段同名
 */
class Order{
    private int orderID;
    private String orderName;
    private Date orderDate;

    public Order() {
        super();
    }

    public Order(int orderID, String orderName, Date orderDate) {
        this.orderID = orderID;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
public class _4SelectUniversal {
    public static void main(String[] args) {
//        String sql="select id,name,email,birth from customers where id=?";
//        List<Customer> list=new ArrayList<Customer>();
//        list=Select(Customer.class,sql,1);
//        print(list);
        //千万要注意的一点，如果表名和关键字相同，一定要加上着重号
        String sql="select order_id as orderID,order_name as orderName,order_date as orderDate from `order` where order_id=?";
        List<Order> list=new ArrayList<Order>();
        list=Select(Order.class,sql,1);
        print(list);
    }

    /**
     *
     * @param clazz 要查询表的字节码文件
     * @param sql   SQL语句
     * @param args  要填补占位符的参数
     * @param <T>   查询表的结果类
     * @return   返回泛型为结果类的List
     */
    public static <T> List<T> Select(Class<T> clazz, String sql, Object ...args){
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
    public static<T> void print(List<T> list) {
        for (T t : list) {
            System.out.println(t.toString());
        }
    }
}
