package org.bpt.ioc.component;

import org.bpt.ioc.Traceable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LazyAutoWiredViaSetter extends Traceable {
	public LazyAutoWiredViaSetter() {
		super();
		System.out.println("ctor LazyAutoWiredViaSetter");
	}
}
