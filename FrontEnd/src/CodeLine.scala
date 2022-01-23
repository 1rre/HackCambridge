import org.scalajs.dom.html
import org.scalajs.dom

object CodeLine {
  val default = {
    val root = dom.document.createElement("div").asInstanceOf[html.Div]
    root.className = "code-line"
    val linenum = dom.document.createElement("code").asInstanceOf[html.Element]
    linenum.className = "linenum"
    root.appendChild(linenum)
    val content = dom.document.createElement("pre").asInstanceOf[html.Pre]
    content.className = "code-content"
    root.appendChild(content)
    val codeContent = dom.document.createElement("code").asInstanceOf[html.Element]
    codeContent.className = s"language-plaintext"
    content.appendChild(codeContent)
    root
  }
    
  def split(txt: String): Array[html.Element] = {
    txt.split("\n").map(a =>
      val line = default.cloneNode(true).asInstanceOf[html.Element]
      line.children(1).children(0).textContent = a
      line
    )
  }
}

class CodeLine(window: CodeWindow, val asJS: html.Element) {
  println(asJS.innerHTML)
  val code = asJS.children(1).children(0).asInstanceOf[html.Element]
  def highlight(lang: String): Unit = {
    code.className = s"language-${lang}"
    hljs.highlightElement(code)
  }
  
}