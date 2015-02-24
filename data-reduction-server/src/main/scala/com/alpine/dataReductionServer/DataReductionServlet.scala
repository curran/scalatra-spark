package com.alpine.dataReductionServer

// Use scalatra to define routes
import org.scalatra._

// Use JSON4S for JSON serialization
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}

// Use Spark
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// Define types for JSON options passed in from the client.
case class Options(
                    dataset: String,
                    sample: Sample = null,
                    filter: List[Filter] = null,
                    aggregate: Aggregate = null
                    )

case class Sample(n: Int, withReplacement: Boolean = true, seed: Int = 0)

case class Filter(name: String) // TODO complete this type

case class Aggregate(dimensions: List[Dimension], measures: List[Measure])

case class Dimension(name: String, i: Integer = 0)

case class Measure(aggregationOp: String, name: String = "", i: Integer = 0)

class DataReductionServlet extends ScalatraServlet {

  // Initialize Spark context.
  // http://spark.apache.org/docs/1.2.0/programming-guide.html#initializing-spark
  val conf = new SparkConf().setAppName("dataReduction").setMaster("local")
  val sc = new SparkContext(conf)

  //// TODO integrate with Chorus / Data Abstraction Layer for getting the data
  val datasets = Map("adult" -> "/Users/Kelleher/repos/data/uci_ml/adult/")

  // Necessary for JSON parsing into case classes.
  // See https://github.com/json4s/json4s#extracting-values
  implicit val formats = DefaultFormats

  get("/reduceData") {
    val options = read[Options](params("options"))
    val dataset = datasets(options.dataset)
    val dataPath = dataset + "data.csv"
    val schemaPath = dataset + "schema.csv"

    val data = sc.textFile(dataPath)
    //val schema = sc.textFile(schemaPath).collect()

    data.first()
  }

  notFound {
    contentType = "text/html"
    serveStaticResource() getOrElse resourceNotFound()
  }

}
