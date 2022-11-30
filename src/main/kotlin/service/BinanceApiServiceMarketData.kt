package service

import JsonToObject
import OrderTypeEnum
import RateLimitEnum
import RateLimitIntervalEnum
import TimeInForceEnum
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

interface BinanceApiServiceMarketData {

    @GET("/fapi/v1/time")
    fun time(): Call<Time>

    @GET("/fapi/v1/exchangeInfo")
    fun exchangeInfo(): Call<ExchangeInfo>

    @GET("/fapi/v1/klines")
    fun kline(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long? = null,
        @Query("endTime") endTime: Long? = null,
        @Query("limit") limit: Int? = null
    ): Call<List<Candle>>

    @GET("/fapi/v1/continuousKlines")
    fun continuousContractKline(
        @Query("pair") pair: String,
        @Query("contractType") contractType: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long? = null,
        @Query("endTime") endTime: Long? = null,
        @Query("limit") limit: Int? = null
    ): Call<List<Candle>>

    @GET("/fapi/v1/premiumIndex")
    fun markPrice(@Query("symbol") symbol: String?): Call<MarkPrice.List>

    @GET("/fapi/v1/ticker/price")
    fun price(): Call<Price.List>
}

@JsonIgnoreProperties(ignoreUnknown = false)
data class Time(
    val serverTime: Long,
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class ExchangeInfo(
    val exchangeFilters: List<Nothing>,
    val rateLimits: List<RateLimit>,
    val serverTime: Long,
    val assets: List<Asset>,
    val symbols: List<Symbol>,
    val timezone: String,
    val futuresType: ExchangeInfoFuturesType,
) {
    @JsonIgnoreProperties(ignoreUnknown = false)
    data class RateLimit(
        val rateLimitType: RateLimitEnum,
        val interval: RateLimitIntervalEnum,
        val intervalNum: Long,
        val limit: Long
    )

    @JsonIgnoreProperties(ignoreUnknown = false)
    data class Asset(
        val asset: String,
        val marginAvailable: Boolean,
        val autoAssetExchange: BigDecimal?
    )

    @JsonIgnoreProperties(ignoreUnknown = false)
    data class Symbol(
        val symbol: String,
        val pair: String,
        val contractType: String,
        val deliveryDate: Long,
        val onboardDate: Long,
        val status: String,
        val maintMarginPercent: BigDecimal,
        val requiredMarginPercent: BigDecimal,
        val baseAsset: String,
        val quoteAsset: String,
        val marginAsset: String,
        val pricePrecision: Int,
        val quantityPrecision: Int,
        val baseAssetPrecision: Int,
        val quotePrecision: Int,
        val underlyingType: String,
        val underlyingSubType: List<String>,
        val settlePlan: Long,
        val triggerProtect: BigDecimal,
        @JsonDeserialize(contentUsing = SymbolFilter.Deserializer::class)
        val filters: List<SymbolFilter>,
        val orderTypes: List<OrderTypeEnum>,
        val timeInForce: List<TimeInForceEnum>,
        val liquidationFee: BigDecimal,
        val marketTakeBound: BigDecimal,
    ) {
        sealed class SymbolFilter {
            @JsonIgnoreProperties(ignoreUnknown = false)
            data class PriceFilter(
                val filterType: SymbolFilterType,
                val minPrice: BigDecimal,
                val maxPrice: BigDecimal,
                val tickSize: BigDecimal
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class LotSize(
                val filterType: SymbolFilterType,
                val minQty: BigDecimal,
                val maxQty: BigDecimal,
                val stepSize: BigDecimal
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class MarketLotSize(
                val filterType: SymbolFilterType,
                val minQty: BigDecimal,
                val maxQty: BigDecimal,
                val stepSize: BigDecimal
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class MaxNumOrders(
                val filterType: String,
                val limit: Int
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class MaxNumAlgoOrders(
                val filterType: SymbolFilterType,
                val limit: Int
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class MinNotional(
                val filterType: SymbolFilterType,
                val notional: BigDecimal
            ) : SymbolFilter()

            @JsonIgnoreProperties(ignoreUnknown = false)
            data class PercentPrice(
                val filterType: SymbolFilterType,
                val multiplierUp: BigDecimal,
                val multiplierDown: BigDecimal,
                val multiplierDecimal: Int
            ) : SymbolFilter()

            enum class SymbolFilterType {
                PRICE_FILTER, LOT_SIZE, MARKET_LOT_SIZE, MAX_NUM_ORDERS, MAX_NUM_ALGO_ORDERS, MIN_NOTIONAL, PERCENT_PRICE
            }

            class Deserializer : JsonDeserializer<SymbolFilter>() {
                override fun deserialize(jp: JsonParser, ctx: DeserializationContext): SymbolFilter {
                    val node = jp.codec.readTree<JsonNode>(jp)
                    val json = node.toString()
                    return when (SymbolFilterType.valueOf(node["filterType"].asText())) {
                        SymbolFilterType.PRICE_FILTER -> JsonToObject.convert(json, PriceFilter::class.java)
                        SymbolFilterType.PERCENT_PRICE -> JsonToObject.convert(json, PercentPrice::class.java)
                        SymbolFilterType.LOT_SIZE -> JsonToObject.convert(json, LotSize::class.java)
                        SymbolFilterType.MIN_NOTIONAL -> JsonToObject.convert(json, MinNotional::class.java)
                        SymbolFilterType.MAX_NUM_ORDERS -> JsonToObject.convert(json, MaxNumOrders::class.java)
                        SymbolFilterType.MAX_NUM_ALGO_ORDERS -> JsonToObject.convert(json, MaxNumAlgoOrders::class.java)
                        SymbolFilterType.MARKET_LOT_SIZE -> JsonToObject.convert(json, MarketLotSize::class.java)
                    }
                }
            }
        }
    }

    enum class ExchangeInfoFuturesType {
        U_MARGINED
    }
}

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder
@JsonIgnoreProperties(ignoreUnknown = false)
data class Candle(
    val openTime: Long,
    val open: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val close: BigDecimal,
    val volume: BigDecimal,
    val closeTime: Long,
    val quoteAssetVolume: BigDecimal,
    val numberOfTrades: Long,
    val takerBuyBaseAssetVolume: BigDecimal,
    val takerBuyQuoteAssetVolume: BigDecimal,
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class MarkPrice(
    val symbol: String,
    val markPrice: BigDecimal,
    val indexPrice: BigDecimal,
    val estimatedSettlePrice: BigDecimal,
    val lastFundingRate: BigDecimal,
    val nextFundingTime: Long,
    val interestRate: BigDecimal,
    val time: Long
) {

    @JsonDeserialize(using = Deserializer::class)
    data class List(val list: kotlin.collections.List<MarkPrice>)

    class Deserializer : JsonDeserializer<List>() {
        private val typeReference = object : TypeReference<kotlin.collections.List<MarkPrice>>() {}
        override fun deserialize(jp: JsonParser, ctx: DeserializationContext): List {
            val node = jp.codec.readTree<JsonNode>(jp)
            val json = node.toString()
            val list = when (node.isObject) {
                true -> listOf(JsonToObject.convert(json, MarkPrice::class.java))
                false -> JsonToObject.convert(json, typeReference)
            }
            return List(list)
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = false)
data class Price(
    val symbol: String,
    val price: BigDecimal,
    val time: Long
) {

    @JsonDeserialize(using = Deserializer::class)
    data class List(val list: kotlin.collections.List<Price>)

    class Deserializer : JsonDeserializer<List>() {
        private val typeReference = object : TypeReference<kotlin.collections.List<Price>>() {}
        override fun deserialize(jp: JsonParser, ctx: DeserializationContext): List {
            val node = jp.codec.readTree<JsonNode>(jp)
            val json = node.toString()
            val list = when (node.isObject) {
                true -> listOf(JsonToObject.convert(json, Price::class.java))
                false -> JsonToObject.convert(json, typeReference)
            }
            return List(list)
        }
    }
}