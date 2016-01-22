#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import sys

import re

from pyspark import SparkConf, SparkContext

conf = SparkConf().setAppName("prominence-calculator").setMaster("local")
sparkContext = SparkContext(conf = conf)

characterName = re.compile("^[A-Z ]+$")

appearings = (sparkContext.textFile("hamlet.txt")
	.filter(lambda line: characterName.match(line) != None)
	.map(lambda line: (line,1))
	.reduceByKey(lambda x,y: x+y)
	.persist());


print "*** The 5 names that appear the most are:"
names = (appearings.sortBy(lambda (name,appearings): appearings, ascending=False)
	.take(5));

for (name,numApp) in names:
	print "\t%s: %d times." % (name, numApp)

print "*** The 5 names that appear the less are:"
names = (appearings.sortBy(lambda (name,appearings): appearings, ascending=True)
	.take(5));

for (name,numApp) in names:
	print "\t%s: %d times." % (name, numApp)
