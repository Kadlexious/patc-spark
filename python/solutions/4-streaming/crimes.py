#!/usr/bin/env python
# -*- coding: utf-8 -*-

# You cannot execute this file directly from the terminal, as other
# python scripts. You will need to use the "spark-submit" command
# Example:
#		$ spark-submit template.py

from pyspark import SparkConf, SparkContext

conf = SparkConf().setAppName("spark-template").setMaster("local")
sc = SparkContext(conf = conf)

from pyspark.streaming import StreamingContext
ssc = StreamingContext(sc, 5)
ssc.checkpoint("checkpoint") 

# Listen to this server
lines = ssc.socketTextStream("localhost",9999)

def coordinatesAndSingleCount(row):
	row = row.split(",")
	return ((float(row[2]),float(row[3])),1.0)

def showEpicenter(entry):
	print "Updated epicenter of crime: %f, %f" % (entry[0][0]/entry[1], entry[0][1]/entry[1])

windowSeconds = 5
windowSize = 10
lines.map(coordinatesAndSingleCount).reduceByWindow(
	lambda ((lat1,lon1),count1),((lat2,lon2),count2): ((lat1+lat2,lon1+lon2),count1+count2),
	lambda ((lat1,lon1),count1),((lat2,lon2),count2): ((lat1-lat2,lon1-lon2),count1-count2),
	windowSize, windowSeconds
	).foreachRDD(
		lambda rdd: rdd.foreach(showEpicenter)
	)
ssc.start()


