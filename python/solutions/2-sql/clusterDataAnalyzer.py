from pyspark import SparkConf, SparkContext
from pyspark.sql import SQLContext, Row
from pyspark.sql.types import *

conf = SparkConf().setAppName("cluster-data-analyzer").setMaster("local")
sparkContext = SparkContext(conf = conf)
sqlContext = SQLContext(sparkContext)

# Since we do not have a csv reader, we first need to read and process them as RDD
machineEventsRDD = (sparkContext.textFile("machine_events.csv")
        .map(lambda line: line.split(',')))

machineEvents = sqlContext.createDataFrame(machineEventsRDD,
	["time","machine_id","event_type","platform_id","cpus","memory"])

machineEvents.registerTempTable("machineEvents")

taskEventsRDD = (sparkContext.textFile("task_events.csv")
	        .map(lambda line: line.split(',')))

taskEvents = sqlContext.createDataFrame(taskEventsRDD,
	["time","missing","job_id","task_id","machine_id","event_type","user",
	"scheduling_class","priority","cpu_request","memory_request","disk_space_request",
	"different_machines_restriction"])
	
taskEvents.registerTempTable("taskEvents")

sqlContext.sql(
      """
        SELECT machineEvents.platform_id, COUNT(taskEvents.job_id) as jobs_deployed
        FROM taskEvents, machineEvents
        WHERE
          taskEvents.machine_id = machineEvents.machine_id
        GROUP BY machineEvents.platform_id
      """).show()