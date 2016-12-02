package smartroom

import java.io.{File, PrintWriter}

import org.joda.time.DateTime

import scala.sys.process._


case class Room(name: String, email: String)

object Rooms {
    val LON07_ALTO = Room("alto", "CONF_46608@cisco.com")
    val LON07_BOARDROOM = Room("boardroom", "CONF_37445@cisco.com")
}

class ExchangeApi(user: String, password: String) {

    val exchangeEndPoint = "https://mail.cisco.com/ews/exchange.asmx"
    val bias = 0
    val getRoomAvailabilityXmlTemplate = "src/main/resources/"
    var roomAvailabilityPostDataTemplate = io.Source.fromFile("src/main/resources/getavailability_template.xml").mkString
    var bookRoomPostDataTemplate = io.Source.fromFile("src/main/resources/bookroom_template.xml").mkString
    val domain = "cisco.com"

    def bookRoom(room: Room, start: DateTime = DateTime.now(),
                 end: DateTime = DateTime.now().plusMinutes(30)): Boolean = {

        val startString = start.toString
        val endString = end.toString

        val bookRoomPostData = bookRoomPostDataTemplate
          .replace("$roomemail", room.email)
          .replace("$room", room.name)
          .replace("$start", startString)
          .replace("$end", endString)
          .replace("$subject", "meeting - test")
          .replace("$meeting_body", "meeting-test")
          .replace("$useremail", s"$user@$domain")
          .replace("$timezone", "-P0M")

        val bookRoomPostDataFile = File.createTempFile("bookroom", "tmp");
        println("data to send: " + bookRoomPostData)

        new PrintWriter(bookRoomPostDataFile) {
            write(bookRoomPostData)
            close()
        }

        val cmdOut = s"bash book_room.sh $user $password ${bookRoomPostDataFile.getAbsolutePath}".!!
        println(cmdOut)

        cmdOut.contains("ResponseClass=\"Success\"")
    }


    def isRoomAvailable(room: Room, start: DateTime = DateTime.now(),
                        end: DateTime = DateTime.now().plusMinutes(30)): String = {

        val startString = start.toString
        val endString = end.toString


        val roomAvailabilityPostData = roomAvailabilityPostDataTemplate.replace("$bias", s"$bias")
          .replace("$room", room.email)
          .replace("$start", startString)
          .replace("$end", endString)


        new PrintWriter("/tmp/check_room_status") {
            write(roomAvailabilityPostData)
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
    }

}
