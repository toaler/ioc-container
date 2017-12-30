package org.bpt.util;

public class Util {
	public static String msg(String action, String msg) {
		return String.format("action=%-12s msg=%s", action, msg);
	}

	public static String msg(String action) {
		return msg(action, "");
	}
	
	public static void log(String action, String msg) {
		System.out.println(msg(action, msg));
	}
	
	public static void log(String action) {
		System.out.println(msg(action));
	}
}