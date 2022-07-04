import clients.BinanceFuturesApiUserRestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RestUserEndpoints {

    private lateinit var client: BinanceFuturesApiUserRestClient
    private val key = ""
    private val secret = ""

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiUserRestClient(key, secret, "https://testnet.binancefuture.com")
    }

//    @Test
//    fun openOrders() {
//        client.openOrders().let {
//            it.body()?.forEach {
//                println(it)
//            }
//        }
//    }

//    @Test
//    fun positions() {
//        client.positions().let {
//            it.body()?.forEach {
//                println(it)
//            }
//        }
//    }

//    @Test
//    fun newOrder() {
//        client.newOrder(
//            "BTCUSDT",
//            ORDER_SIDE.BUY,
//            price = BigDecimal("17000"),
//            type = ORDER_TYPE.LIMIT,
//            quantity = BigDecimal("0.01"),
//            timeInForce = TIME_IN_FORCE.GTC,
//            newClientOrderId = "testorder"
//        ).let {
//            it.body()?.let {
//                println(it)
//            }
//        }
//    }

//    @Test
//    fun deleteOrder() {
//        client.deleteOrder(
//            "BTCUSDT",
//            origClientOrderId = "testorder"
//        ).let {
//            it.body()?.let {
//                println(it)
//            }
//        }
//    }

//    @Test
//    fun futuresAccountBalance() {
//        client.futuresAccountBalance().let {
//            it.body()?.let {
//                println(it)
//            }
//        }
//    }

}