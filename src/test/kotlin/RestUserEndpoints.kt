import com.krokodilushka.kotlin_binance_futures_api.*
import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceFuturesApiUserRestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RestUserEndpoints {

    private lateinit var client: BinanceFuturesApiUserRestClient
    private val key = "e9f7a5acad7f97d57955387460171255dac2f80413305617d0dacf6808b8bff0"
    private val secret = "d1118c39e2d38412ba50a8923398feba0c015c114ac1a784fe6ba308d0ff8fe4"

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiUserRestClient(key, secret, "https://testnet.binancefuture.com")
    }

    @Test
    fun openOrders() {
        client.openOrders().let {
            it.body()?.forEach {
                println(it)
            }
        }
    }

    @Test
    fun positions() {
        client.positions().let {
            it.body()?.forEach {
                println(it)
            }
        }
    }

    @Test
    fun newOrder() {
        client.newOrder(
            "BNBUSDT",
            OrderSideEnum.BUY,
            positionSide = PositionSideEnum.BOTH,
            price = BigDecimal("200"),
            type = OrderTypeEnum.LIMIT,
            quantity = BigDecimal("3"),
            timeInForce = TimeInForceEnum.GTC,
            newClientOrderId = "closebnb"
        ).let {
            it.body()?.let {
                println(it)
            }
        }
    }

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

//    @Test
//    fun currentMultiAssetsMode() {
//        client.currentMultiAssetsMode().let {
//            it.body()?.let {
//                println(it)
//            }
//        }
//    }

//    @Test
//    fun changeMultiAssetsMode() {
//        client.changeMultiAssetsMode(true).let {
//            it.body()?.let {
//                println(it)
//            }
//        }
//    }

    @Test
    fun accountInformation() {
        client.accountInformation().let {
            it.body()?.let {
                println(it)
                it.assets.forEach {
                    println(it)
                    println("openOrderInitialMargin: ${it.openOrderInitialMargin}")
                }
                it.positions.find { it.symbol == "BTCUSDT" }.let {
                    println(it)
                }
            }
        }
    }

//    @Test
//    fun leverageBracket() {
//        client.leverageBracket().let {
//            it.body()?.let {
//                it.find { it.symbol == "BTCUSDT" }?.brackets?.forEach {
//                    println(it)
//                }
//            }
//        }
//    }

//    @Test
//    fun changeInitialLeverage() {
//        client.changeInitialLeverage("BTCUSDT", 5).let {
//            println(it.body())
//        }
//    }

    @Test
    fun changeMarginType() {
        client.changeMarginType("BTCUSDT", MarginTypeEnum.ISOLATED).let {
            println(it.body())
        }
    }

}