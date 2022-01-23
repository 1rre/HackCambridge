
import scalajs.js
import scalajs.js.annotation._


@js.native
@JSImport("express",  JSImport.Namespace)
object express extends js.Object {
  def apply(): JSExpress = js.native
}

@js.native
@JSGlobal
class JSExpress extends js.Any {
  def get(path: String, fn: js.Function2[js.Any, ResponseTag, js.Any]): js.Any = js.native
  def post(path: String, fn: js.Function2[js.Any, ResponseTag, js.Any]): js.Any = js.native
  def listen(port: Int): js.Any = js.native
}