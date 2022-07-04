import clients.BinanceFuturesApiMarketRestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RestMarketEndpoints {

    private lateinit var client: BinanceFuturesApiMarketRestClient

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiMarketRestClient(apiUrl = "https://testnet.binancefuture.com")
    }

//    @Test
//    fun exchangeInfo() {
//        client.exchangeInfo().let {
//            println(it.body())
//        }
//    }

//    @Test
//    fun candlesTick() {
//        client.candlesTick("BTCUSDT", CANDLESTICK_CHART_INTERVAL.H1).let {
//            println(it.body())
//        }
//    }

}