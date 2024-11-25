package ru.javaops.masterjava.config;

import ru.javaops.masterjava.persist.DBIProvider;

import java.sql.DriverManager;

public class DbConfig {
    public static void initDBI(String dbUrl, String dbUser, String dbPassword) {
        DBIProvider.init(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PostgreSQL driver not found", e);
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        });
    }
}
