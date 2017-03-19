package com.jpmc.rdt.docmgmt.ocr.spring;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.jpmc.rdt.docmgmt.cache.CacheStartup;
import com.jpmc.rdt.docmgmt.spring.PropertySourcesUtility;
import com.jpmc.rdt.docmgmt.ta.util.FaxAcknowledgeEncryption;

/**
 * 
 * @author Hitesh Modi
 *
 */
@SpringBootApplication(scanBasePackages="com.jpmc.rdt.docmgmt.ocr, com.jpmc.rdt.docmgmt.database.persistence, com.jpmc.rdt.docmgmt.ta")
@EnableScheduling
@EnableAsync
@Import({LocalPropertiesConfiguration.class, RdpConfiguration.class})
public class OcrSpringConfiguration {
	
	@Value("${spring.datasource.username}")
	String username;

	@Value("${spring.datasource.password}")
	String password;
	
	@Value("${spring.datasource.driverClassName}")
	String driverClass;
	
	@Value("${spring.datasource.url}")
	String url;
	
	@Value("${faxacknowledge.email.host}")
	private String host = "mailhost.jpmchase.net";
	
	@Value("${faxacknowledge.email.port}")
	private String port = "25";
	
	@Value("${faxacknowledge.email.transportProtocol}")
	private String transportProtocol = "smtp";
	
	@Value("${faxacknowledge.email.sslTrust}")
	private String sslTrust = "mailhost.jpmchase.net";
	
	@Value("${faxacknowledge.email.smtpStarttlsEnable}")
	private String smtpStarttlsEnable = "true";
	
	@Resource(name="faxAckPasswordDecryptor")
	private FaxAcknowledgeEncryption decryptor;
	
	@Value("${faxacknowledge.password.encryption.encryptionKey}")
	private String encryptionKey;
	
	@Value("${faxacknowledge.password.encryption.initVector}")
	private String initVector;
	
	@Value("${faxacknowledge.dbwatcher.threadpoolsize}") 
	private int faxAckDbWatcherThreadPoolSize;
	
	@Value("${lux.spooler.threadpoolsize}")
	private int luxSpoolerThreadPoolSize;
	
	@Value("${hk.spooler.threadpoolsize}")
	private int hkSpoolerThreadPoolSize;
	
	
	@Bean
	public static PlaceholderConfigurerSupport propertyConfigurer() {
		PropertySourcesUtility propertyUtility = new PropertySourcesUtility();
		return propertyUtility;
	}

	@Bean
	public static CacheStartup cacheStartup() {
		CacheStartup cacheStartup = new CacheStartup();
		return cacheStartup;
	}
	
	@Bean
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(decryptor.decrypt(encryptionKey, initVector, password));
		return dataSource;
	}
	
	@Bean(name="taskExecutor")
	public ThreadPoolTaskExecutor faxAckTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(faxAckDbWatcherThreadPoolSize);
		executor.setMaxPoolSize(faxAckDbWatcherThreadPoolSize);
		return executor;
	}
	
	@Bean(name="luxSpoolerTaskExecutor")
	public ThreadPoolTaskExecutor luxSpoolerTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(luxSpoolerThreadPoolSize);
		executor.setMaxPoolSize(luxSpoolerThreadPoolSize);
		return executor;
	}
	
	@Bean(name="hkSpoolerTaskExecutor")
	public ThreadPoolTaskExecutor hkSpoolerTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(hkSpoolerThreadPoolSize);
		executor.setMaxPoolSize(hkSpoolerThreadPoolSize);
		return executor;
	}
	
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		return scheduler;
	}
	
	@Bean(name = "mailSender")
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));
		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.transport.protocol", transportProtocol);
		javaMailProperties.setProperty("mail.smtp.ssl.trust", sslTrust);
		javaMailProperties.setProperty("mail.smtp.starttls.enable", smtpStarttlsEnable);
		return mailSender;
	}
	
	
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(OcrSpringConfiguration.class, args);
    }
	
	
}
