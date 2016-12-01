organization := "org.ragdan"
name := "smart-room-iot"
version := "1.0.0"
scalaVersion := "2.11.7"

cancelable in Global := true


libraryDependencies ++= Seq(
    "com.codahale.metrics" % "metrics-core" % "3.0.2",
    "dom4j" % "dom4j" % "1.6.1",
    "jaxen" % "jaxen" % "1.1.6",
    "joda-time" % "joda-time" % "2.9.6",

    "org.scalatest" %% "scalatest" % "2.2.1" % "test"

)
