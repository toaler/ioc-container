package org.bpt.ioc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component(value="LazyComponent")
@Lazy
public class LazyComponent {
	
	public LazyComponent() {
		System.out.println("ctor LazyComponent");
	}

	@Autowired @Lazy
	private LazyAutoWired lazyAutoWired;

	@Autowired
	private EagerAutoWired eagerAutoWired;

	public LazyAutoWired getLazyAutoWired() {
		return lazyAutoWired;
	}

	public EagerAutoWired getEagerAutoWired() {
		return eagerAutoWired;
	}
}
