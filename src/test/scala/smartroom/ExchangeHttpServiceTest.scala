package smartroom

import org.scalatest.FlatSpec

object ExchangeHttpServiceTest extends App {
    new ExchangeHttpService(port = 9000).start

}
