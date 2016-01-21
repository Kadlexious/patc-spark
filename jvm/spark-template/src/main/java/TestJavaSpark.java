import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

/**
 * @author Mario Macias (http://github.com/mariomac)
 */
public class TestJavaSpark {
	public static String test(SparkConf configuration) {
		JavaSparkContext sc = new JavaSparkContext(configuration);
		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1,2,3,4,5,6));
		String message = "Java's Spark says: " + rdd.count() +
					" elements that sum " + rdd.reduce((a, b) -> a+b);
		sc.close();
		return message;
	}
}
