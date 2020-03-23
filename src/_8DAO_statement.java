import java.sql.Connection;
import java.util.List;

/**
 * Author:liang;
 * Date:2020/3/23;
 * Time:10:18;
 * Package Name:PACKAGE_NAME;
 *
 *
 *
 *
 * 此处陈述一种思想
 * DAO是对通过Java代码封装的一些对于数据库的增删改查的通用基本操作
 * 我先把这些代码封装到BaseDAO中，然后你如果根据特定表中的特定数据，进行特定操作，就定义一个针对于特定表的接口，
 * 然后创建一个实现类继承BaseDAO,然后implements接口，添加属于表的特定操作
 *
 * 通过接口定义规范，然后实现这些接口
 *
 * 接口+实现类的模式
 */
public class _8DAO_statement {
    public static void main(String[] args) throws Exception {
    }
}
