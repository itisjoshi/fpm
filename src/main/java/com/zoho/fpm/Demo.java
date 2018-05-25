package com.zoho.fpm;

import java.util.logging.Logger;

/**
 *
 * Hello world!
 *
 */
public class Demo {
	
	private final static Logger LOGGER = Logger.getLogger(Demo.class.getName());
	
	public static void main(String[] args) {		  
		RFLearn.main(args);
		ClusterLearn.main(args);
	}
}