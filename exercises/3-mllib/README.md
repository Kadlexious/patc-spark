# Step-by-step MLlib hands-on

*IMPORTANT NOTE*: if you are using Java/Scala in the course's VM, add the next option to Maven or Java:

    -Xmx1024m
    
## Grouping labeled images

Suppose you have a set of labeled pictures from _Instagram_. Those pictures are in the [phototags.txt](phototags.txt) file, where
each line contains the different labels for a given picture:

    Messi Barcelona Madrid Instagram Barcelona
    barcelona paella playa sangria instagram
    Paella Barcelona Instagram vermut Champions
    RAMBLAS PAELLA RAMBLAS INSTAGRAM INIESTA
    MESSI MADRID INSTAGRAM GOL PORTERIA
    BarCelona Messi Instagram Twitter Balón
    Messi GOL BaRceLona INSTAGRAM arena
    sangría paella instaGram AGUA Barcelona

Using the tags, we want to classify the pictures according to a set of given topics.

This exercise will show you:

* How to apply KMeans _clustering_ algorithm to classify existing data
* How to apply the generated model to classify incoming data

# 1 - Loading and cleaning the data

The tags from [phototags.txt](phototags.txt) are introduced by a large set of users, and they may be slightly different
(e.g. `sangria` != 'Sangría'). The first step is to load the data in an RDD, where each element is an array of tags for a single photo.
You must clean the tags by:

1. Converting them to lower case.
2. Replacing Spanish accents `áéíóú` by non-accent vowels `aeiou`

# 2 - Feature extraction and transformation

`mllib.feature` package contains classes to extract feature vector from text. This hands-on uses [TF-IDF](https://en.wikipedia.org/wiki/Tf-idf)
as MLlib example functionality.

`HashingTF` class allows obtaining a Hash with the term frequencies of each word in a document. For example:

    scala> import org.apache.spark.mllib._
    scala> import org.apache.spark.rdd.RDD
    scala> import org.apache.spark.mllib.feature.HashingTF
    scala> val words : RDD[Seq[String]] = sc.parallelize(Array("to be or not to be")).map(v => v.split(" "))
    scala> val hashingTF = new HashingTF()
    scala> val tf = hashingTF.transform(words)
    scala> tf.collect()
    res0: Array[org.apache.spark.mllib.linalg.Vector] = Array((1048576,[3139,3555,3707,109267],[2.0,1.0,2.0,1.0]))

The the returned vector shows an array with the next components:

* The default value for `HashingTf()` --> 2^20 = 1048576
* A vector with the hash representation of each word
* A vector with the frequency for each of the corresponding words.
 
You can execute the HashingTF transform method to obtain TFs for the tags in the  [phototags.txt](phototags.txt) libraries. You will get a resulting vector for each line of the file.

# 3 - Clustering

Now is time to use the `KMeans` clustering algorithm to classify the pictures into two different categories.

An example of usage of `KMeans`:

        scala> import org.apache.spark.mllib.clustering.KMeans
        scala> import org.apache.spark.mllib.linalg.Vector
        scala> import org.apache.spark.mllib.linalg.Vectors
        scala> var points : Array[org.apache.spark.mllib.linalg.Vector] = Array(
             | Vectors.dense(10,10), Vectors.dense(11,10), Vectors.dense(-10,-10), Vectors.dense(-12,-9))
        scala> val clusters = KMeans.train(sc.parallelize(points), 2, 19)
        scala> clusters.clusterCenters
        res2: Array[org.apache.spark.mllib.linalg.Vector] = Array([10.5,10.0], [-11.0,-9.5])

Please refer to the [Spark API](https://spark.apache.org/docs/latest/api/scala/index.html) to get the details on the parameters required by `KMeans.train`.
