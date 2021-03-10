package dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.var;

@SuppressWarnings("unused")
@Deprecated
public class JdbcDriver {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/aakash_test";

    // JDBC Database Credentials
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "akksshah";

    @Getter
    private static GenericObjectPool connectionPool = null;

    public DataSource setUpPool() throws Exception {
        Class.forName(JDBC_DRIVER);

        // Creates an Instance of GenericObjectPool That Holds Our Pool of Connections Object!
        connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(5);

        // Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create the Connection Object!
        ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

        // Creates a Pool-ableConnectionFactory That Will Wraps the Connection Object Created by the ConnectionFactory to Add Object Pooling Functionality!
        PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, connectionPool, null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }

    public void printDbStat() {
        System.out.println("Max: " + getConnectionPool().getMaxActive()
                               + ", Active: " + getConnectionPool().getNumActive()
                               + ", Idle: " + getConnectionPool().getNumIdle());
    }

    public static void main(String[] args) {
        var dataSource = new BasicDataSource();
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dataSource.setUrl(JDBC_DB_URL);
        dataSource.setUsername("root");
        dataSource.setPassword("akksshah");
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(60);
        try {
            var connection = dataSource.getConnection();
            String query = "select * from purchases.purchase";
            var preparedStatement = connection.prepareStatement(query);
            var result = preparedStatement.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("LastName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
