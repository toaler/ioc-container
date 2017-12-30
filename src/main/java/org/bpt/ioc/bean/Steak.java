package org.bpt.ioc.bean;

import org.bpt.ioc.Traceable;
import org.bpt.util.Util;

public class Steak extends Traceable {
	public Steak() {
		super();
		Util.log("ctor", "Steak.java");
	}
}