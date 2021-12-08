package ru.vsu.plannerbackendv2.dao.db;

import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectFromProperties;
import ru.vsu.plannerbackendv2.di.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {

    @InjectFromProperties
    private String DB_URL;

    @InjectFromProperties
    private String DB_USER;

    @InjectFromProperties
    private String DB_PASSWORD;

    @InjectFromProperties
    private String driver;

    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find JDBC driver: " + e.getMessage(), e);
        }
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get Connection: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            else
                return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public String getDB_USER() {
        return DB_USER;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public String getDriver() {
        return driver;
    }
}
