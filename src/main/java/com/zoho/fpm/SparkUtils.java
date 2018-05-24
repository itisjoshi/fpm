package com.zoho.fpm;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkUtils {

	public static SparkSession sparkSession = null;

	/**
	* Initializes a spark session in local
	* @return sparkSession
	*/
	public static SparkSession createSparkSession() {
		
		if(sparkSession == null) {
			//Enable spark UI only if development mode is true
			SparkSession session = SparkSession.builder().master("local")
					.appName("spark").config("spark.sql.warehouse.dir", "data/")
					.config("spark.ui.enabled", false).getOrCreate();
			sparkSession = session;
			return session;
		}
		return sparkSession;
	}

	public static Dataset<Row> readDataSetAsCSV(String path) {
		return SparkUtils.sparkSession.read()
				.option("header", "true")
				.option("inferSchema", "true")
				.option("multiLine", true)
				.option("delimiter", ",")
				.format("csv")
				.load(path);
	}

}
