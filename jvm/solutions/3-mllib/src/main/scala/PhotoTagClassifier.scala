import org.apache.spark.mllib.clustering.{KMeansModel, KMeans}
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
/**
  * @author Mario Macias (http://github.com/mariomac)
  */
object PhotoTagClassifier {

  def main(args: Array[String]) {
    val sparkContext : SparkContext = SparkContext.getOrCreate(
      new SparkConf(true).setAppName("MLLibDemo").setMaster("local"));
    val documents : RDD[Seq[String]] = sparkContext.textFile("phototags.txt")
      .map(line => line.toLowerCase)
      .map(line => line.replaceAll("á","a").replaceAll("é","e").replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u"))
      .map(line => line.split(" "));

    //"clean" terms
    documents.foreach(u => { u.foreach(t => print(t+" ")); println();})

    val hashingTF : HashingTF = new HashingTF()
    val tf : RDD[Vector] = hashingTF.transform(documents)
    tf.foreach(t => println(t))

    val clusters : KMeansModel = KMeans.train(tf, 2, 1, 1)

    val results = documents.map(x => Seq(x, clusters.predict(hashingTF.transform(x))))

    results.foreach(x => println(x))

//    val sc = new SparkContext(configuration);
//    val rdd = sc.parallelize(Array(4, 5, 12, 1, 6, 9));
//    "Scala's Spark says: from " + rdd.count() + " numbers, " + rdd.max() + " is the highest"
  }
}
