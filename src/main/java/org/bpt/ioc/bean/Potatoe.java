package org.bpt.ioc.bean;

import org.bpt.ioc.Traceable;
import org.bpt.util.Util;

public class Potatoe extends Traceable {
	public Potatoe() {
		super();
		Util.log("ctor", "Potatoe.java");
	}
}