package com.krokodilushka.kotlin_binance_futures_api

import com.krokodilushka.kotlin_binance_futures_api.clients.BinanceWebSocketClient
import com.krokodilushka.kotlin_binance_futures_api.model.websocket.WebSocketEvent
import com.krokodilushka.kotlin_binance_futures_api.service.BinanceApiService

fun main() {
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
        BinanceApiConstants.WEBSOCKET_URL_TESTNET
    )
    webSocketClient.setListener(BinanceWebSocketClient.BinanceApiWebSocketListener(callback))
    val channels = listOf<BinanceWebSocketClient.WebSocketStream>(
        BinanceWebSocketClient.WebSocketStream.AllMarketTickersStreams()
    )
    webSocketClient.connect(channels)
    Thread.sleep(2000L)
    webSocketClient.close()
    BinanceApiService.httpClient.connectionPool().evictAll()
}