import scala.collection.mutable.Buffer
import org.scalajs.dom.html

import SpeechParser.{Python, Ruby, Java}
import SpeechParser.{NoMatch, Match}
import SpeechParser.Command
import SpeechParser.PrintArg

class CodeWindow(val asJS: html.Div) {
  def resolveJava(cmd: Command): String = cmd match {
    case PrintArg(i: Int) => s"System.out.println(x${i-1})"
  }
  def resolvePy(cmd: Command): String = cmd match {
    case PrintArg(i: Int) => s"print(x${i-1})"
  }
  def resolveRuby(cmd: Command): String = cmd match {
    case PrintArg(i: Int) => s"puts x${i-1}.inspect"
  }
  def update(): Unit = {
    val input = Main.Transcript.children(0).innerText.toLowerCase.filterNot(_ == ',')
    val (text, lang) = {
      val x = (SpeechParser.fullFn).parse("""\s+""".r.replaceAllIn(input," "))
      println(x)
      x match {
        case Match(_, (Java, name, args, rtn, i)) => (s"public static $rtn $name${args.zipWithIndex.map((x,y)=>s"$x x$y").mkString("(", ", ", ")")} {\n  ${i.map(resolveJava).map(_ + ";").mkString("\n  ")}\n}\n", "java")
        case Match(_, (Python, name, args, _, i)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")}:\n  ${i.map(resolvePy).mkString("\n  ")}\n", "python")
        case Match(_, (Ruby, name, args, _, i)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")} do\n  ${i.map(resolveRuby).mkString("\n  ")}\nend\n", "ruby")
        case Match(_, (Java, name, args, rtn)) => (s"public static $rtn $name${args.zipWithIndex.map((x,y)=>s"$x x$y").mkString("(", ", ", ")")} {}\n", "java")
        case Match(_, (Python, name, args, _)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")}:\n  0\n", "python")
        case Match(_, (Ruby, name, args, _)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")} do\nend\n", "ruby")
        case Match(_, (Java, name, args)) => (s"public static void $name${args.zipWithIndex.map((x,y)=>s"$x x$y").mkString("(", ", ", ")")} {}\n", "java")
        case Match(_, (Python, name, args)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")}:\n  0\n", "python")
        case Match(_, (Ruby, name, args)) => (s"def $name${args.zipWithIndex.map((x,y)=>s"x$y").mkString("(", ", ", ")")} do\nend\n", "ruby")
        case Match(_, (Java, name)) => (s"public static void $name() {}\n", "java")
        case Match(_, (Python, name)) => (s"def $name():\n  0\n", "python")
        case Match(_, (Ruby, name)) => (s"def $name do\nend\n", "ruby")
        case NoMatch(_) => ("", "plaintext")
      }
    }
    for (c <- asJS.children) asJS.removeChild(c)
    for (c <- asJS.children) asJS.removeChild(c)
    for (c <- asJS.children) asJS.removeChild(c)
    for (c <- CodeLine.split(text)) asJS.appendChild(c)
    updateLineNums()
    highlight(lang)

  }
  //asJS.appendChild(CodeLine.default)
  updateLineNums()
  highlight("plaintext")
  def lines =
    asJS.children.map(c => CodeLine(this, c.asInstanceOf[html.Element]))
  def updateLineNums(): Unit =
    println(lines)
    println(lines.map(_.asJS.innerHTML))
    for ((child, i) <- lines.zipWithIndex)
      child.asJS.children(0).innerText = s"${i + 1}"
  for (line <- lines)
    println(line)
  def lineCount = lines.size
  def highlight(lang: String): Unit = lines.foreach(_.highlight(lang))

}
