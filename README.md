# scalatra-spark
A proof of concept integration serving Apache Spark services with Scalatra.

The interesting files are:

 - [project/build.scala](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/project/build.scala) This has all the version stuff that needed to be finessed in order to integrate Spark and Scalatra.
 - [DataReductionServlet.scala](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/src/main/scala/com/alpine/dataReductionServer/DataReductionServlet.scala) This is the part that defines a JSON REST API that invokes a Spark job.
 - [index.html](https://github.com/curran/scalatra-spark/blob/master/data-reduction-server/src/main/webapp/index.html) This invokes the API using jQuery and prints the result to the console.
