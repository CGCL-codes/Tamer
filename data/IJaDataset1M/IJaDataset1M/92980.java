package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
public class SimplePooledConnectionFactory extends AConnectionFactory {

    private final String connectionUrl;

    public SimplePooledConnectionFactory(DatabaseSettings databaseSettings) throws StorageInitializationException {
        super(databaseSettings);
        String poolName = "rojacdb." + System.currentTimeMillis();
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(databaseSettings.getUrl(), databaseSettings.getUserName(), databaseSettings.getPassword());
        ObjectPool connectionPool = new GenericObjectPool<Object>(null, 20, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0, true, true);
        new PoolableConnectionFactory(connectionFactory, connectionPool, null, "SELECT 1+1", false, true);
        try {
            Class.forName("org.apache.commons.dbcp.PoolingDriver");
        } catch (ClassNotFoundException e) {
            throw new StorageInitializationException("Can not initialize pooling driver", e);
        }
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            driver.registerPool(poolName, connectionPool);
        } catch (SQLException e) {
            throw new StorageInitializationException("Can not obtain pooling driver", e);
        }
        connectionUrl = "jdbc:apache:commons:dbcp:" + poolName;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
