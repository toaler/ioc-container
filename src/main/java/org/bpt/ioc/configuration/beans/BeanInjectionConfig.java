package org.bpt.ioc.configuration.beans;

import org.bpt.ioc.bean.Dinner;
import org.bpt.ioc.bean.IceCream;
import org.bpt.ioc.bean.Potatoe;
import org.bpt.ioc.bean.Steak;
import org.bpt.util.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class BeanInjectionConfig {

	@Bean(value = "Dinner")
	public Dinner dinner(Steak steak, Potatoe potatoe, @Lazy IceCream iceCream) {
		Util.log("bean factory dinner", "BeanInjectionConfig.java");
		return new Dinner(steak, potatoe, iceCream);
	}

	@Bean(value = "Steak")
	@Lazy
	public Steak steak() {
		Util.log("bean factory steak", "BeanInjectionConfig.java");
		return new Steak();
	}

	@Bean(value = "Potatoe")
	@Lazy
	public Potatoe potatoe() {
		Util.log("bean factory potatoe", "BeanInjectionConfig.java");
		return new Potatoe();
	}

	@Bean(value = "IceCream")
	@Lazy
	public IceCream iceCream() {
		Util.log("bean factory ice cream", "BeanInjectionConfig.java");
		return new IceCream();
	}
}
