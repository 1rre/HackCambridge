import scalajs.js.annotation._
import scalajs.js
import js.JSConverters._
import io.scalajs.nodejs.buffer.Blob
import io.scalajs.nodejs.setTimeout

@js.native
@JSGlobal
class ResponseTag extends js.Any {
  def send(s: String): Unit = js.native
}
object Main extends App {
  val File = ("./assets/speech.wav")
  println(File)
  import Priv._
  println(ApiKey)
  val deepgram = new Deepgram(ApiKey)
  val fileBuffer = io.scalajs.nodejs.fs.Fs.readFileSync(File)
  val source = new TranscriptionBufferSource(fileBuffer, "audio/wav")
  val trans = deepgram.transcription.live()
  trans.addListener("open", () => {
    println("opened!")
    for (i <- fileBuffer.grouped(1000)) {
      trans.send(i.toJSArray)
    }
    setTimeout(() => trans.finish(), 1000)
  })
  trans.addListener("close", () => {
    println("Closed")
  })
  trans.addListener("transcriptReceived", {t: js.Any => 
    println(t)
  })
  val expr = express.apply()
  expr.listen(4218)
  expr.get("/", (x: js.Any, y: ResponseTag) => y.send("Hello!"))
  expr.post("/", (x: js.Any, y: ResponseTag) => {
    println("afsalf")
    println("safnsa")
  })
}


