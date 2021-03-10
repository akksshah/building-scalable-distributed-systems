package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtility {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource hikariDataSource;

    static {
        config.setAutoCommit(true);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://purchases.cvst2wuziexb.us-east-1.rds.amazonaws.com/purchases");
        config.setMaximumPoolSize(60);
        config.setUsername("admin");
        config.setPassword("akksshah");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
