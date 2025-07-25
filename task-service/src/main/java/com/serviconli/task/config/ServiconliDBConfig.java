package com.serviconli.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ServiconliDBConfig {

    @Value("${serviconli.datasource.url}")
    private String url;

    @Value("${serviconli.datasource.username}")
    private String username;

    @Value("${serviconli.datasource.password}")
    private String password;

    @Bean(name = "serviconliDataSource")
    public DataSource serviconliDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean(name = "serviconliJdbcTemplate")
    public JdbcTemplate serviconliJdbcTemplate() {
        return new JdbcTemplate(serviconliDataSource());
    }
}
