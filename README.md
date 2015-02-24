# scalatra-spark

An example project that demonstrates how to serve [Apache Spark](http://spark.apache.org/) services with [Scalatra](http://www.scalatra.org/).

The project was generated according to [Scalatra's Getting Started Guide](http://www.scalatra.org/2.4/getting-started/first-project.html), then minimal changes were made to integrate with Spark. Some effort was also required also to use [JSON4S](https://github.com/json4s/json4s) for parsing arguments and rendering results.

The interesting files are:

 - [project/build.scala](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/project/build.scala) This has all the version stuff that needed to be finessed in order to integrate Spark and Scalatra.
 - [DataReductionServlet.scala](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/src/main/scala/com/alpine/dataReductionServer/DataReductionServlet.scala) This is the part that defines a JSON REST API that invokes a Spark job.
 - [main.js](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/src/main/webapp/main.js) This invokes the API using jQuery and prints the result to the console.


See also [StackOverflow - Any examples of code integrating Scalatra with Spark](http://stackoverflow.com/questions/24965676/any-examples-of-code-integrating-scalatra-with-spark)

by Curran Kelleher Feb 2015
