package org.efymich.myapp.config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LiquibaseConfig {

    public final static String CHANGELOGFILE = "/db/changelog/master.xml";

    @SneakyThrows
    public Connection getConnection() {
        Properties properties = new Properties();
        properties.load(LiquibaseConfig.class.getClassLoader().getResourceAsStream("/liquibase/liquibase.properties"));
        Class.forName("oracle.jdbc.driver.OracleDriver");

        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }
}
