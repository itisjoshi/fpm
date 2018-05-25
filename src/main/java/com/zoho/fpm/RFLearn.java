package com.zoho.fpm;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.ml.regression.RandomForestRegressionModel;
import org.apache.spark.ml.regression.RandomForestRegressor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class RFLearn {

	public static void main(String[] args) {

		SparkUtils.createSparkSession();

	    Dataset<Row> data = SparkUtils.readDataSetAsCSV("transaction.csv");

	    String[] featureCols = {"income","auto_spent_amount","auto_spent_count","food_spent_amount",
	    		"food_spent_count","entertainment_spent_amount","entertainment_spent_count",
	    		"clothing_spent_amount","clothing_spent_count","medical_spent_amount","medical_spent_count",
	    		"housing_spent_amount","housing_spent_count"};
	    VectorAssembler assembler = new VectorAssembler().setInputCols(featureCols).setOutputCol("features");
	    Dataset<Row> df2 = assembler.transform(data);
	    	    
	    StringIndexer labelIndexer = new StringIndexer().setInputCol("loan").setOutputCol("label");
	    Dataset<Row> df3 = labelIndexer.fit(data).transform(df2);
	    	    
	    VectorIndexerModel featureIndexer = new VectorIndexer()
	      .setInputCol("features")
	      .setOutputCol("indexedFeatures")
	      .setMaxCategories(6)
	      .fit(df3);
	    
	    Dataset<Row>[] splits = df3.randomSplit(new double[] {0.7, 0.3});
	    Dataset<Row> trainingData = splits[0];
	    Dataset<Row> testData = splits[1];

	    RandomForestRegressor rf = new RandomForestRegressor()
	      .setLabelCol("label")
	      .setFeaturesCol("indexedFeatures");

	    Pipeline pipeline = new Pipeline()
	      .setStages(new PipelineStage[] {featureIndexer, rf});

	    PipelineModel model = pipeline.fit(trainingData);

	    Dataset<Row> predictions = model.transform(testData);

	    predictions.select("prediction", "label", "user").show();

		IndexToString labelReverse = new IndexToString().setInputCol("label").setOutputCol("reversed_label");
		Dataset<Row> prediction_reversed = labelReverse.transform(predictions);
		prediction_reversed.show();
		
		prediction_reversed.select("reversed_label", "user").write()
	    .format("com.databricks.spark.csv")
	    .option("header", "true")
	    .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
	    .save("predicted_rf.csv");

	    RegressionEvaluator evaluator = new RegressionEvaluator()
	      .setLabelCol("label")
	      .setPredictionCol("prediction")
	      .setMetricName("rmse");
	    double rmse = evaluator.evaluate(predictions);
	    System.out.println("Root Mean Squared Error (RMSE) on test data = " + rmse);

	    RandomForestRegressionModel rfModel = (RandomForestRegressionModel)(model.stages()[1]);
	    System.out.println("Learned regression forest model:\n" + rfModel.toDebugString());
	}
}