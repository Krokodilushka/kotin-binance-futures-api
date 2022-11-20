package clients

import CandlesTickChartIntervalEnum
import ContinuousContractKlineTypeEnum
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import model.websocket.WebSocketEvent
import model.websocket.WebSocketMessage
import okhttp3.*

class BinanceWebSocketClient(
    private val client: OkHttpClient,
    private val webSocketBaseUrl: String
) {

    private var webSocket: WebSocket? = null
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private var listener: WebSocketListener? = null

    fun setListener(listener: WebSocketListener) {
        this.listener = listener
    }

    fun connect(channels: List<WebSocketStream>) {
        require(null !== listener) {
            "Не установлен слушатель"
        }
        val streamingUrl = StringBuilder("$webSocketBaseUrl/stream")
        if (channels.isNotEmpty()) {
            streamingUrl.append("?streams=" + channels.joinToString(separator = "/") { it.toString() })
        }
        val request = Request.Builder().url(streamingUrl.toString()).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun close() {
        webSocket?.close(1000, "Closed by user")
    }

    fun message(message: WebSocketMessage.Request) {
        val jsonString = objectMapper.writeValueAsString(message)
        webSocket?.send(jsonString)
    }

    interface WebSocketCallback {
        fun onEvent(eventWrapper: WebSocketEvent.Wrapper<WebSocketEvent>)
        fun onMessage(message: WebSocketMessage.Wrapper<WebSocketMessage.Wrapper.Response>) {}
        fun onFailure(cause: Throwable)
        fun onClosing(code: Int, reason: String)
        fun onClosed(code: Int, reason: String)
    }

    class BinanceApiWebSocketListener(private val callback: WebSocketCallback) :
        WebSocketListener() {

        private val mapper = ObjectMapper().registerKotlinModule()
        private val eventWrapperReader = mapper.readerFor(WebSocketEvent.Wrapper::class.java)
        private val messageWrapperReader = mapper.readerFor(WebSocketMessage.Wrapper::class.java)
        private var isClosed = false

        override fun onMessage(webSocket: WebSocket, text: String) {
            textToEventWrapper(text)?.let {
                callback.onEvent(it)
            } ?: textToMessageResponse(text)?.let {
                callback.onMessage(it)
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            callback.onClosing(code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            isClosed = true
            callback.onClosed(code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            callback.onFailure(t)
        }

        private fun textToEventWrapper(text: String): WebSocketEvent.Wrapper<WebSocketEvent>? {
            return eventWrapperReader.readValue<WebSocketEvent.Wrapper<WebSocketEvent>>(text)
        }

        private fun textToMessageResponse(text: String): WebSocketMessage.Wrapper<WebSocketMessage.Wrapper.Response>? {
            return messageWrapperReader.readValue<WebSocketMessage.Wrapper<WebSocketMessage.Wrapper.Response>>(text)
        }
    }

    interface WebSocketStream {

        class AllMarketTickersStreams : WebSocketStream {
            override fun toString() = "!ticker@arr"
        }

        class MarkPrice : WebSocketStream {
            override fun toString() = "!markPrice@arr"
        }

        class ContinuousContractKline(
            val pair: String,
            val continuousContractKline: ContinuousContractKlineTypeEnum,
            val intervalEnum: CandlesTickChartIntervalEnum
        ) : WebSocketStream {
            override fun toString(): String =
                "${pair.lowercase()}_${continuousContractKline.name.lowercase()}@continuousKline_${intervalEnum.apiRepresentation}"
        }

        data class UserData(val listenKey: String) : WebSocketStream {
            override fun toString() = listenKey
        }
    }

}