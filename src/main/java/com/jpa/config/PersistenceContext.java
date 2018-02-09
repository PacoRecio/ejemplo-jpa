package com.jpa.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
//Enable Spring Data JPA by annotating the PersistenceContext class with the @EnableJpaRepositories annotation.
//Configure the base packages that are scanned when Spring Data JPA creates implementations for our repository interfaces.
@EnableJpaRepositories(basePackages = {
        "com.jpa.todo"
})
@EnableTransactionManagement
//Clase para configurar la capa persistente de la aplicacion
public class PersistenceContext {

	//Configuracion del Datasource bean
	// --Ensure that the close() method of the created DataSource object is invoked when the application context is closed.
	@Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
	
	/*
    Configure the database connection.
	We need to set the name of the JDBC driver class, the JDBC url, the username of database user, and the password of the database user.
	*/
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("db.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("db.password"));
 
    /*Create a new HikariDataSource object and return the created object.*/
        return new HikariDataSource(dataSourceConfig);
    }
	
	//Configure the entity manager factory bean
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, 
	                                                            Environment env) {
	   LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	   entityManagerFactoryBean.setDataSource(dataSource);
	   entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	   entityManagerFactoryBean.setPackagesToScan("com.jpa.todo");
	 
	   Properties jpaProperties = new Properties();
	     
	        //Configures the used database dialect. This allows Hibernate to create SQL
	        //that is optimized for the used database.
	        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
	 
	        //Specifies the action that is invoked to the database when the Hibernate
	        //SessionFactory is created or closed.
	        jpaProperties.put("hibernate.hbm2ddl.auto", 
	                env.getRequiredProperty("hibernate.hbm2ddl.auto")
	        );
	 
	        //Configures the naming strategy that is used when Hibernate creates
	        //new database objects and schema elements
	        jpaProperties.put("hibernate.ejb.naming_strategy", 
	                env.getRequiredProperty("hibernate.ejb.naming_strategy")
	        );
	 
	        //If the value of this property is true, Hibernate writes all SQL
	        //statements to the console.
	        jpaProperties.put("hibernate.show_sql", 
	                env.getRequiredProperty("hibernate.show_sql")
	        );
	 
	        //If the value of this property is true, Hibernate will format the SQL
	        //that is written to the console.
	        jpaProperties.put("hibernate.format_sql", 
	                env.getRequiredProperty("hibernate.format_sql")
	        );
	 
	        entityManagerFactoryBean.setJpaProperties(jpaProperties);
	 
	    return entityManagerFactoryBean;
	 }
	
	//create a transaction manager bean that integrates the JPA provider with the Spring transaction mechanism.
	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
	   JpaTransactionManager transactionManager = new JpaTransactionManager();
	   transactionManager.setEntityManagerFactory(entityManagerFactory);
	   return transactionManager;
	}
	     
	    //Add the other beans here
}
