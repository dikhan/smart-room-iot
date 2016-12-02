organization := "smart-room"
name := "smart-room-iot"
version := "1.0.0"

scalaVersion := "2.11.7"
val akkaV = "2.3.9"
val sprayV = "1.3.3"

libraryDependencies ++= Seq(
    "com.codahale.metrics" % "metrics-core" % "3.0.2",
    "joda-time" % "joda-time" % "2.9.6",
    "org.joda" % "joda-convert" % "1.2",


    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-client" % sprayV,
    "io.spray" %% "spray-json" % "1.3.1",
    "io.spray" %% "spray-testkit" % sprayV % "test",

    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",

    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.json4s" %% "json4s-native" % "3.5.0",

    "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
