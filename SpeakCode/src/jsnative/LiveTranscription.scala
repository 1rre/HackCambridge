import scalajs.js
import scalajs.js.annotation._
import io.scalajs.nodejs.events.EventEmitter
import scala.collection.mutable.ArrayBuffer
import io.scalajs.nodejs.buffer.Blob
import io.scalajs.nodejs.buffer.Buffer

@js.native
@JSImport("@deepgram/sdk", "LiveTranscription")
class LiveTranscription extends EventEmitter {
  def getReadyState: Int = js.native
  def send(data: js.Array[_]): Unit = js.native
  def send(data: String): Unit = js.native
  def send(data: Blob): Unit = js.native
  def send(data: Buffer): Unit = js.native
  def finish(): Unit = js.native
}