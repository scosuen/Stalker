package com.scott.stalker.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DatasourceConfig {

	private static final String CLASS_NAME = "org.neo4j.jdbc.Driver";
	private static final String URL = "jdbc:neo4j://localhost:7474/";
	private static final String USER_NAME = "neo4j";
	private static final String PASSOWRD = "123457";

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(CLASS_NAME);
		ds.setUsername(USER_NAME);
		ds.setPassword(PASSOWRD);
		ds.setUrl(URL);
		return ds;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource()); // notice this is calling
		jdbcTemplate.afterPropertiesSet();
		return jdbcTemplate;
	}
	
	 @Bean
	 public PlatformTransactionManager txManager() {
	     return new DataSourceTransactionManager(dataSource());
	 }
}
