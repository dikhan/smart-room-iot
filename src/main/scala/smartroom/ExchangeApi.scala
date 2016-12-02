package smartroom

import java.io.PrintWriter

import org.joda.time.DateTime

import scala.sys.process._

class ExchangeApi(user:String, password: String) {

    val exchangeEndPoint = "https://mail.cisco.com/ews/exchange.asmx"
    val bias = 0
    val getRoomAvailabilityXmlTemplate = "src/main/resources/"


    def isRoomAvailable(room: String, start: DateTime = DateTime.now()
                        , end: DateTime = DateTime.now().plusMinutes(30)): String = {

        val startString = start.toString
        val endString = end.toString


        var xml = io.Source.fromFile("src/main/resources/getavailability_template.xml").mkString
        xml = xml.replace("$bias", s"$bias")
          .replace("$room", room)
          .replace("$start", startString)
          .replace("$end", endString)


        new PrintWriter("/tmp/check_room_status") {
            write(xml)
            close()
        }


        val stdout = s"bash get_room.sh $user $password".!!
//        println(stdout)

        val pattern = "MergedFreeBusy.*?>\\d{2}".r

        pattern.findFirstIn(stdout) match {
            case Some(x) if x.length > 0 =>
                x.substring(x.length - 2).charAt(0) match {
                    case '0' => "free"
                    case '1' => "tentative"
                    case '2' => "busy"
                    case '3' => "unavailable"
                }
            case _ => println("error")
                "error"
        }


//        println(state)


//        false
    }

}
