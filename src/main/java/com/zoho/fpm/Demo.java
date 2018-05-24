package com.zoho.fpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.spark_project.dmg.pmml.Array;

/**
 *
 * Hello world!
 *
 */
public class Demo {
	
	private final static Logger LOGGER = Logger.getLogger(Demo.class.getName());
	
	public static void main(String[] args) {		  
		Long user_id;
		Long income;
		Long auto_spent_amount;
		Long auto_spent_count;
		Long food_spent_amount;
		Long food_spent_count;
		Long entertainment_spent_amount;
		Long entertainment_spent_count;
		Long clothing_spent_amount;
		Long clothing_spent_count;
		Long medical_spent_amount;
		Long medical_spent_count;
		Long housing_spent_amount;
		Long housing_spent_count;
		String loan;
		
		for(int i = 751; i<=1000; i++) {
			System.out.println(i+","+randomNumberInRange(30000, 100000) +","+
					randomNumberInRange(400, 8000) +","+randomNumberInRange(4, 10) +","+
					randomNumberInRange(200, 2500) +","+randomNumberInRange(6, 12) +","+
					randomNumberInRange(1000, 3000) +","+randomNumberInRange(6, 8) +","+
					randomNumberInRange(3000, 4000) +","+randomNumberInRange(4, 9) +","+
					randomNumberInRange(500, 1800) +","+randomNumberInRange(5, 9) +","+
					randomNumberInRange(500, 1500) +","+randomNumberInRange(8, 18) +","+
					"personal_loan");
		}

		
	}

	   public static int randomNumberInRange(int min, int max) {
	        Random random = new Random();
	        return random.nextInt((max - min) + 1) + min;
	    }
}