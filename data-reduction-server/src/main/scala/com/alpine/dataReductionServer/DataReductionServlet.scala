// This is an example servlet that defines a JSON REST API
// that invokes Spark code.
// Curran Kelleher Feb 2015

package com.alpine.dataReductionServer

// Use scalatra to define routes
import org.scalatra._

// Use JSON4S for JSON serialization
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

// Use Spark
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// Define types for JSON options passed in from the client.
case class Options( dataset: String, cube: Cube )
case class Cube(dimensions: List[Dimension], measures: List[Measure])
case class Dimension(name: String, i: Integer = 0)
case class Measure(aggregationOp: String, name: String = "", i: Integer = 0)

class DataReductionServlet extends ScalatraServlet {

  // This is necessary for JSON parsing into case classes.
  // See https://github.com/json4s/json4s#extracting-values
  implicit val formats = DefaultFormats

  // Initialize Spark context.
  // http://spark.apache.org/docs/1.2.0/programming-guide.html#initializing-spark
  val conf = new SparkConf().setAppName("dataReduction").setMaster("local")
  val sc = new SparkContext(conf)

  // This is a stand-in for a more complex data abstraction layer.
  val datasets = Map("adult" -> "/Users/Kelleher/repos/data/uci_ml/adult/")

  // This defines the HTTP GET method that invokes Spark.
  get("/reduceData") {

    // The read function is provided by json4s.jackson.Serialization.
    // It automagically parses JSON into instances of case classes.
    val options = read[Options](params("options"))

    // Extract things from the options object.
    val dataset = datasets(options.dataset)
    val dataPath = dataset + "data.csv"
    val schemaPath = dataset + "schema.csv"
    var dimensions = options.cube.dimensions
    var measures = options.cube.measures

    // Use Spark to load the data files.
    val data = sc.textFile(dataPath)
    val schema = sc.textFile(schemaPath).collect()

    // Add attribute index information to dimensions and measures for later use.
    val attributeIndices = schema.map(_.split(",")(0).trim).zipWithIndex.toMap
    dimensions = dimensions.map(d => Dimension(d.name, attributeIndices(d.name)))
    measures = measures.map(m => 
      if (m.aggregationOp == "count"){ 

        // Present count as a column called "count" in the output.
        Measure("count", "count")
      }
      else {

        // For non-count aggregations, the attribute index is necessary.
        Measure(m.aggregationOp, m.name, attributeIndices(m.name))
      }
    )

    // Parse data into table, ignoring rows not parsed correctly.
    val tupleSize = schema.length;
    val table = data.map(_.split(",").map(_.trim)).filter(_.length == tupleSize)

    // Compute a data cube using the specified dimensions and measures.
    val cube = table.map(row =>
      (
        // Each key is a List of dimension values.
        dimensions.map(d => row(d.i)),

        // Each value is a List of measure values.
        measures.map(m =>
          if (m.aggregationOp == "count"){
            1.0
          } else {
            row(m.i).toDouble
          }
        )
      )
    ).reduceByKey((a, b) =>

      // Aggregate over unique dimension tuples
      // by summing measure values.
      (a, b).zipped.map(_+_)

    ).collect()

    // Return the cube to the client as nicely formatted JSON.
    write(cube.map( observation =>
      (
        dimensions.map(_.name).zip(observation._1) :::
        measures.map(_.name).zip(observation._2)
      ).toMap
    ))

    // Example output:
    // [
    //   { "sex": "Female", "race": "White", "count": 8642, "capital-gain": 4957141 },
    //   { "sex": "Female", "race": "Other", "count": 109, "capital-gain": 27759 },
    //   { "sex": "Male", "race": "Black", "count": 1569, "capital-gain": 1102151 },
    //   { "sex": "Female", "race": "Asian-Pac-Islander", "count": 346, "capital-gain": 269339 },
    //   { "sex": "Female", "race": "Amer-Indian-Eskimo", "count": 119, "capital-gain": 64808 },
    //   { "sex": "Male", "race": "White", "count": 19174, "capital-gain": 26242964 },
    //   { "sex": "Male", "race": "Other", "count": 162, "capital-gain": 225534 },
    //   { "sex": "Female", "race": "Black", "count": 1555, "capital-gain": 803303 },
    //   { "sex": "Male", "race": "Asian-Pac-Islander", "count": 693, "capital-gain": 1266675 },
    //   { "sex": "Male", "race": "Amer-Indian-Eskimo", "count": 192, "capital-gain": 129650 }
    // ]
  }

  // Serve static files.
  notFound {
    contentType = "text/html"
    serveStaticResource() getOrElse resourceNotFound()
  }

}
