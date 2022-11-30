import com.krokodilushka.kotlin_binance_futures_api.CandlesTickChartIntervalEnum
import com.krokodilushka.kotlin_binance_futures_api.ContinuousContractKlineTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceWebSocketClient
import com.krokodilushka.kotlin_binance_futures_api.model.websocket.WebSocketEvent
import com.krokodilushka.kotlin_binance_futures_api.model.websocket.WebSocketMessage
import org.junit.jupiter.api.Test
import com.krokodilushka.kotlin_binance_futures_api.service.BinanceApiService

class WebSocketMarket {

    @Test
    fun test() {
        val callback = object : BinanceWebSocketClient.WebSocketCallback {
            override fun onEvent(eventWrapper: WebSocketEvent.Wrapper<WebSocketEvent>) {
                println("onEvent: $eventWrapper")
            }

            override fun onFailure(cause: Throwable) {
                println("onFailure: $cause")
                throw cause
            }

            override fun onClosing(code: Int, reason: String) {
                println("onClosing code: $code, reason: $reason")
            }

            override fun onClosed(code: Int, reason: String) {
                println("onClosed code: $code, reason: $reason")
            }
        }

        val webSocketClient =
            BinanceWebSocketClient(BinanceApiService.httpClient, "wss://fstream.binance.com")
        webSocketClient.setListener(BinanceWebSocketClient.BinanceApiWebSocketListener(callback))
        val channels = listOf<BinanceWebSocketClient.WebSocketStream>(
//            BinanceWebSocketClient.WebSocketStream.AllMarketTickersStreams(),
//            BinanceWebSocketClient.WebSocketStream.MarkPrice(),
//            BinanceWebSocketClient.WebSocketStream.ContinuousContractKline(
//                "btcusdt",
//                ContinuousContractKlineTypeEnum.PERPETUAL,
//                CandlesTickChartIntervalEnum.H1
//            )
        )
        webSocketClient.connect(channels)
        val msg = listOf(
//            BinanceWebSocketClient.WebSocketStream.ContinuousContractKline(
//                "api3usdt",
//                com.krokodilushka.kotlin_binance_futures_api.ContinuousContractKlineTypeEnum.PERPETUAL,
//                com.krokodilushka.kotlin_binance_futures_api.CandlesTickChartIntervalEnum.H1
//            ).getStreamName(),
            BinanceWebSocketClient.WebSocketStream.ContinuousContractKline(
                "btcusdt",
                ContinuousContractKlineTypeEnum.PERPETUAL,
                CandlesTickChartIntervalEnum.M1
            )
        )
        webSocketClient.message(WebSocketMessage.Request(WebSocketMessage.Method.SUBSCRIBE, msg, 1))
        println("Wait events...")
        Thread.sleep(60000L)
    }

}