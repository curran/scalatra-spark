package com.alpine.dataReductionServer
import org.scalatra._
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}

// Define types for JSON options passed in from the client.
case class Options( sample: Sample = null, filter: List[Filter], aggregate: Aggregate)

case class Sample( n: Int, withReplacement: Boolean = true, seed: Int = 0)

case class Filter( name: String ) // TODO complete this type

case class Aggregate(dimensions: List[Dimension], measures: List[Measure])

case class Dimension(name: String, i: Integer = 0)

case class Measure(aggregationOp: String, name:String = "", i: Integer = 0)

class DataReductionServlet extends ScalatraServlet {

  // Necessary for JSON parsing into case classes.
  // See https://github.com/json4s/json4s#extracting-values
  implicit val formats = DefaultFormats

  get("/reduceData") {
    val options = read[Options](params("options"))
    write(options)
  }

  notFound {
    contentType = "text/html"
    serveStaticResource() getOrElse resourceNotFound()
  }

}
