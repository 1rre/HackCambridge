import scalajs.js.annotation._
import scalajs.js
import js.JSConverters._
import org.scalajs.dom.{window, document, html}
import scala.scalajs.js.JSON

@js.native
@JSGlobal
object hljs extends js.Any {
  def highlightAll(): Unit = js.native
  def highlightElement(e: html.Element): Unit = js.native
}

object Main extends App {
  lazy val Transcript = document.getElementById("text-transcript").asInstanceOf[html.Div]
  lazy val Output = CodeWindow(document.getElementById("text-output").asInstanceOf[html.Div])
  window.onload = x => {
    Output.updateLineNums()
    Output.highlight("plaintext")
    Transcript.addEventListener("DOMSubtreeModified", e => {
      println(Transcript.children(0).innerText)
      Output.update()
    })
  }
  val x = "a string and an integer "
  //"takes five arguments an integer an integer a string an integer and an integer"
  println(SpeechParser.args(2).parse(x))
}
