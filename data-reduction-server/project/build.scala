import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object DataReductionServerBuild extends Build {
  val Organization = "com.alpine"
  val Name = "Data Reduction Server"
  val Version = "0.1.0-SNAPSHOT"

  // Modified Scala version to be compatible with Spark
  // See http://spark.apache.org/docs/1.2.0/programming-guide.html#linking-with-spark
  // "Spark 1.2.0 uses Scala 2.10. To write applications in Scala, you will need to use a compatible Scala version (e.g. 2.10.X)." 2.10.4 is the latest 2.10.X version available.
  val ScalaVersion = "2.10.4"

  val ScalatraVersion = "2.3.0"

  lazy val project = Project (
    "data-reduction-server",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",

        // Modified Jetty version to match Spark dependency
        // See http://mvnrepository.com/artifact/org.apache.spark/spark-core_2.10/1.2.1
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.14.v20131031" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "8.1.14.v20131031" % "container",

        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        "org.json4s"   %% "json4s-jackson" % "3.2.11",
        "org.apache.spark" % "spark-core_2.10" % "1.2.1"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
