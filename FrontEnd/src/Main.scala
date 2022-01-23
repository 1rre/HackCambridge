import scalajs.js.annotation._
import scalajs.js
import js.JSConverters._
import org.scalajs.dom.{window, document, html}
import scala.scalajs.js.JSON
import org.scalajs.dom.MediaStreamConstraints
import org.scalajs.dom.AudioContext
import org.scalajs.dom.MediaStream
import org.scalajs.dom.Blob
import org.scalajs.dom.Event
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.WebSocket
import org.scalajs.dom.MutationObserver

@js.native
@JSGlobal
object hljs extends js.Any {
  def highlightAll(): Unit = js.native
  def highlightElement(e: html.Element): Unit = js.native
}

@js.native
@JSGlobal
class MediaRecorder(stream: MediaStream, options: js.Object = js.native) extends js.Any {
  var ondataavailable: js.Function1[BlobEvent, Unit] = js.native
  var onstop: js.Function1[Event, Unit] = js.native
  def start(i: Int = js.native): Unit = js.native
  def stop(): Unit = js.native
}

@js.native
@JSGlobal
class BlobEvent(stream: MediaStream) extends Event(null, null) {
  val data: Blob = js.native
}


object Main extends App {
  lazy val Transcript = document.getElementById("text-transcript").asInstanceOf[html.Div]
  lazy val Output = CodeWindow(document.getElementById("text-output").asInstanceOf[html.Div])
  class Button(asDiv: html.Div, var enabled: Boolean) {
    def enable() = if (!enabled) {
      enabled = true
      asDiv.style.color = "white"
    }
    def disable() = if (enabled) {
      enabled = false
      asDiv.style.color = "grey"
    }
    def onclick = asDiv.onclick
    def onclick_=(f: js.Function1[Event, _]): Unit = asDiv.onclick = e => if (enabled) f(e)
    val style = asDiv.style
  }
  lazy val StartButton = Button(document.getElementById("start-button").asInstanceOf[html.Div], true)
  lazy val StopButton = Button(document.getElementById("stop-button").asInstanceOf[html.Div], false)
  var Recorder: MediaRecorder = null
  window.onload = x => {
    Output.updateLineNums()
    Output.highlight("plaintext")
    Transcript.addEventListener("DOMSubtreeModified", e => {
      println(Transcript.children(0).innerText)
      Output.update()
    })
    StopButton.onclick = e => {
      Recorder.stop()
      StopButton.disable()
      StartButton.enable()
    }
    StartButton.onclick = e => {
      window.navigator.mediaDevices.getUserMedia(new MediaStreamConstraints {override val audio = true}) `then` {x => 

        object opts extends js.Object {val mimeType = "audio/webm"}
        val mr = new MediaRecorder(x, opts)
        val socket = new WebSocket("wss://api.deepgram.com/v1/listen", js.Array("token", "2e94edd2d58ac4338ede17dd2049f41458eb835d"))
        socket.onopen = e => {
          StartButton.disable()
          StopButton.enable()
          mr.ondataavailable = e =>
            if (e.data.size > 0 && socket.readyState == 1)
              socket.send(e.data)
          mr.start(2500)
          Recorder = mr
          mr.onstop = e => socket.close()
        }
        socket.onmessage = msg =>
          Transcript.children(0).textContent += JSON.parse(msg.data.toString).selectDynamic("channel").selectDynamic("alternatives").asInstanceOf[js.Array[js.Dynamic]].head.selectDynamic("transcript").asInstanceOf[String]
        socket.onclose = e =>
          println("Socket Closing")
          StartButton.style.color = "white"
          StopButton.style.color = "grey"
        socket.onerror = e => println(s"Error: $e")
      }
    }
  }
}
