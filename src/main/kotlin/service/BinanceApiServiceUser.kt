package service

import MARGIN_TYPE
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
    @GET("/fapi/v1/multiAssetsMargin")
    fun currentMultiAssetsMode(
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<MultiAssetsMargin>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/multiAssetsMargin")
    fun changeMultiAssetsMode(
        @Query("multiAssetsMargin") multiAssetsMargin: String,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<CodeMsg>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/fapi/v2/account")
    fun accountInformation(
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<AccountInformation>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/listenKey")
    fun startUserDataStream(): Call<ListenKey>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @PUT("/fapi/v1/listenKey")
    fun keepAliveUserDataStream(): Call<Empty>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY_HEADER)
    @DELETE("/api/v3/userDataStream")
    fun closeUserDataStream(@Query("listenKey") listenKey: String): Call<Empty>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/fapi/v1/leverageBracket")
    fun leverageBracket(
        @Query("symbol") symbol: String?,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<List<LeverageBracket>>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/leverage")
    fun changeInitialLeverage(
        @Query("symbol") symbol: String,
        @Query("leverage") leverage: String,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<ChangeInitialLeverage>

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/fapi/v1/marginType")
    fun changeMarginType(
        @Query("symbol") symbol: String,
        @Query("marginType") marginType: String,
        @Query("recvWindow") recvWindow: Long?,
        @Query("timestamp") timestamp: Long
    ): Call<CodeMsg>

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
) {

}

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
data class MultiAssetsMargin(
    val multiAssetsMargin: Boolean,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountInformation(
    val feeTier: Int,
    val canTrade: Boolean,
    val canDeposit: Boolean,
    val canWithdraw: Boolean,
    val updateTime: Long,
    val totalInitialMargin: BigDecimal,
    val totalMaintMargin: BigDecimal,
    val totalWalletBalance: BigDecimal,
    val totalUnrealizedProfit: BigDecimal,
    val totalMarginBalance: BigDecimal,
    val totalPositionInitialMargin: BigDecimal,
    val totalOpenOrderInitialMargin: BigDecimal,
    val totalCrossWalletBalance: BigDecimal,
    val totalCrossUnPnl: BigDecimal,
    val availableBalance: BigDecimal,
    val maxWithdrawAmount: BigDecimal,
    val assets: List<Asset>,
    val positions: List<Position>,
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Asset(
        val asset: String,
        val walletBalance: BigDecimal,
        val unrealizedProfit: BigDecimal,
        val marginBalance: BigDecimal,
        val maintMargin: BigDecimal,
        val initialMargin: BigDecimal,
        val positionInitialMargin: BigDecimal,
        val openOrderInitialMargin: BigDecimal,
        val crossWalletBalance: BigDecimal,
        val crossUnPnl: BigDecimal,
        val availableBalance: BigDecimal,
        val maxWithdrawAmount: BigDecimal,
        val marginAvailable: Boolean,
        val updateTime: Long,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Position(
        val symbol: String,
        val initialMargin: BigDecimal,
        val maintMargin: BigDecimal,
        val unrealizedProfit: BigDecimal,
        val positionInitialMargin: BigDecimal,
        val openOrderInitialMargin: BigDecimal,
        val leverage: Int,
        val isolated: Boolean,
        val entryPrice: BigDecimal,
        val maxNotional: BigDecimal,
        val bidNotional: BigDecimal,
        val askNotional: BigDecimal,
        val positionSide: POSITION_SIDE,
        val positionAmt: BigDecimal,
        val updateTime: Long,
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CodeMsg(
    val code: Int,
    val msg: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListenKey(
    @JsonProperty("listenKey")
    val listenKey: String
)


data class LeverageBracket(
    val symbol: String,
    val brackets: List<Bracket>
) {
    data class Bracket(
        val bracket: Long,
        val initialLeverage: Long,
        val notionalCap: Long,
        val notionalFloor: Long,
        val maintMarginRatio: BigDecimal,
        val cum: Long,
    )
}

data class ChangeInitialLeverage(
    val leverage: Int,
    val maxNotionalValue: String,
    val symbol: String
)