package smartroom

import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.routing.{Directives, HttpServiceActor}
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import Directives._
import org.joda.time.DateTime
import spray.http.StatusCodes
import spray.http.StatusCodes._

class ExchangeHttpService(interface: String = "localhost", port: Int) {

    def start = {
        implicit val as = ActorSystem("restservice")
        val httpActor = as.actorOf(Props(ActorA))
        implicit val timeout = Timeout(1, TimeUnit.SECONDS)
        IO(Http) ? Http.Bind(httpActor, interface, port)
    }
}

object ActorA extends HttpServiceActor {
    val route =
        path("book" / Segment) { minutesString: String =>
            post {
                try {
                    val minutes = Integer.valueOf(minutesString)
                    val start = new DateTime()
                    val end = new DateTime().plusMinutes(minutes)

                    SmartRoom.exchange.bookRoom(SmartRoom.chosenRoom, start, end)

                    println(s"booked room for $minutes minutes")
                    complete("booked")
                } catch {
                    case e: Throwable =>
                        println(e.getMessage)
                        complete(BadRequest, "Invalid minutes")
                }

            }
        }

    override def receive: Receive = {
        runRoute(route)
    }
}
