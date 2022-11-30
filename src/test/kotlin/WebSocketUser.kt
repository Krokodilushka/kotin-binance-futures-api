import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceFuturesApiUserRestClient
import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceWebSocketClient
import com.krokodilushka.kotlin_binance_futures_api.model.websocket.WebSocketEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.krokodilushka.kotlin_binance_futures_api.service.BinanceApiService

class WebSocketUser {

    private lateinit var client: BinanceFuturesApiUserRestClient
    private val key = "KPTtqLtTK2LaMcXSlSxONqJ4aIivnigKo4tM0mkvg7l6HpHG8M6nI8VgXKSjX92Y"
    private val secret = "BFXs8JgYpL5g1AptosPqFKBfldqLzw2ID0o82ST7Y8H40BWsh32HNtg40wqmyiFF"

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiUserRestClient(key, secret, "https://fapi.binance.com")
    }

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
        val webSocketClient = BinanceWebSocketClient(
            BinanceApiService.httpClient,
            "wss://fstream.binance.com"
        )
        webSocketClient.setListener(BinanceWebSocketClient.BinanceApiWebSocketListener(callback))
        val channels = listOf<BinanceWebSocketClient.WebSocketStream>(
            BinanceWebSocketClient.WebSocketStream.UserData(client.startUserDataStream().body()!!.listenKey)
        )
        webSocketClient.connect(channels)
        println("Wait events...")
        Thread.sleep(600000L)
    }

}