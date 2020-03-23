import java.util.Date;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:18:12;
 * Package Name:PACKAGE_NAME;
 * 需求：
 * 步骤：
 */
public class Customer{
    //首先，参数名必须和要查询的表的字段名一致或者后面的SQL语句中用as改成这个名后面用getColumnLabel，方便后面反射
    private int id;
    private String name;
    private String email;
    private Date birth;
    //这儿必须搞一个空参构造函数

    public Customer() {
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}