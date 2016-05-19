# Step-by-step MLlib hands-on


## Grouping labeled images

Suppose you have a set of labeled pictures from _Instagram_. Those pictures are in the [phototags.txt](phototags.txt) file, where
each line contains the different labels for a given picture:

    Messi Barcelona Madrid Instagram Barcelona
    barcelona paella playa sangria instagram
    Paella Barcelona Instagram vermut Champions
    RAMBLAS PAELLA RAMBLAS INSTAGRAM INIESTA
    MESSI MADRID INSTAGRAM GOL PORTERIA
    BarCelona Messi Instagram Twitter Balon
    Messi GOL BaRceLona INSTAGRAM arena
    sangria paella instaGram AGUA Barcelona

Using the tags, we want to classify the pictures according to a set of given topics.

This exercise will show you:

* How to apply KMeans _clustering_ algorithm to classify existing data
* How to apply the generated model to classify incoming data

## 1 - Loading and cleaning the data

The tags from [phototags.txt](phototags.txt) are introduced by a large set of users, and they may be slightly different
(e.g. `sangria` != 'Sangría'). The first step is to load the data in an RDD, where each element is an array of tags for a single photo.
You must clean the tags by converting them to lower case.

    photoTags = (sc.textFile("phototags.txt")
             .map(lambda line: line.lower().split(" "))
             .persist())

In the interactive console, you can see the `photoTags` contents by typing:

    In [7]: photoTags.collect()
    Out[7]:
    [[u'messi', u'barcelona', u'madrid', u'instagram', u'barcelona'],
     [u'barcelona', u'paella', u'playa', u'sangria', u'instagram'],
     [u'paella', u'barcelona', u'instagram', u'vermut', u'champions'],
     [u'ramblas', u'paella', u'ramblas', u'instagram', u'iniesta'],
     [u'messi', u'madrid', u'instagram', u'gol', u'porteria'],
     [u'barcelona', u'messi', u'instagram', u'twitter', u'bal\xf3n'],
     [u'messi', u'gol', u'barcelona', u'instagram', u'arena'],
     [u'sangr\xeda', u'paella', u'instagram', u'agua', u'barcelona']]

## 2 - Feature extraction and transformation

`mllib.feature` package contains classes to extract feature vector from text. This hands-on uses [TF-IDF](https://en.wikipedia.org/wiki/Tf-idf)
as MLlib example functionality.

`HashingTF` class allows obtaining a Hash with the term frequencies of each word in a document. For example:

    from pyspark.mllib.feature import HashingTF
    hashingTF = HashingTF()
    tf = hashingTF.transform(photoTags)

From the interactive console, you can see the contents of `tf` by typing:

    In [8]: tf.collect()
    Out[8]:
    [SparseVector(1048576, {388745: 1.0, 503816: 1.0, 618478: 2.0, 929925: 1.0}),
     SparseVector(1048576, {178348: 1.0, 618478: 1.0, 767093: 1.0, 929925: 1.0, 1030326: 1.0}),
     SparseVector(1048576, {355677: 1.0, 618478: 1.0, 630725: 1.0, 767093: 1.0, 929925: 1.0}),
     SparseVector(1048576, {239361: 2.0, 645012: 1.0, 767093: 1.0, 929925: 1.0}),
     SparseVector(1048576, {102762: 1.0, 388745: 1.0, 503816: 1.0, 582305: 1.0, 929925: 1.0}),
     SparseVector(1048576, {384611: 1.0, 503816: 1.0, 618478: 1.0, 900746: 1.0, 929925: 1.0}),
     SparseVector(1048576, {143584: 1.0, 503816: 1.0, 582305: 1.0, 618478: 1.0, 929925: 1.0}),
     SparseVector(1048576, {446808: 1.0, 618478: 1.0, 767093: 1.0, 929925: 1.0, 1046606: 1.0})]

Each returned `SparseVector` shows an array with the next components:

* The default value for `HashingTf()` --> 2^20 = 1048576
* A Map with the hash representation of each word followid by its frequency in the sentence

## 3 - Clustering

Each `SparseVector` object can be seen as a point in a n-dimensional space.

Now is time to use the `KMeans` clustering algorithm to classify the pictures into two different categories:

    from pyspark.mllib.clustering import KMeans
    clusters = KMeans.train(tf,2,maxIterations=50)

where `2` is the number of categories and `maxIterations=50` is the maximum number of times to update the clusters if the algorithm does not converge to a stable solution.

The `clusters` variable is now an instance of the `KMeansModel` class.

Please refer to the [Spark API](https://spark.apache.org/docs/latest/api/scala/index.html) to get the details on `KMeans` and `KMeansModel` classes.

## 4 - Checking the group of each of the existing pictures

The `KMeans.train` method returns a `KMeansModel` object, whose `predict` method can classify incoming data according to the trained clusters.

You can check the classification of the tags, by calling the `KMeansModel.predict` method by passing individually a `TF` object for each of the pictures.

Applying this step with the generated KMeansModel from the previous example:

    def showTagsGroup((tag, group)):
    	print "\t%s. Labeled as: %d" % (tag, group)
    	
    print "The existing pictures belong to the next groups:"
    (photoTags.map(lambda x: (x, clusters.predict(hashingTF.transform(x))))
    	.foreach(showTagsGroup))

Here, the `predict` method is not predicting but entries from the past. It is used just to observ the classification `KMeans` has done with the training set.

*NOTE*: At this point you can unpersist the `photoTags` RDD to free its occupied memory:

    photoTags.unpersist()

## 5 - Checking the group of new entries

You can check that a couple of new entries from different topics are labelled as expected, for example:

1. `"barcelona messi gol iniesta"`
2. `"paella ramblas vermut barcelona playa"`

To do that, you must generate hashing tf from the senteces an use the KMeansModel to predict their label:

    (sc.parallelize(["barcelona messi gol iniesta"])
        .map(lambda l: l.split(" "))
        .map(lambda words: (words, clusters.predict(hashingTF.transform(words))))
        .foreach(showTagsGroup))
        
    (sc.parallelize(["paella ramblas vermut barcelona playa"])
        .map(lambda l: l.split(" "))
        .map(lambda words: (words, clusters.predict(hashingTF.transform(words))))
        .foreach(showTagsGroup))
        
You have to see the next output:

    ['barcelona', 'messi', 'gol', 'iniesta']. Labeled as: 0
    ['paella', 'ramblas', 'vermut', 'barcelona', 'playa']. Labeled as: 1

