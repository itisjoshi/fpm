package com.zoho.fpm;

import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class ClusterLearn {


    public static void main(String[] args) {
		SparkUtils.createSparkSession();
	
	    Dataset<Row> dataSet = SparkUtils.readDataSetAsCSV("transaction.csv");
	
	    String[] featureCols = {"auto_spent_amount","food_spent_amount",
	            "entertainment_spent_amount",
	            "clothing_spent_amount","medical_spent_amount",
	            "housing_spent_amount"};
	    VectorAssembler assembler = new VectorAssembler().setInputCols(featureCols).setOutputCol("features");
	    Dataset<Row> df2 = assembler.transform(dataSet);
	
	
	    KMeans kmeans = new KMeans().setMaxIter(1000).setK(5);
	    KMeansModel model = kmeans.fit(df2);
	
	    Dataset<Row> predictions = model.transform(df2);

	    predictions.show();
	    predictions.select("prediction", "loan", "user").show(900);
	    predictions.select("prediction", "loan", "user").write()
	            .format("com.databricks.spark.csv")
	            .option("header", "true")
	            .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
	            .save("predicted_cluster.csv");
	
    }
}
