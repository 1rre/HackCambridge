import scalajs.js
import scalajs.js.annotation._

@js.native
@JSImport("@deepgram/sdk", "Transcriber")
class Transcriber(credentials: String, apiURL: String) extends js.Object {
  def preRecorded(
      source: TranscriptionSource,
      options: PrerecordedTranscriptionOptions = js.native
  ): js.Promise[PrerecordedTranscriptionResponse] = js.native
  def live(options: LiveTranscriptionOptions = js.native): LiveTranscription = js.native
}
