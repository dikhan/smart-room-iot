package smartroom

import java.net._
import java.util.concurrent._

import com.codahale.metrics.SlidingWindowReservoir

object SmartRoom extends App {

    val m = new SlidingWindowReservoir(30)
    val udpPort = 5006
    val bookingServicePort = 9000

    val MOTION_DETECTED = 1
    val MOTION_NOT_DETECTED = 0

    val CHECK_INTERVAL = 15


    println(s"motion update service running at $udpPort")
    println(s"room booking service running at $bookingServicePort")

    val chosenRoom = obtainRoom
    println(s"using room: $chosenRoom")
    val exchange = createExchangeApi

    // initialize with motion not detected
    (1 to 5).foreach { x =>
        m.update(MOTION_NOT_DETECTED)
    }


    def obtainRoom: Room = {
        print("enter room (name,email): ")
        new String(System.console().readLine()) match {
            case x if !x.isEmpty =>
                x.split(",") match {
                    case Array(name, roomName) => Room(name, roomName)
                }
            case _ => Rooms.LON07_ALTO
        }
    }

    val udpListener = new Thread(new Runnable() {
        def run() {

            val udpServerSocket = new DatagramSocket(udpPort)

            val buf = new Array[Byte](1000)
            val udpPacket = new DatagramPacket(buf, buf.length)

            while (true) {
                udpServerSocket.receive(udpPacket)
                val data = udpPacket.getData
                val s = new String(data, 0, udpPacket.getLength)

                if (s == "motion detected") {
                    m.update(MOTION_DETECTED)
                } else {
                    m.update(MOTION_NOT_DETECTED)
                }
            }
        }
    })
    udpListener.start()


    val controlLightRunnable = new Runnable() {
        def run() = {
            val isRoomOccupied = m.getSnapshot.getValues.contains(MOTION_DETECTED)
            println("room occupied: " + isRoomOccupied)
            val roomStatus = exchange.isRoomAvailable(chosenRoom)
            println("exchange room status: " + roomStatus)


            if (!isRoomOccupied && roomStatus == "free") {
                println("trigger green light on smart bulb")
                HueApi.updateLight(state = State.green)
            } else if (isRoomOccupied && roomStatus == "free") {
                println("trigger amber light on smart bulb")
                HueApi.updateLight(state = State.yellow)
            } else if (!isRoomOccupied && roomStatus != "free") {
                println("trigger amber light on smart bulb")
                HueApi.updateLight(state = State.yellow)
            } else {
                println("trigger red light on smart bulb")
                HueApi.updateLight(state = State.red)
            }
        }
    }

    val executor = Executors.newSingleThreadScheduledExecutor()
    executor.scheduleAtFixedRate(controlLightRunnable, 5, CHECK_INTERVAL, TimeUnit.SECONDS)

    // start room booking service
    new ExchangeHttpService("0.0.0.0", bookingServicePort).start

    def createExchangeApi: ExchangeApi = {
        print("enter user: ")
        val user = new String(System.console().readLine())

        print("enter password: ")
        val password = new String(System.console().readPassword())

        new ExchangeApi(user, password)
    }

}
