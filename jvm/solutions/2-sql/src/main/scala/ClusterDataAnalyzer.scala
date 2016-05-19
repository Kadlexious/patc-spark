import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkContext, SparkConf}

import scala.util.matching.Regex

/**
  * @author Mario Macias (http://github.com/mariomac)
  */
object ClusterDataAnalyzer {
  def test(configuration: SparkConf) : Unit = {
    val sparkContext = new SparkContext(configuration);
    val sqlContext = new SQLContext(sparkContext)

    // Since we do not have a csv reader, we first need to read and process them as RDD

    val machineEventsRDD : RDD[Row] = sparkContext.textFile("machine_events.csv")
        .map(line => Row.fromSeq(line.split(',').toSeq));
    val machineEvents : DataFrame = sqlContext.createDataFrame(machineEventsRDD,
      StructType(Array[StructField](
      StructField("time",StringType,false),
      StructField("machine_id",StringType,false),
      StructField("event_type",StringType,false),
      StructField("platform_id",StringType,true),
      StructField("cpus",StringType,true),
      StructField("memory",StringType,true)
    )));

    machineEvents.registerTempTable("machineEvents");

    val taskEventsRDD : RDD[Row] = sparkContext.textFile("task_events.csv")
        .map(line => Row.fromSeq(line.split(',').toSeq))


    val taskEvents : DataFrame = sqlContext.createDataFrame(taskEventsRDD, StructType(Array[StructField](
      StructField("time",IntegerType,false),
      StructField("missing",StringType,true),
      StructField("job_id",StringType,false),
      StructField("task_id",StringType,false),
      StructField("machine_id",StringType,true),
      StructField("event_type",StringType,false),
      StructField("user",StringType,true),
      StructField("scheduling_class",StringType,true),
      StructField("priority",StringType,false),
      StructField("cpu_request",StringType,true),
      StructField("memory_request",StringType,true),
      StructField("disk_space_request",StringType,true),
      StructField("different_machines_restriction",StringType,true)
    )));

    taskEvents.registerTempTable("taskEvents");

    sqlContext.sql(
      """
        SELECT machineEvents.platform_id, COUNT(taskEvents.job_id) as jobs_deployed
        FROM taskEvents, machineEvents
        WHERE
          taskEvents.machine_id = machineEvents.machine_id
        GROUP BY machineEvents.platform_id
      """)
      .show()
  }
}
