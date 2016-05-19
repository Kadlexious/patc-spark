import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Mario Macias (http://github.com/mariomac)
  */
object ProminenceCalculatorScala {
  def main(args:Array[String]) = {
    val config: SparkConf = new SparkConf(true).setAppName("prominence-calculator").setMaster("local")
    val sparkContext: SparkContext = new SparkContext(config)

    val characterName =  "^[A-Z ]+$".r;

    val appearings = sparkContext.textFile("hamlet.txt")
        .filter(line => characterName.pattern.matcher(line).matches())
        .map(line => (line, 1))
        .reduceByKey((x,y) => x+y)
        .persist();

    val printCharacter = (name: String, appearings: Int) => println("\t"+name+": " + appearings + " times.");

    println("*** The 5 names that appear the most are: ");
    appearings.sortBy(tuple => tuple._2, false)
      .take(5)
      .foreach(t => printCharacter(t._1,t._2));

    println("*** The 5 names that appear the less are: ");
    appearings.sortBy(tuple => tuple._2, true)
      .take(5)
      .foreach(t => printCharacter(t._1,t._2));

  }
}
