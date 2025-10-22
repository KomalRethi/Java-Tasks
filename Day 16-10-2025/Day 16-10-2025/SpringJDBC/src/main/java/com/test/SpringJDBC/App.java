package com.test.SpringJDBC;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.ApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    	JdbcTemplate jdbctemplate = context.getBean(JdbcTemplate.class);
    	
    	String sql = "INSERT INTO tdb(first_name, last_name) values ('Amith','Patel')";
    	int count = jdbctemplate.update(sql);
    	if(count > 0) {
    		System.out.println("Insertion successfully!!");
    	}
    	
    	else {
    		System.out.println("Failed Insertion");
    	}
    	
    }
}
