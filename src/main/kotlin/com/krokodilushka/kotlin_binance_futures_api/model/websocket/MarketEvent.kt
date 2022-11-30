package com.krokodilushka.kotlin_binance_futures_api.model.websocket

import com.krokodilushka.kotlin_binance_futures_api.CandlesTickChartIntervalEnum
import com.krokodilushka.kotlin_binance_futures_api.ContinuousContractKlineTypeEnum
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.krokodilushka.kotlin_binance_futures_api.JsonToObject
import java.math.BigDecimal

sealed class MarketEvent : WebSocketEvent() {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AllMarketTickerEvent(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("s") val symbol: String,
        @JsonProperty("p") val priceChange: BigDecimal,
        @JsonProperty("P") val priceChangePercent: BigDecimal,
        @JsonProperty("w") val weightedAveragePrice: BigDecimal,
        @JsonProperty("c") val lastPrice: BigDecimal,
        @JsonProperty("Q") val lastQuantity: BigDecimal,
        @JsonProperty("o") val openPrice: BigDecimal,
        @JsonProperty("h") val highPrice: BigDecimal,
        @JsonProperty("l") val lowPrice: BigDecimal,
        @JsonProperty("v") val totalTradedBaseAssetVolume: BigDecimal,
        @JsonProperty("q") val totalTradedQuoteAssetVolume: BigDecimal,
        @JsonProperty("O") val statisticsOpenTime: Long,
        @JsonProperty("C") val statisticsCloseTime: Long,
        @JsonProperty("F") val firstTradeId: Long,
        @JsonProperty("L") val lastTradeId: Long,
        @JsonProperty("n") val totalNumberOfTrades: Long,
    ) : MarketEvent() {
        @JsonDeserialize(using = Deserializer::class)
        data class List(val list: kotlin.collections.List<AllMarketTickerEvent>) : WebSocketEvent()

        class Deserializer : JsonDeserializer<List>() {
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): List {
                val node = jp.codec.readTree<JsonNode>(jp)
                val json = node.toString()
                val typeReference = object : TypeReference<kotlin.collections.List<AllMarketTickerEvent>>() {}
                val list = JsonToObject.convert(json, typeReference)
                return List(list)
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class MarkPriceEvent(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("s") val symbol: String,
        @JsonProperty("p") val markPrice: BigDecimal,
        @JsonProperty("i") val indexPrice: BigDecimal,
        @JsonProperty("P") val estimatedSettlePrice: BigDecimal,
        @JsonProperty("r") val fundingRate: BigDecimal,
        @JsonProperty("T") val nextFundingTime: Long,
    ) : MarketEvent() {
        @JsonDeserialize(using = Deserializer::class)
        data class List(val list: kotlin.collections.List<MarkPriceEvent>) : WebSocketEvent()

        class Deserializer : JsonDeserializer<List>() {
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): List {
                val node = jp.codec.readTree<JsonNode>(jp)
                val json = node.toString()
                val typeReference = object : TypeReference<kotlin.collections.List<MarkPriceEvent>>() {}
                val list = JsonToObject.convert(json, typeReference)
                return List(list)
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ContinuousContractKlineEvent(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("ps") val pair: String,
        @JsonProperty("ct") val contractType: ContinuousContractKlineTypeEnum,
        @JsonProperty("k") val kline: Kline,
    ) : MarketEvent() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Kline(
            @JsonProperty("t") val klineStartTime: Long,
            @JsonProperty("T") val klineCloseTime: Long,
            @JsonProperty("i") val interval: CandlesTickChartIntervalEnum,
            @JsonProperty("f") val firstTradeID: Long,
            @JsonProperty("L") val lastTradeID: Long,
            @JsonProperty("o") val open: BigDecimal,
            @JsonProperty("c") val close: BigDecimal,
            @JsonProperty("h") val high: BigDecimal,
            @JsonProperty("l") val low: BigDecimal,
            @JsonProperty("v") val volume: BigDecimal,
            @JsonProperty("n") val numberOfTrades: Long,
            @JsonProperty("x") val isClosed: Boolean,
            @JsonProperty("q") val quoteAssetVolume: BigDecimal,
            @JsonProperty("V") val takerBuyVolume: BigDecimal,
            @JsonProperty("Q") val takerBuyQuoteAssetVolume: BigDecimal,
        )
    }
}