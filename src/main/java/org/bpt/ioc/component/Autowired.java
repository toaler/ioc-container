package org.bpt.ioc.component;

public class Autowired {
	private final StackTraceElement[] ctorStack;

	public Autowired() {
		ctorStack = getStack();
	}
	public StackTraceElement[] getCtorStack() {
		return ctorStack;
	}
	
	public StackTraceElement[] getAccessPath() {
		return getStack();
	}
	
	private StackTraceElement[] getStack() {
		return Thread.currentThread().getStackTrace();
	}
}