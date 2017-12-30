package org.bpt.ioc.component;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LazyAutoWired extends Autowired {

	public LazyAutoWired() {
		super();
		System.out.println("ctor LazyAutoWired");
	}

}
