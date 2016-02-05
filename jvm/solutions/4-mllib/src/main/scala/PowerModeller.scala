import java.io.{PrintStream, FileOutputStream, FileInputStream}
import java.util.Scanner

import org.apache.spark.ml.optim.WeightedLeastSquares
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.regression._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.linalg.{Vectors, Vector, Matrix}

/**
  * @author Mario Macias (http://github.com/mariomac)
  */
class PowerModeller(
  val sc: SparkContext
) {

  def modelBuildIterations = 10

  def buildAndValidate() = {

    val input = sc.textFile("./CPU.csv")
      .filter(l => !l.startsWith("timestamp")).map(l => l.split(",").map(_.toDouble))

    val max = input.reduce((v1, v2) => {
      val arr : Array[Double] = Array.ofDim(v1.size)
      for(i <- 0 until v1.size) {
        arr(i) = Math.max(v1(i),v2(i))
        if(arr(i) <= 0.01) arr(i) = 0.01
      }
      arr
    })

    //remove columns whose max values are 0
    val normalizedInput = input.map( row => {
      var r = 0;
      var nrow = row
      for(i <- 0 until max.size) {
        if(max(i) <= 0.001) {
          nrow = nrow.take(i+r) ++ nrow.drop(i+r+1)
          r-=1
        }
      }
    })

    println("d = " + max.toSeq)
    val normalizedInput = input.map( l => {
      val arr : Array[Double] = Array.ofDim(l.size)
      for(i <- 0 until l.size) {
        arr(i) = l(i) / max(i)
      }
      arr
    })
    val normalizedInput = input;

    val splits = normalizedInput.randomSplit(Array(0.1, 0.4), seed = 11L)


    val training : RDD[LabeledPoint] = splits(0)
      .filter(line => !line.startsWith("timestamp")) // remove header line
      .map(arr => LabeledPoint(arr(1), Vectors.dense(arr.splitAt(2)._2.toArray)))

    val model = LinearRegressionWithSGD.train(training,modelBuildIterations)

    val validation = splits(1)
      .filter(line => !line.startsWith("timestamp")) // remove header line
      .map(arr => LabeledPoint(arr(1), Vectors.dense(arr.splitAt(2)._2.toArray)))
      .map(lp => (lp.label, model.predict(lp.features)))
//      .take(10).foreach(p => println(p))
//      .map(arr => (arr(1), model.predict())
//
    validation.persist()
//
    validation.map(t => t._1 + "\t" + t._2).saveAsTextFile("./validation-" + System.currentTimeMillis() / 10000)
//
    val mse = validation.map(tuple => Math.pow(tuple._1-tuple._2,2)).mean()
    println("Mean squared error: " + mse)
//
//    validation.unpersist()
  }


}
