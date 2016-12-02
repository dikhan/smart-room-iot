package org.ragdan

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write

import scalaj.http.Http

object HueApi {

  implicit val formats = DefaultFormats

  val endPoint = "http://192.168.1.115"
  val token = "pBOzJTQATMmMsoT7bWelIwDI7SYQPtTSOW-UGYO8"

  def updateLight(lightId:String = "1", state: State) = {
    println("turning state: " + state)
    val url = s"$endPoint/api/$token/lights/$lightId/state"
    var response = Http(url).put(write(state)).header("content-type", "application/json").asString
  }

}
