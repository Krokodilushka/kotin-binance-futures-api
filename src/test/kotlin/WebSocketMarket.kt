import clients.BinanceWebSocketClient
import model.websocket.MarketEvent
import model.websocket.WebSocketEvent
import model.websocket.WebSocketMessage
import org.junit.jupiter.api.Test
import service.BinanceApiService

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
        }

        val webSocketClient =
            BinanceWebSocketClient(BinanceApiService.httpClient, "wss://stream.binancefuture.com")
        webSocketClient.setListener(BinanceWebSocketClient.BinanceApiWebSocketListener(callback))
        val channels = listOf<BinanceWebSocketClient.WebSocketStream>(
            BinanceWebSocketClient.WebSocketStream.AllMarketTickersStreams()
        )
        webSocketClient.connect(channels)
        println("Wait events...")
        Thread.sleep(10000L)
    }

}