package com.yourdomain.common.database.config;

import com.yourdomain.common.database.mapper.CommonEntityMapper;
import com.yourdomain.common.secretmanager.service.SecretRefreshService;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@AutoConfiguration
@EnableScheduling
@EnableConfigurationProperties(DatabaseProperties.class)
@MapperScan(basePackageClasses = CommonEntityMapper.class)
public class CommonDatabaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SecretRefreshService.class)
    @ConditionalOnClass(SecretRefreshService.class)
    public DatabaseSecretMapper databaseSecretMapper() {
        return new DatabaseSecretMapper();
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnBean(SecretRefreshService.class)
    public DataSource rotatingDataSource(SecretRefreshService secretRefreshService,
            DatabaseSecretMapper mapper,
            DatabaseProperties properties) {
        if (!properties.isRotateEnabled()) {
            return staticDataSource(properties);
        }
        return new RotatingDataSource(secretRefreshService, mapper);
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource commonDataSource(DatabaseProperties properties) {
        return staticDataSource(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RotatingDataSource.class)
    public DatabaseRotationScheduler databaseRotationScheduler(RotatingDataSource rotatingDataSource) {
        return new DatabaseRotationScheduler(rotatingDataSource);
    }

    @Bean
    @ConditionalOnMissingBean(name = "commonDatabaseRotationJob")
    @ConditionalOnBean(DatabaseRotationScheduler.class)
    public Runnable commonDatabaseRotationJob(DatabaseRotationScheduler scheduler, DatabaseProperties properties) {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelayString = "#{@commonDatabaseProperties.rotateScanInterval.toMillis()}")
            public void run() {
                scheduler.refresh();
            }
        };
    }

    @Bean("commonDatabaseProperties")
    @ConditionalOnMissingBean(name = "commonDatabaseProperties")
    public DatabaseProperties commonDatabasePropertiesBean(DatabaseProperties properties) {
        return properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    private DataSource staticDataSource(DatabaseProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(properties.jdbcUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }
}
