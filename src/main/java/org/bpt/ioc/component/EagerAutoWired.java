package org.bpt.ioc.component;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class EagerAutoWired extends Autowired {
	
	public EagerAutoWired() {
		super();
		System.out.println("ctor EagerAutoWired");
	}
}