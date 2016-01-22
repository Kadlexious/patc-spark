import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author Mario Macias (http://github.com/mariomac)
 */
public class ProminenceCalculatorJava {
	public static void main(String[] args) {
		SparkConf config = new SparkConf(true).setAppName("prominence-calculator").setMaster("local");
		JavaSparkContext sparkContext = new JavaSparkContext(config);

		Pattern characterName = Pattern.compile("^[A-Z ]+$");

		Function2<Integer,Integer,Integer> reducer = (a,b) -> a+b;

		JavaPairRDD appearings = sparkContext.textFile("hamlet.txt")
			.filter(line -> characterName.matcher(line).matches())
			.mapToPair(line -> new Tuple2(line, 1))
				.reduceByKey(reducer)
				.persist(StorageLevel.MEMORY_ONLY());


		Consumer<Tuple2<String,Integer>> printCharacter = t -> System.out.println("\t"+t._1()+": " + t._2() + " times.");

		System.out.println("*** The 5 names that appear the most are: ");
		appearings
			.mapToPair(x -> ((Tuple2)x).swap()) // DIRTY HACK: since Java RDDs do not support "sortBy"
			.sortByKey(false)
			.mapToPair(x -> ((Tuple2)x).swap())
			.take(5)
			.forEach(printCharacter);

		System.out.println("*** The 5 names that appear the less are: ");
		appearings
				.mapToPair(x -> ((Tuple2)x).swap()) // DIRTY HACK: since Java RDDs do not support "sortBy"
				.sortByKey(true)
				.mapToPair(x -> ((Tuple2)x).swap())
				.take(5)
				.forEach(printCharacter);

	}
}
