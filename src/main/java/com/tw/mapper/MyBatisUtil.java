package com.tw.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisUtil {

    private static SqlSessionFactory sqlSessionFactory;

    private MyBatisUtil() {
    }

    static {
        String resource = "mybatis-config.xml";

        InputStream inputStream;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            Properties properties = new Properties();
            String connectURL = String.format(
                    "jdbc:mysql://%s:%s/%s?user=%s&password=%s&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull",
                    System.getenv().getOrDefault("DB_HOST", "127.0.0.1"),
                    System.getenv().getOrDefault("DB_PORT", "3307"),
                    System.getenv().getOrDefault("DB_NAME", "data_store"),
                    System.getenv().getOrDefault("DB_USERNAME", "mysql"),
                    System.getenv().getOrDefault("DB_PASSWORD", "mysql")
            );
            properties.put("db.url", connectURL);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
