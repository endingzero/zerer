package com.zjw.zerer.account.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.base.Splitter;
import com.zjw.zerer.core.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @ClassName: RoutingDataSourceConfig
 * @Description:
 * @author: jiewei
 * @date: 2019/11/27
 */
@Slf4j
@Configuration
@MapperScan(basePackages = Constants.CUSTOM_PACKAGE, sqlSessionFactoryRef = "zererSqlSessionFactory")
public class RoutingDataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Value("${custom.datasource.names}")
    private String customNames;

    @Resource
    private Environment env;

    private DynamicRoutingDataSource dynamicDataSource;

    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("defaultDataSource") DataSource defaultDataSource) {


        // 采用是想AbstractRoutingDataSource的对象包装多数据源
        dynamicDataSource = new DynamicRoutingDataSource();

        // 默认数据源
        dynamicDataSource.setResolvedDefaultDataSource(defaultDataSource);

        // 配置多数据源
        Splitter.on(Constants.COMMA_SPILT).splitToList(customNames).forEach(dbName -> {
            DruidDataSource customDataSource = new DruidDataSource();
            customDataSource.setUrl(env.getProperty("custom.datasource." + dbName + ".url"));
            customDataSource.setUsername(env.getProperty("custom.datasource." + dbName + ".username"));
            customDataSource.setPassword(env.getProperty("custom.datasource." + dbName + ".password"));
            customDataSource.setDriverClassName(env.getProperty("custom.datasource." + dbName + ".driverClassName"));
            this.setDataSourceProperties(customDataSource);
            dynamicDataSource.addDateSource(dbName, customDataSource);
            log.info("加载数据源...[{}] [{}] ", dbName, customDataSource.getUrl());
        });
        return dynamicDataSource;

    }

    private void setDataSourceProperties(DruidDataSource dataSource) {
        dataSource.setInitialSize(dataSourceProperties.getInitialSize());
        dataSource.setMinIdle(dataSourceProperties.getMinIdle());
        dataSource.setMaxActive(dataSourceProperties.getMaxActive());
        dataSource.setMaxWait(dataSourceProperties.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(dataSourceProperties.getValidationQuery());
        dataSource.setTestWhileIdle(dataSourceProperties.getTestWhileIdle());
        dataSource.setTestOnBorrow(dataSourceProperties.getTestOnBorrow());
        dataSource.setTestOnReturn(dataSourceProperties.getTestOnReturn());
        dataSource.setPoolPreparedStatements(dataSourceProperties.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            dataSource.setFilters(dataSourceProperties.getFilters());
        } catch (SQLException e) {
            log.error("SQL Exception", e);
        }
        dataSource.setConnectionProperties(dataSourceProperties.getConnectionProperties());
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name = "zererSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource,
                                               ObjectProvider<Interceptor[]> interceptorsProvider,
                                               @Qualifier("zererGlobalConfig") GlobalConfig globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        // 指定数据源(这个必须有，否则报错)
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setVfs(SpringBootVFS.class);
        sqlSessionFactory.setPlugins(interceptorsProvider.getIfAvailable());
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "zererGlobalConfig")
    public GlobalConfig globalConfig(@Qualifier(value = "zererDbConfig") GlobalConfig.DbConfig dbConfig) {
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.setBanner(false);
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    @Bean(name = "zererDbConfig")
    public GlobalConfig.DbConfig dbConfig() {
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        //dbConfig.setDbType(DbType.MYSQL);
        dbConfig.setIdType(IdType.AUTO);
        dbConfig.setCapitalMode(true);
        return dbConfig;
    }
}

