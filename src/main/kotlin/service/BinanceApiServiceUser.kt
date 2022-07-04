package service

import ORDER_SIDE
import ORDER_STATUS
import ORDER_TYPE
import POSITION_SIDE
import RESPONSE_TYPE
import TIME_IN_FORCE
import WORKING_TYPE
import com.fasterxml.jackson.annotation.*
import model.rest.Empty
import retrofit2.Call
import retrofit2.http.*
import java.math.BigDecimal

interface BinanceApiServiceUser {

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/fapi/v1/openOrders")
    fun openOrders(
        @Query("symbol") symbol: String?,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<List<Order>>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/fapi/v2/positionRisk")
    fun positions(
        @Query("symbol") symbol: String?,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<List<Position>>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/order")
    fun newOrder(
        @Query("symbol") symbol: String,
        @Query("side") side: ORDER_SIDE,
        @Query("positionSide") positionSide: POSITION_SIDE?,
        @Query("type") type: ORDER_TYPE,
        @Query("timeInForce") timeInForce: TIME_IN_FORCE?,
        @Query("quantity") quantity: String?,
        @Query("reduceOnly") reduceOnly: Boolean?,
        @Query("price") price: String?,
        @Query("newClientOrderId") newClientOrderId: String?,
        @Query("stopPrice") stopPrice: String?,
        @Query("closePosition") closePosition: Boolean?,
        @Query("activationPrice") activationPrice: String?,
        @Query("callbackRate") callbackRate: String?,
        @Query("workingType") workingType: WORKING_TYPE?,
        @Query("newOrderRespType") newOrderRespType: RESPONSE_TYPE?,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<NewOrder>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @DELETE("/fapi/v1/order")
    fun deleteOrder(
        @Query("symbol") symbol: String,
        @Query("orderId") orderId: String?,
        @Query("origClientOrderId") origClientOrderId: String?,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<NewOrder>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/fapi/v2/balance")
    fun futuresAccountBalance(
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<List<FuturesAccountBalance>>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/listenKey")
    fun startUserDataStream(): Call<ListenKey>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @PUT("/fapi/v1/listenKey")
    fun keepAliveUserDataStream(): Call<Empty>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY_HEADER)
    @DELETE("/api/v3/userDataStream")
    fun closeUserDataStream(@Query("listenKey") listenKey: String): Call<Empty>
}

@JsonIgnoreProperties(ignoreUnknown = false)
data class Order(
    val avgPrice: BigDecimal,
    val clientOrderId: String,
    val cumQuote: BigDecimal,
    val executedQty: BigDecimal,
    val orderId: Long,
    val origQty: BigDecimal,
    val origType: ORDER_TYPE,
    val price: BigDecimal,
    val reduceOnly: Boolean,
    val side: ORDER_SIDE,
    val positionSide: POSITION_SIDE,
    val status: ORDER_STATUS,
    val stopPrice: BigDecimal,
    val closePosition: Boolean,
    val symbol: String,
    val time: Long,
    val timeInForce: String,
    val type: ORDER_TYPE,
    val activatePrice: BigDecimal?,
    val priceRate: BigDecimal?,
    val updateTime: Long,
    val workingType: WORKING_TYPE,
    val priceProtect: Boolean
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class Position(
    val symbol: String,
    val isAutoAddMargin: Boolean,
    val isolatedMargin: BigDecimal,
    val isolatedWallet: BigDecimal,
    val leverage: Int,
    val liquidationPrice: BigDecimal,
    val marginType: String,
    val markPrice: BigDecimal,
    val maxNotionalValue: BigDecimal,
    val notional: BigDecimal,
    val positionAmt: BigDecimal,
    val positionSide: POSITION_SIDE,
    val unRealizedProfit: BigDecimal,
    val updateTime: Long,
    val entryPrice: BigDecimal,
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class NewOrder(
    val clientOrderId: String,
    val cumQty: BigDecimal,
    val cumQuote: BigDecimal,
    val executedQty: BigDecimal,
    val orderId: Long,
    val avgPrice: BigDecimal,
    val origQty: BigDecimal,
    val price: BigDecimal,
    val reduceOnly: Boolean,
    val side: ORDER_SIDE,
    val positionSide: POSITION_SIDE,
    val status: ORDER_STATUS,
    val stopPrice: BigDecimal,
    val closePosition: Boolean,
    val symbol: String,
    val timeInForce: TIME_IN_FORCE,
    val type: ORDER_TYPE,
    val origType: ORDER_TYPE,
    val activatePrice: BigDecimal?,
    val priceRate: BigDecimal?,
    val updateTime: Long,
    val workingType: WORKING_TYPE,
    val priceProtect: Boolean
)

@JsonIgnoreProperties(ignoreUnknown = false)
data class FuturesAccountBalance(
    val accountAlias: String,
    val asset: String,
    val balance: BigDecimal,
    val crossWalletBalance: BigDecimal,
    val crossUnPnl: BigDecimal,
    val availableBalance: BigDecimal,
    val maxWithdrawAmount: BigDecimal,
    val marginAvailable: Boolean,
    val updateTime: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListenKey(
    @JsonProperty("listenKey")
    val listenKey: String
)