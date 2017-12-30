package org.bpt.ioc.bean;

import org.bpt.ioc.Traceable;
import org.bpt.util.Util;

public class IceCream extends Traceable {
	public IceCream() {
		super();
		Util.log("ctor", "IceCream.java");
	}
}
