#!/usr/bin/env python
# -*- coding: utf-8 -*-

# You cannot execute this file directly from the terminal, as other
# python scripts. You will need to use the "spark-submit" command
# Example:
#		$ spark-submit template.py

from pyspark import SparkConf, SparkContext

conf = SparkConf().setAppName("spark-template").setMaster("local")
sparkContext = SparkContext(conf = conf)

val = (sparkContext.parallelize([1,2,3,4,5,6])
	.filter(lambda n : n % 2 == 0)
	.sum())

print "the Sum of the even numbers from 1 to 6 is %d" % val