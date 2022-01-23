import es.tmoor.parsing._

object SpeechParser extends Parsers[Char] {
  def str(s: String) = s.map(ElemParser.apply(_)).reduce[Parser[_]](_~_)
  sealed trait Language
  object Python extends Language
  object Ruby extends Language
  object Java extends Language
  val python: Parser[Language] = str("python") >> Python
  val ruby: Parser[Language] = str("ruby") >> Ruby
  val java: Parser[Language] = str("java") >> Java
  def languageName = python | ruby | java
  def intro = str("declare a function in ") /> languageName
  def introAndname = intro ~ (str(" called") /> fnName) #> {
    case (Java, str) =>
      val x = str.trim.split(' ').map(_.capitalize).mkString
      println((x, str))
      (Java, x.head.toLower +: x.tail)
    case (lang, str)=> (lang, str.trim.replace(' ', '_'))
  }
  def fnName: Parser[String] = (str(" which") >> "") | (new TypeParser[Char]() ~ fnName #> ((a,b) =>
    println((a,b))
    a +: b
  )) | (TypeParser[Char]() #> (_.toString))
}