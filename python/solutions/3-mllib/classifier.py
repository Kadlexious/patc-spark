#!/usr/bin/env python
# -*- coding: utf-8 -*-

# You cannot execute this file directly from the terminal, as other
# python scripts. You will need to use the "spark-submit" command
# Example:
#		$ spark-submit template.py

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

from pyspark import SparkConf, SparkContext
from pyspark.mllib.feature import HashingTF
from pyspark.mllib.clustering import KMeans

conf = SparkConf().setAppName("photo-classifier").setMaster("local[2]")
sc = SparkContext(conf = conf)

## 1 - Loading and cleaning the data

photoTags = (sc.textFile("phototags.txt")
# Because a possible bug with PySpark localization, this code doesn't work. For this exercise, we have
# removed the accents of 'phototags.txt', and instead of running the next line:
#	.map(lambda line: line.lower().replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u').split(" "))
# we have simplified it to:
	.map(lambda line: line.lower().split(" "))
	.persist())


## 2 - Feature extraction and transformation

hashingTF = HashingTF()
tf = hashingTF.transform(photoTags)

## 3 - Clustering

clusters = KMeans.train(tf,2,50)

## 4 - Checking the group of each of the existing pictures

print "The existing pictures belong to the next groups:"
def showTagsGroup((tag, group)):
	print "\t%s. Labeled as: %d" % (tag, group)

(photoTags.map(lambda x: (x, clusters.predict(hashingTF.transform(x))))
	.foreach(showTagsGroup))

photoTags.unpersist()

## 5 - Cheching new entries

print "Checking new entries:"

(sc.parallelize(["barcelona messi gol iniesta"])
    .map(lambda l: l.split(" "))
    .map(lambda words: (words, clusters.predict(hashingTF.transform(words))))
    .foreach(showTagsGroup))

(sc.parallelize(["paella ramblas vermut barcelona playa"])
    .map(lambda l: l.split(" "))
    .map(lambda words: (words, clusters.predict(hashingTF.transform(words))))
    .foreach(showTagsGroup))
