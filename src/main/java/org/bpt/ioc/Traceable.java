package org.bpt.ioc;

public class Traceable {
	private final StackTraceElement[] ctorStack;

	public Traceable() {
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