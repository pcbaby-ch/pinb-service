package com.pinb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages={"com.pinb.*"},exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)
public class App {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) { 
		LOG.info("#main() App启动开始");
		SpringApplication.run(App.class, args);
		LOG.info("#main() App启动结束");
	}
}
