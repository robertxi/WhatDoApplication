package com.robert.whatdo.service;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by RobertXi on 10/6/15.
 */
public class MyDataSourceFactory {

//    static String url = "jdbc:mysql://localhost:3306/whatdodb";
//    static String dbName = "whatdodb";
//    static String driver = "com.mysql.jdbc.Driver";
//    static String dbUser = "wdadmin";
//    static String password = "wdpwd";

    public static final MyDataSourceFactory INSTANCE = new MyDataSourceFactory();

    private MysqlDataSource ds = null;

    private MyDataSourceFactory() {
        Properties props = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/Users/RobertXi/IdeaProjects/gs-spring-boot/initial/src/main/java/com/robert/whatdo/service/db.properties");
            props.load(fis);
            ds = new MysqlDataSource();
            ds.setURL(props.getProperty("DB_URL"));
            ds.setUser(props.getProperty("DB_USERNAME"));
            ds.setPassword(props.getProperty("DB_PASSWORD"));
            ds.setDatabaseName(props.getProperty("DB_DATABASE"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return this.ds;
    }
}
