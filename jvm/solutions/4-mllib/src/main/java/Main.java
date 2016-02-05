import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

/**
 * @author Mario Macias (http://github.com/mariomac)
 */
public class Main {
	public static void main(String[] args) {
		// For learning and testing purposes, we will run Spark locally.
		// Use local[N] to run locally with N threads
		SparkConf config = new SparkConf(true).setAppName("power-models").setMaster("local[2]");
		SparkContext ctx = new SparkContext(config);

		PowerModeller pm = new PowerModeller(ctx);
		pm.buildAndValidate();

	}
}
