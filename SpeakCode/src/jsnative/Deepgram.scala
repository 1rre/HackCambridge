import scalajs.js
import scalajs.js.annotation._

@js.native
@JSImport("@deepgram/sdk", "Deepgram")
class Deepgram(apiKey: String, apiURL: String = js.native) extends js.Object {
  def transcription: Transcriber = js.native
}