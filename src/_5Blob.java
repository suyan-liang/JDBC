import com.sun.corba.se.pept.transport.ConnectionCache;

import java.io.*;
import java.sql.*;
import java.util.regex.Pattern;

/**
 * Author:liang;
 * Date:2020/3/22;
 * Time:18:09;
 * Package Name:PACKAGE_NAME;
 *
 *
 * 需求：操作Blob类型数据
 * Blob型可以存储二进制信息
 *
 * 分类       最大容量
 * TinyBlob  255B
 * Blob      65K
 * MediumBlob  16M
 * LongBlob    4G
 *
 *
 * 有一个问题，指定了相关Blob类型后还报错， xxx to large，就去mysql的安装目录下修改my.ini文件下
 * 添加配置信息
 * max_allowed_packet=16M
 * 而后重启mysql服务
 *
 * 默认的packet是1M
 *
 * Packet for query is too large
 */
public class _5Blob {
    public static void main(String[] args) throws Exception {
        select_blob();
//        update_blob();
    }

    /**
     * 和正常的修改其实区别不大，就是把setObject 改成setBlob，参数传入文件流即可
     *
     * 这里进行简单的测试
     *
     * 增删改一致，大概都是这个样子
     * @throws Exception
     */
    public static void update_blob()  {
        Connection connection= null;
        PreparedStatement ps= null;
        try {
            connection = myJDBC.getConnection();
            String sql="insert into customers(name,email,birth,photo) values (?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setObject(1,"阿离");
            ps.setObject(2,"ali@163.com");
            ps.setObject(3,"2000-03-16");
            ps.setBlob(4,new FileInputStream(new File("C:\\Users\\liang\\IdeaProjects\\JDBC\\src\\ali.jpg")));
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myJDBC.closeSources(connection,ps);
        }
    }

    /**
     * 获取blob类型的数据，并下载到本地
     */
    public static void select_blob() throws Exception {
        Connection connection= null;
        PreparedStatement ps= null;
        InputStream in=null;
        FileOutputStream fos=null;
        try {
            connection = myJDBC.getConnection();
            String sql="select photo from customers where id=?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1,22);
            ResultSet rs=ps.executeQuery();
            if (rs.next())
            {
                //这是区别所在，就是获取的时候用Blob对象来接收，再通过这个对象生成一个输入流
                Blob blob=rs.getBlob("photo");
                in=blob.getBinaryStream();
                fos=new FileOutputStream(new File("E://ali.jpg"));
                byte[] buf=new byte[1024];
                int len=0;
                while ((len=in.read(buf))!=-1){
                    fos.write(buf,0,len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(in!=null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos!=null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myJDBC.closeSources(connection,ps);
        }
    }
}
