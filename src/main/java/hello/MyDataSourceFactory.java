package hello;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by RobertXi on 10/6/15.
 */
public class MyDataSourceFactory {
    static String url = "jdbc:mysql://localhost:3306/whatdodb";
    static String dbName = "whatdodb";
    static String driver = "com.mysql.jdbc.Driver";
    static String dbUser = "wdadmin";
    static String password = "wdpwd";

    public static DataSource getMySQLDataSource() {
        MysqlDataSource mysqlDS=new MysqlDataSource();
        mysqlDS.setURL(url);
        mysqlDS.setUser(dbUser);
        mysqlDS.setPassword(password);
        mysqlDS.setDatabaseName(dbName);

        return mysqlDS;
    }
}
