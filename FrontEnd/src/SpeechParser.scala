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
  def introAndname = intro ~ (str(" called ") /> fnName) #> {
    case (Java, str) =>
      val x = str.trim.split(' ').map(_.capitalize).mkString
      println((x, str))
      (Java, x.head.toLower +: x.tail)
    case (lang, str)=> (lang, str.trim.replace(' ', '_'))
  }
  def fnName: Parser[String] =
    (str("which") >> "") | (new TypeParser[Char]() ~ fnName #> ((a,b) => a +: b)) | (TypeParser[Char]() #> (_.toString))
  def argType =  Seq(" an integer", " a string", " a double" ) map str zip Seq("int", "string", "double") map (_>>_) reduce (_|_)
  def args(i: Int): Parser[Seq[String]] =
    if (i == 1) argType #> (Seq(_)) else (1 until i).foldLeft(str(" and") /> argType #> (Seq(_)))((a,_) => argType ~ a #> (_+:_))
  def nArgs = Seq(
    str("no arguments") >> Seq[String](),
    str("one argument") /> args(1),
    Seq("two ", "three ", "four ", "five ", "six ", "seven ", "eight ", "nine ") map str zip (2 to 9) map (_~str("arguments") /> args(_)) reduce (_|_)
  ).reduce(_|_)

  sealed trait Command
  case class PrintArg(i: Int) extends Command
  def argStatement = str(" takes ") /> nArgs
  type HeaderChoice = (Language, String) | (Language, String, Seq[String]) | (Language, String, Seq[String], String) | (Language, String, Seq[String], String, Seq[Command])
  def headerNoRtn = ((introAndname ~ argStatement) #> {case ((a,b),c) => (a,b,c)}).asInstanceOf[Parser[HeaderChoice]] | introAndname.asInstanceOf[Parser[HeaderChoice]]
  def header = (headerNoRtn ~ returnType #> {case ((a,b),c) => (a,b,c); case ((a,b,c),d) => (a,b,c,d)}).asInstanceOf[Parser[HeaderChoice]] | headerNoRtn
  def returnType = str(" and returns") /> argType
  def returnStatment = str(" that prints the ") /> (Seq("first ", "second ", "third ", "fourth ", "fifth ", "sixth ", "seventh ", "eighth ", "ninth ") map (_ + "argument") map str zip (1 to 9) map (_>>_) reduce (_|_))
  def fullFn = (header ~ returnStatment #> {case ((a,b),c) => (a,b); case ((a,b,c),d) => (a,b,c); case ((a,b,c,d),e) => (a,b,c,d,Seq(PrintArg(e)))}).asInstanceOf[Parser[HeaderChoice]] | header.asInstanceOf[Parser[HeaderChoice]]
}