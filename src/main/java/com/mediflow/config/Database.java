package com.mediflow.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class Database {
    private static final Properties PROPS = new Properties();
    static {
        try (InputStream in = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("Missing src/main/resources/db.properties. Copy db.properties.example and configure it.");
            PROPS.load(in);
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    private Database() {}
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(PROPS.getProperty("db.url"), PROPS.getProperty("db.username"), PROPS.getProperty("db.password"));
    }
    public static void testConnection() throws Exception {
        try (Connection ignored = getConnection()) { }
    }
}
