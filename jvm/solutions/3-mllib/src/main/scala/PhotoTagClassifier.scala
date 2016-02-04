import org.apache.spark.mllib.clustering.{KMeansModel, KMeans}
import org.apache.spark.mllib.feature.{IDF, HashingTF}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
/**
  * @author Mario Macias (http://github.com/mariomac)
  */
object PhotoTagClassifier {

  def test {
    val sparkContext : SparkContext = SparkContext.getOrCreate(
      new SparkConf(true).setAppName("MLLibDemo").setMaster("local"));
    val photoTags : RDD[Seq[String]] = sparkContext.textFile("phototags.txt")
      .map(line => line.toLowerCase.replaceAll("á","a").replaceAll("é","e").replaceAll("í","i").replaceAll("ó","o").replaceAll("ú","u").split(" "));

    val hashingTF : HashingTF = new HashingTF()
    val tf : RDD[Vector] = hashingTF.transform(photoTags)
    tf.foreach(t => println(t))

    val clusters : KMeansModel = KMeans.train(tf, 2, 1, 10)

    val results = photoTags.map(x => Seq(x, clusters.predict(hashingTF.transform(x))))
    results.foreach(x => println(x))


    var football : RDD[Seq[String]] =
      sparkContext.parallelize(Array("barcelona messi gol iniesta")).map(line => line.split(" "))
    var footballTf : RDD[Vector] = hashingTF.transform(football)
    football.foreach(x => println(s"Predicted category for football post: ${clusters.predict(hashingTF.transform(x))}"))

    var touristic : RDD[Seq[String]] =
      sparkContext.parallelize(Array("paella ramblas vermut barcelona playa")).map(line => line.split(" "))
    var touristicTf : RDD[Vector] = hashingTF.transform(football)
    touristic.foreach(x => println(s"Predicted category for touristic post: ${clusters.predict(hashingTF.transform(x))}"))

    //    var tourist : RDD[Vector] = hashingTF.transform(
//      sparkContext.parallelize("").asInstanceOf[RDD[String]].map(line => line.split(" "))
//    )
//    println(s"Predicted category for touristic post: ${clusters.predict(tourist)}")






//    clusters.predict(

    sparkContext.stop()
  }
}
