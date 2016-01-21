import org.apache.spark.SparkConf;

/**
 * @author Mario Macias (http://github.com/mariomac)
 */
public class Main {
	public static void main(String[] args) {
		// For learning and testing purposes, we will run Spark locally.
		// Use local[N] to run locally with N threads
		SparkConf config = new SparkConf(true).setAppName("spark-template").setMaster("local");
		System.out.println("*** Testing Apache Spark in Java and Scala");
		System.out.println(TestJavaSpark.test(config));
		System.out.println(TestScalaSpark.test(config));
	}
}
