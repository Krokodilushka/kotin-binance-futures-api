import com.krokodilushka.kotlin_binance_futures_api.CandlesTickChartIntervalEnum
import com.krokodilushka.kotlin_binance_futures_api.ContinuousContractKlineTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceFuturesApiMarketRestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RestMarketEndpoints {

    private lateinit var client: BinanceFuturesApiMarketRestClient

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiMarketRestClient(apiUrl = "https://fapi.binance.com")
    }

//    @Test
//    fun exchangeInfo() {
//        client.exchangeInfo().let {
//            println(it.body())
//        }
//    }

//    @Test
//    fun candlesTick() {
//        client.kline("SUSHIUSDT", com.krokodilushka.kotlin_binance_futures_api.CandlesTickChartIntervalEnum.H4, limit = 42).body()?.forEach {
//            println("listOf(\"${it.openTime}\",\"${it.open}\",\"${it.high}\",\"${it.low}\",\"${it.close}\"),")
//        }
//    }

    @Test
    fun continuousContractKline() {
        client.continuousContractKline(
            "BTCUSDT",
            ContinuousContractKlineTypeEnum.PERPETUAL,
            CandlesTickChartIntervalEnum.H4,
            limit = 42
        ).body()?.forEach {
            println("listOf(\"${it.openTime}\",\"${it.open}\",\"${it.high}\",\"${it.low}\",\"${it.close}\"),")
        }
    }

//    @Test
//    fun candlesTick() {
//        client.markPrice("BTCUSDT").let {
//            println(it.body())
//        }
//    }

    @Test
    fun price() {
        client.price().let {
            println(it.body())
        }
    }

}