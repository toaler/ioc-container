package org.bpt.ioc.configuration;

import org.bpt.ioc.bean.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.bpt.ioc.component")
public class TestConfig {

	@Bean("A")
	public A a() {
		return new A();
	}
}
