import org.apache.spark.SparkConf;

/**
 * @author Mario Macias (http://github.com/mariomac)
 */
public class Main {
	public static void main(String[] args) {
		// For learning and testing purposes, we will run Spark locally.
		// Use local[N] to run locally with N threads
		SparkConf config = new SparkConf(true).setAppName("spark-template").setMaster("local[2]");
		System.out.println("*** Testing Apache Spark in Java and Scala");
		ClusterDataAnalyzer.test(config);
	}
}
