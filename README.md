# Data analytics with Apache Spark

Materials for the Apache Spark session for the UPC/BSC course on Big Data Analytics.

The course exercises can be followed in three programming languages, according to which makes the student feel more confortable:
Scala, Java and Python. However, Java is discouraged since its verbosity (even with Java 8's _lambdas_) and
misses some basic functions, such as `RDD.sortBy(...)`.

Copyright: [Mario Macias](http://macias.info)

## Course info

This course covers version 1.6.0 of Apache Spark, the newest until the conception of this course.

It is divided in 3 submodules (a 4th will be included if there is time) that will cover some basic aspects of Apache Spark:

1. Resilient-Distributed Datasets and Data Access
2. Spark SQL
3. MLlib for machine learning
4. (_If there is time_) A fourth topic to be decided according to the interests of the course attendees

Each module consists on a basic theoretical introduction about some related aspects about the topic, followed by
hands-on exercises.

## Software requirements for this course

The required software and libraries
are easy to install, not too invasive, 
and may work in Windows/Mac/Linux without major problems. Following are listed the software requirements if you plan
to follow the Scala/Java or Python version:

### Requirements for JVM-based languages

If you plan to follow the Scala/Java version of the course:

* Java Development Kit 6 (if you are willing to use Java as main language, Java 8 is recommended)
* Apache Maven 3
* Recommended: a good IDE with Java/Scala support ([IntelliJ IDEA](https://www.jetbrains.com/idea/),
[Scala IDE for Eclipse](http://scala-ide.org/) or [Netbeans](http://www.netbeans.org) + [Scala Plugin](https://github.com/dcaoyuan/nbscala).

The provided `jvm/spark-template` Maven project will automatically download and configure the required libraries, so there
is no need of installation of extra packages in your computer.

If you want to use the Spark interactive console, you will need to download the [Apache Spark 1.6.0 pre-built package](http://www.apache.org/dyn/closer.lua/spark/spark-1.6.0/spark-1.6.0.tgz).

### Requirements Python

If you plan to follow the Python version of the course:

* Pyhon 2.6+, 3.4+ or PyPy 2.3+
* [Apache Spark 1.6.0 pre-built package](http://www.apache.org/dyn/closer.lua/spark/spark-1.6.0/spark-1.6.0.tgz)

## Structure of this repository

        ├── exercises		Exercises proposed for the different topics of this course
        │   ├── 1-intro
        │   ├── 2-sql
        │   └── 3-ml
        ├── jvm				Project template and exercises' solutions for Spark and Scala
        │   ├── solutions	
        │   └── spark-template
        └── python			Program template and exercises' solutions for Python
            └── solutions
         
## Recommended bibliography

In English:

* [Learning Spark](http://shop.oreilly.com/product/0636920028512.do)
* [Spark Cookbook](https://www.packtpub.com/big-data-and-business-intelligence/spark-cookbook)
	
In Spanish:

* [Introducción a Apache Spark](http://www.sparkbarcelona.es/)
