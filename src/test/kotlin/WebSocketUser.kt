import clients.BinanceFuturesApiUserRestClient
import clients.BinanceWebSocketClient
import model.websocket.MarketEvent
import model.websocket.WebSocketEvent
import model.websocket.WebSocketMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.BinanceApiService

class WebSocketUser {

    private lateinit var client: BinanceFuturesApiUserRestClient
    private val key = ""
    private val secret = ""

    @BeforeEach
    fun beforeEach() {
        client =
            BinanceFuturesApiUserRestClient(key, secret, BinanceApiConstants.REST_API_URL_TESTNET)
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
        }
        val webSocketClient = BinanceWebSocketClient(
            BinanceApiService.httpClient,
            BinanceApiConstants.WEBSOCKET_URL_TESTNET
        )
        webSocketClient.setListener(BinanceWebSocketClient.BinanceApiWebSocketListener(callback))
        val channels = listOf<BinanceWebSocketClient.WebSocketStream>(
            BinanceWebSocketClient.WebSocketStream.UserData(client.startUserDataStream().body()!!.listenKey)
        )
        webSocketClient.connect(channels)
        println("Wait events...")
    }

}