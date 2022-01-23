import scalajs.js
import scalajs.js.annotation._
import io.scalajs.nodejs.fs.ReadStream
import io.scalajs.nodejs.buffer.Buffer

trait TranscriptionSource extends js.Object
class TranscriptionReadStreamSource(val stream: ReadStream, val mimeType: String) extends TranscriptionSource
class TranscriptionUrlSource(val url: String) extends TranscriptionSource
class TranscriptionBufferSource(
  val buffer: Buffer,
  val mimetype: String
) extends TranscriptionSource
