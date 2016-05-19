# Exercise about Spark Streaming
## *The epicenter of the crime*

*FICTION*: Sacramento City has a problem with criminality, so the police created an app that, evey time a citizen suffers the criminality, it sends a record in the next format:

    TIME,ADDRESS,LATTITUDE,LONGITUDE
    
For example:

    1/1/06 0:00,3108 OCCIDENTAL DR,38.55042047,-121.3914158
    
We will create an app that continuously listens for the new crimes, and calculates where is the *epicenter* of the crime during a time window.

First of all, you need to execute the `crimeServer.py` script that will simulate a server that notifies the new crimes in Sacramento City.

*IMPORTANT*: this is a quick'n'dirty implementation made in 5 minutes, so every time you restart your Spark script you need to rerun the `crimeServer.py` script.

Now let's proceed to the Spark script to analyse crimes in sacramento.

## 1 - Create a Streaming Context

First, you need to create the `StreamingContext` object, which is the entry point to Spark Streaming:

    from pyspark.streaming import StreamingContext
    ssc = StreamingContext(sc, 5)
    
5 is the duration (in seconds) of every batch.

In every batch, the data will be checkpointed, so you need to set a Checkpoint directory:

    ssc.checkpoint("checkpoint") 

## 2 - Connect to the crime server

    crimeServer = ssc.socketTextStream("localhost",9999)

## 3 - Transform the input data

To calculate the epicenter, we will sum all the coordinates received in the time window and divide by the number of coordinates.

Thinking the Spark way, we would need to convert every crime to something like:

    ((lat,lon),1)
    
E.g.:

    ((58.12342,-121.12341),1)
    
This form will allow us to reduce into a tuple where the first element is a tuple with the sum of the coordinates and the second one the number of coordinates:

    def coordinatesAndSingleCount(row):
    	row = row.split(",")
    	return ((float(row[2]),float(row[3])),1.0)
    	
    coordinates = crimeServer.map(coordinatesAndSingleCount)

## 4 - Reduce the coordinates in a window

When reducing a stream, we need to provide two lambda functions:

  * The *reduce* function that in this case will sum all the incoming coordinates
  * The complementary function of reduce, that in this case will subtract the oldest coordinates that are going out of the window
 
We will use the `reduceByWindow` method:

    reducedCoordinates = coordinates.reduceByWindow(
      lambda ((lat1,lon1),count1),((lat2,lon2),count2): ((lat1+lat2,lon1+lon2),count1+count2),
      lambda ((lat1,lon1),count1),((lat2,lon2),count2): ((lat1-lat2,lon1-lon2),count1-count2),
    	 5, 5
    	)

Where `5` is both the window size and the batch time.

## 5 - Showing the epicenter

The output of the stream will be a set of RDDs that contain the reduced coordinates (a tuple with the summed coordinated and the number of coordinates). We need now to divide the sum of latitudes and lungitudes and then show it by screen.

    def showEpicenter(entry):
      print "Updated epicenter of crime: %f, %f" % (entry[0][0]/entry[1], entry[0][1]/entry[1])
      
    reducedCoordinates.foreachRDD(lambda rdd: rdd.foreach(showEpicenter))
    
## 6 - Start processing the streaming

The previous set of instructions won't produce any output until we tell the `StreamingContext` instance to start processing the stream.

    ssc.start()
    
## 7 - Fight the crime!

You will see periodically what is the epicenter of the crime:

    Updated epicenter of crime: 38.565769, -121.437853
    Updated epicenter of crime: 38.544570, -121.456474
    Updated epicenter of crime: 38.524920, -121.467767
    Updated epicenter of crime: 38.574805, -121.481816
    Updated epicenter of crime: 38.582740, -121.483295
    Updated epicenter of crime: 38.531597, -121.471232
    Updated epicenter of crime: 38.543534, -121.460683

    



    
