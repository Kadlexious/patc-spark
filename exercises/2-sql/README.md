# Analyzer of Google Cluster Data traces

This exercise aims to analyse a subset of the huge [Google Cluster Data](https://github.com/google/cluster-data) repository.

To get a subset of it, unzip the file located in [traces/unzip_me.zip], which contains the next files:

* `machine_events.csv`
* `task_events.csv`
* `schemas.csv`: the schema that describes the structure of the previous files.

[This link shows more information about the files, as well as others that have been not included in this exercise](https://drive.google.com/file/d/0B5g07T_gRDg9Z0lsSTEtTWtpOW8/view)

This exercise consists on finding *how many jobs have been deployed for each platform*. Example of execution:

    +--------------------+-------------+
    |         platform_id|jobs_deployed|
    +--------------------+-------------+
    |HofLGzk1Or/8Ildj2...|       890939|
    |70ZOvysYGtB6j9MUH...|         9167|
    |GtXakjpd0CD41brK7...|        90567|
    +--------------------+-------------+

## Important considerations

* The data files are in CSV format. Spark SQL does not provide by default any CSV reader. You have two alternatives:
  - Googling, you can find a 3rd-party library to create Data Frames from CSV
  - Using the `rdd.textFile` method, create your own CSV-to-DataFrame conversor.
  
* Since the tables have a considerable number of columns, and we are not going to perform numeric operations, you can assume all the columns are `StringType`.
