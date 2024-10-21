package org.as1iva.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final HikariConfig config = new HikariConfig("/application.properties");

    private static final HikariDataSource dataSource = new HikariDataSource(config);

    private ConnectionManager() {
    }

    public static Connection get() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}