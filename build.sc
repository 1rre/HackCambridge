import mill._, scalalib._, scalajslib._, scalajslib.api._

object Parsers extends ScalaModule {
  def scalaVersion = "3.1.0"
}

object FrontEnd extends ScalaJSModule {
  def scalaVersion = "3.1.0"
  def scalaJSVersion = "1.8.0"
  def moduleDeps = Seq(Parsers)
  def ivyDeps = Agg(
    ivy"org.scala-js:scalajs-dom_sjs1_3:2.1.0"
  )
}

object SpeakCode extends ScalaJSModule {
  def scalaVersion = "2.13.7"
  def scalaJSVersion = "1.8.0"
  def scalacOptions = Seq("-deprecation")
  def moduleKind = ModuleKind.CommonJSModule
  def ivyDeps = Agg(
    ivy"org.scala-js:scalajs-dom_sjs1::1.1.0",
    ivy"net.exoego::scala-js-nodejs-v16_sjs1:0.14.0"
  )
}
