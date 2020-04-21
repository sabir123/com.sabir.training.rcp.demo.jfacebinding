package com.sabir.training.rcp.demo.jfacebinding;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.sabir.training.rcp.demo.jfacebinding.messages"; //$NON-NLS-1$
	public static String View_0;
	public static String View_1;
	public static String View_2;
	public static String View_3;
	public static String View_4;
	public static String View_5;
	public static String View_6;
	public static String View_7;
	static {
		// initialize resource bundle
		System.out.println("Messages.enclosing_method()");
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
