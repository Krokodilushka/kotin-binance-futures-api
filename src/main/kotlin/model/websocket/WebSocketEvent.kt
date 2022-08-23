package model.websocket

import clients.BinanceWebSocketClient
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

abstract class WebSocketEvent {

    @JsonDeserialize(using = Wrapper.Deserializer::class)
    data class Wrapper<T : WebSocketEvent>(
        @JsonProperty("stream")
        val stream: String,
        @JsonProperty("data")
        val event: T
    ) {
        class Deserializer : JsonDeserializer<Wrapper<WebSocketEvent>>() {
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): Wrapper<WebSocketEvent>? {
                val node = jp.codec.readTree<JsonNode>(jp)
                if (null !== node["stream"] && null !== node["data"]) {
                    val stream = node["stream"].textValue()
                    val eventType = node["data"]["e"]?.textValue()
                    val json = node["data"].toString()
                    val event = when {
                        // market data
                        stream.endsWith(
                            BinanceWebSocketClient.WebSocketStream.AllMarketTickersStreams().getStreamName()
                        ) -> JsonToObject.convert(
                            json,
                            object : TypeReference<MarketEvent.AllMarketTickerEvent.List>() {}
                        )

                        stream.endsWith(
                            BinanceWebSocketClient.WebSocketStream.MarkPrice().getStreamName()
                        ) -> JsonToObject.convert(
                            json,
                            object : TypeReference<MarketEvent.MarkPriceEvent.List>() {}
                        )

                        eventType == "continuous_kline" -> JsonToObject.convert(
                            json,
                            MarketEvent.ContinuousContractKlineEvent::class.java
                        )
                        // user events
                        else -> when (eventType) {
                            "MARGIN_CALL" -> JsonToObject.convert(
                                json,
                                UserDataEvent.MarginCall::class.java
                            )

                            "ACCOUNT_UPDATE" -> JsonToObject.convert(
                                json,
                                UserDataEvent.AccountUpdate::class.java
                            )

                            "ORDER_TRADE_UPDATE" -> JsonToObject.convert(
                                json,
                                UserDataEvent.OrderTradeUpdate::class.java
                            )

                            "ACCOUNT_CONFIG_UPDATE" -> JsonToObject.convert(
                                json,
                                UserDataEvent.AccountConfigUpdate::class.java
                            )

                            "listenKeyExpired" -> JsonToObject.convert(
                                json,
                                UserDataEvent.DataStreamExpired::class.java
                            )

                            else -> throw Exception("WebSocket received unknown event $eventType")
                        }
                    }
                    return Wrapper(stream, event)
                }
                return null
            }
        }
    }
}