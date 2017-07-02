package org.bpt.ioc.component;

import org.springframework.stereotype.Component;

@Component("B")
public class B {

	public String getName() {
		return "b";
	}

}