package org.bpt.ioc.bean;

import org.bpt.ioc.Traceable;
import org.bpt.util.Util;

public class Dinner extends Traceable {

	private final Steak steak;
	private final Potatoe potatoe;
	private final IceCream iceCream;

	public Dinner(Steak steak, Potatoe potatoe, IceCream iceCream) {
		super();
		Util.log("ctor", "Dinner.java");
		this.steak = steak;
		this.potatoe = potatoe;
		this.iceCream = iceCream;
	}

	public Steak getSteak() {
		return steak;
	}

	public Potatoe getPotatoe() {
		return potatoe;
	}

	public IceCream getIceCream() {
		return iceCream;
	}
}