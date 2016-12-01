organization := "org.ragdan"
name := "smart-room-iot"
version := "1.0.0"
scalaVersion := "2.11.7"

cancelable in Global := true


libraryDependencies ++= Seq(
    "com.codahale.metrics" % "metrics-core" % "3.0.2",
    "joda-time" % "joda-time" % "2.9.6",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.json4s" %% "json4s-native" % "3.5.0"
)
