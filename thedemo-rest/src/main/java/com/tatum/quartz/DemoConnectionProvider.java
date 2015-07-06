package com.tatum.quartz;

import org.quartz.utils.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ComponentScan
public class DemoConnectionProvider implements ConnectionProvider {

    @Autowired
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() throws SQLException {
    }

    @Override
    public void initialize() throws SQLException {

    }
}
