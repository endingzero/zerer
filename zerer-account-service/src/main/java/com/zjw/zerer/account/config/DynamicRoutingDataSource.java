package com.zjw.zerer.account.config;

import com.zjw.zerer.core.thread.RouterHolder;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: DynamicRoutingDataSource
 * @Description:
 * @author: jiewei
 * @date: 2019/11/27
 */
public class DynamicRoutingDataSource extends AbstractDataSource {

    public static String DEFAULT = "zerer_main";

    private Map<String, DataSource> resolvedDataSources = new ConcurrentHashMap<>();

    private DataSource resolvedDefaultDataSource;

    public void addDateSource(String lookupKey, DataSource dataSource) {
        resolvedDataSources.put(lookupKey, dataSource);
    }

    public void setResolvedDefaultDataSource(DataSource defaultTargetDataSource) {
        if (this.resolvedDefaultDataSource == null) {
            this.resolvedDefaultDataSource = defaultTargetDataSource;
            this.resolvedDataSources.put(DEFAULT, defaultTargetDataSource);
        }
    }

    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        String lookupKey = determineCurrentLookupKey();
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && lookupKey == null) {
            dataSource = this.resolvedDefaultDataSource;
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }

    public boolean containsLookupKey(String key) {
        return this.resolvedDataSources.containsKey(key);
    }

    private String determineCurrentLookupKey() {

        // 从共享线程中获取数据源名称
        if (RouterHolder.getTarget() == null) {
            return DEFAULT;
        }
        return RouterHolder.getTarget().getDatasource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }
}
