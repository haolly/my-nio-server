package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import java.beans.PropertyVetoException;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by liuhao on 2015/9/18.
 */
public class ConnectionPool implements Closeable {
    private final ComboPooledDataSource pool;
    protected ConnectionPool(DBConfig config) {
        this.pool = createPool(config);
    }

    private static ComboPooledDataSource createPool(DBConfig config) {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass(config.driver);
        } catch (PropertyVetoException e) {
            throw new DBException(e,"Invalid jdbc driver class: " + config.driver);
        }
        // jdbc:mysql://localhost:port/dbname?user....
        String conStr = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true",
                config.host, config.db,config.userName, config.passWord);
        if(config.batchOptimze) {
            conStr += "&useServerPreparedStmts=false&rewriteBatchedStatements=true";
        }
        ds.setJdbcUrl(conStr);

        if(config.poolMin != 0) {
            ds.setMinPoolSize(config.poolMin);
        }
        if(config.poolMax != 0) {
            ds.setMaxPoolSize(config.poolMax);
        }
        if(config.checkoutTimeout != 0) {
            ds.setCheckoutTimeout(config.checkoutTimeout);
        }
        return ds;
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    @Override
    public void close() throws IOException {
        try {
            DataSources.destroy(pool);
        } catch (SQLException e) {
            // TODO add logger
            e.printStackTrace();
        }
    }
}
