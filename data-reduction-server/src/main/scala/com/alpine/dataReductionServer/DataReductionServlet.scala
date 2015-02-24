package com.alpine.dataReductionServer
import org.scalatra._
import org.json4s._
import org.json4s.jackson.JsonMethods._

case class Person(id: Int, name: String)


class DataReductionServlet extends ScalatraServlet {

  // Necessary for JSON parsing into case classes.
  // See https://github.com/json4s/json4s#extracting-values
  implicit val formats = DefaultFormats

  get("/reduceData") {
    val optionsJSONStr = params("options")
    val person = parse(optionsJSONStr).extract[Person]
    "Hello" + person.name
  }

  notFound {
    contentType = "text/html"
    serveStaticResource() getOrElse resourceNotFound()
  }

}
