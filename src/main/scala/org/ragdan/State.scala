package org.ragdan

case class State(on: Boolean, bri: Int, hue: Int, sat: Int, effect: String, xy: Array[Float], ct: Int, alert: String)

object State {
  val green = State(true, 254, 25500, 254, "none", Array(0.1F, 0.8F), 153, "select")
  val red = State(true, 254, 25500, 254, "none", Array(0.6679F, 0.3181F), 153, "select")
  val yellow = State(true, 254, 25500, 254, "none", Array(0.5425F, 0.4196F), 153, "select")
}