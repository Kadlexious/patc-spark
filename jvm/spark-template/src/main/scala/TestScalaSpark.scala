import org.apache.spark.{SparkContext, SparkConf}

/**
  * @author Mario Macias (http://github.com/mariomac)
  */
object TestScalaSpark {
  def test(configuration: SparkConf) : String = {
    val sc = new SparkContext(configuration);
    val rdd = sc.parallelize(Array(4, 5, 12, 1, 6, 9));
    "Scala's Spark says: from " + rdd.count() + " numbers, " + rdd.max() + " is the highest"
  }
}
