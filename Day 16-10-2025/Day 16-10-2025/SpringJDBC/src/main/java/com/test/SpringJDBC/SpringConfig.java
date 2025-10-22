package com.test.SpringJDBC;

import java.sql.DriverManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class SpringConfig {
	
	@Bean
	public DriverManagerDataSource myDataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		datasource.setUrl("jdbc:mysql://localhost:3306/testdb");
		datasource.setUsername("root");
		datasource.setPassword("Komal@321");
		
		return datasource;
	} 
	
	@Bean
	public JdbcTemplate myJdbcTemplate() {
		
		/*
		 * JdbcTemplate jdbctemplate = new JdbcTemplate();
		 * jdbctemplate.setDataSource(myDataSource()); return jdbctemplate;
		 */
		return new JdbcTemplate(myDataSource());
	}
}
