package hello;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Created by RobertXi on 10/6/15.
 */
public class MyDataSourceFactory {

    static String url = "jdbc:mysql://localhost:3306/whatdodb";
    static String dbName = "whatdodb";
    static String driver = "com.mysql.jdbc.Driver";
    static String dbUser = "wdadmin";
    static String password = "wdpwd";

    public static final MyDataSourceFactory INSTANCE = new MyDataSourceFactory();

    private MysqlDataSource ds = null;

    private MyDataSourceFactory() {
        ds=new MysqlDataSource();
        ds.setURL(url);
        ds.setUser(dbUser);
        ds.setPassword(password);
        ds.setDatabaseName(dbName);
    }

    public DataSource getDataSource() {
        return this.ds;
    }
}
