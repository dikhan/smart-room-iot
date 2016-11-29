import com.codahale.metrics.SlidingWindowReservoir
import java.net._
import java.util.concurrent._

object Smart extends App {

    val m = new SlidingWindowReservoir(5)
    val port = 5006

    // initialize with motion not detected
    m.update(0)
    m.update(0)
    m.update(0)
    m.update(0)
    m.update(0)


    val udpListener = new Thread(new Runnable() {
        def run() {

            val udpServerSocket = new DatagramSocket(port)

            val buf = new Array[Byte](1000)
            val udpPacket = new DatagramPacket(buf, buf.length)

            while (true) {
                udpServerSocket.receive(udpPacket)
                val data = udpPacket.getData
                val s = new String(data, 0, udpPacket.getLength)

                if (s == "motion detected") {
                    m.update(1)
                } else {
                    m.update(0)
                }
            }
        }
    })
    udpListener.start()


    val controlLightRunnable = new Runnable() {
        def run() = {
//            println(m.getSnapshot.getValues.toList)
            val motionDetected = m.getSnapshot.getValues.contains(1)
            if (motionDetected)
                println("trigger green light on smart bulb")
            else
                println("trigger red light on smart bulb")
        }
    }

    val executor = Executors.newSingleThreadScheduledExecutor()
    executor.scheduleAtFixedRate(controlLightRunnable, 5, 5, TimeUnit.SECONDS)

}
